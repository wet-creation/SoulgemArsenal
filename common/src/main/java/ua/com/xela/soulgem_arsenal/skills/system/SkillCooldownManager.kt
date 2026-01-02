package ua.com.xela.soulgem_arsenal.skills.system

import kotlinx.coroutines.Job
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.core.coroutines.MinecraftServerScope
import ua.com.xela.soulgem_arsenal.core.utils.orRandom
import ua.com.xela.soulgem_arsenal.platform.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.api.SkillCooldown
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.*
import java.util.concurrent.ConcurrentHashMap


object SkillCooldownManager {

    private lateinit var level: ServerLevel
    private lateinit var server: MinecraftServer

    private val activeJobs = ConcurrentHashMap<CooldownKey, Job>()
    private val cooldownSubscribers: MutableMap<CooldownKey, ServerPlayer> = mutableMapOf()
    private val skillCooldowns: MutableMap<SkillKey, SkillCooldown<out Skill>> = mutableMapOf()

    inline fun <reified T : Skill> onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        updater: (T) -> T
    ) {
        val stack = player.findInPlayer(id = id) ?: return
        val current = stack.getSkill<T>() ?: return
        val updated = updater(current)
        stack.setSkill(updated)
    }

    fun register(pair: Pair<SkillKey, SkillCooldown<out Skill>>) {
        skillCooldowns.putIfAbsent(pair.first, pair.second)
    }

    fun resumeForPlayer(player: ServerPlayer) {
        val storage = CooldownStorage.get(this.level)
        val listOfInstanceId = player.getAllInstanceId()

        val statesToResume = storage.states.filterKeys { listOfInstanceId.contains(it.itemId) }
        statesToResume.forEach { (key, stored) ->
            resumeSingleCooldown(player, key, stored)
        }
    }

    fun resumeForServer(server: MinecraftServer, level: ServerLevel) {
        this.level = level
        this.server = server
        val storage = CooldownStorage.get(level)
        val statesToResume = storage.states.filterValues { it.wasCanceled.not() }

        statesToResume.forEach { (key, stored) ->
            val player = server.playerList.getPlayer(stored.playerId)
            resumeSingleCooldown(player, key, stored)
        }


    }

    fun onPlayerDisconnect(player: ServerPlayer) {
        val storage = CooldownStorage.get(this.level)
        val listOfInstanceId = player.getAllInstanceId()

        val statesToCancel = storage.states.filterKeys { listOfInstanceId.contains(it.itemId) }
        statesToCancel.keys.forEach { key ->
            cancel(player, key)
            cooldownSubscribers.remove(key)
        }
    }

    fun subscribePlayer(serverPlayer: ServerPlayer, itemStack: ItemStack) {
        val id = itemStack.getInstanceIdOrNull() ?: return
        val skills = itemStack.getAllSkills()
        val storage = CooldownStorage.get(level)

        skills.forEach { skill ->
            val key = CooldownKey(id, skill.key)
            val state = storage.states[key] ?: return@forEach

            cooldownSubscribers[key] = serverPlayer
            if (activeJobs[key] != null) {
                sendPacket(key) {
                    Services.NETWORK_SERVER.broadcastCooldownStart(
                        player = it,
                        key = key.toNetworkCooldownKey(),
                        totalSeconds = state.totalSecondsPerCharge,
                        remainingSeconds = state.remainingSeconds
                    )
                }
            }

            applyPendingCharges(
                itemId = id,
                skillKey = skill.key,
                serverPlayer = serverPlayer,
                state = state
            )

            if (CooldownStorage.get(level).states[key] == null) {

                sendPacket(key) {
                    Services.NETWORK_SERVER.broadcastCooldownFinished(
                        player = it,
                        key = key.toNetworkCooldownKey()
                    )
                }
                cooldownSubscribers.remove(key)
            }
        }
    }


    fun unsubscribePlayer(itemStack: ItemStack) {
        val id = itemStack.getInstanceIdOrNull() ?: return
        val skills = itemStack.getAllSkills()
        skills.forEach {
            val key = CooldownKey(id, it.key)
            cooldownSubscribers.remove(key)
        }
    }


    fun startCooldown(
        player: ServerPlayer?,
        itemId: ItemInstanceId,
        skillKey: SkillKey,
        chargesToRestore: Int,
        secondsPerCharge: Int,
    ) {
        if (chargesToRestore <= 0 || secondsPerCharge <= 0) return

        val key = CooldownKey(itemId, skillKey)
        player?.let {
            cooldownSubscribers[key] = it
        }
        val wholeTime = (secondsPerCharge * chargesToRestore).toGameTime()
        val storage = CooldownStorage.get(level)
        activeJobs.remove(key)?.cancel()

        val job = MinecraftServerScope.launchInScope {
            var currentChargeIndex = 0
            updateStorage(
                key,
                CooldownStorage.CooldownState(
                    totalSecondsPerCharge = secondsPerCharge,
                    remainingSeconds = secondsPerCharge,
                    currentChargeIndex = 0,
                    chargeToRestore = chargesToRestore,
                    playerId = player?.uuid.orRandom(),
                    endGameTime = wholeTime,
                    appliedCharges = 0
                )
            )
            while (chargesToRestore > currentChargeIndex) {
                var remaining = secondsPerCharge

                sendPacket(key) {
                    Services.NETWORK_SERVER.broadcastCooldownStart(
                        player = it,
                        key = key.toNetworkCooldownKey(),
                        totalSeconds = secondsPerCharge,
                        remainingSeconds = remaining
                    )
                }

                while (remaining > 0) {
                    delayAndWaitIfPaused(1000L)
                    remaining--
                    updateStorage(
                        key,
                        (storage.states[key])?.copy(
                            totalSecondsPerCharge = secondsPerCharge,
                            remainingSeconds = remaining,
                            currentChargeIndex = currentChargeIndex,
                        )
                    )
                    sendPacket(key) {
                        Services.NETWORK_SERVER.broadcastCooldownTick(
                            player = it,
                            key = key.toNetworkCooldownKey(),
                            totalSeconds = secondsPerCharge,
                            remainingSeconds = remaining
                        )
                    }

                }
                currentChargeIndex++
                updateStorage(
                    key,
                    (storage.states[key])?.copy(
                        totalSecondsPerCharge = secondsPerCharge,
                        remainingSeconds = remaining,
                        currentChargeIndex = currentChargeIndex,
                    )
                )
                cooldownSubscribers[key]?.let {
                    defaultChargeFlow(
                        id = itemId,
                        player = it,
                        key = skillKey,
                        charge = 1
                    )

                    val storage = CooldownStorage.get(level)
                    val s = storage.states[key]
                    if (s != null && it.ownsInstanceId(key.itemId)) {
                        storage.updateState(
                            key,
                            s.copy(appliedCharges = s.appliedCharges + 1)
                        )
                    }
                }
                sendPacket(key) {
                    Services.NETWORK_SERVER.broadcastCooldownChargeRestored(
                        player = it,
                        key = key.toNetworkCooldownKey(),
                        restoredChargeIndex = currentChargeIndex
                    )
                }
            }
            onCooldownFinish(
                key = key,
                state = (storage.states[key])?.copy(
                    currentChargeIndex = chargesToRestore,
                    remainingSeconds = 0
                )
            )
        }

        activeJobs[key] = job
    }

    private fun resumeSingleCooldown(
        player: ServerPlayer?,
        key: CooldownKey,
        stored: CooldownStorage.CooldownState,
    ) {
        startCooldown(
            player = player,
            skillKey = key.skill,
            itemId = key.itemId,
            chargesToRestore = stored.chargeToRestore - stored.currentChargeIndex,
            secondsPerCharge = stored.totalSecondsPerCharge,
        )
    }


    private fun defaultChargeFlow(
        id: ItemInstanceId,
        player: ServerPlayer,
        key: SkillKey,
        charge: Int
    ) {
        skillCooldowns[key]?.onCharge(player, id, charge) ?: error("$key WAS NOT mapped for cooldown restore")
    }


    private fun cancel(player: ServerPlayer, key: CooldownKey) {
        val storage = CooldownStorage.get(level)

        updateStorage(
            key,
            storage.states[key]?.copy(
                wasCanceled = true
            )
        )
        activeJobs.remove(key)?.cancel()

        Services.NETWORK_SERVER.broadcastCooldownCanceled(player, key.toNetworkCooldownKey())
    }

    private fun applyPendingCharges(
        itemId: ItemInstanceId,
        serverPlayer: ServerPlayer,
        skillKey: SkillKey,
        state: CooldownStorage.CooldownState
    ) {
        val key = CooldownKey(itemId, skillKey)

        val restored = state.currentChargeIndex
        val applied = state.appliedCharges
        val total = state.chargeToRestore
        val delta = restored - applied
        if (delta <= 0) return

        repeat(delta) {
            defaultChargeFlow(itemId, serverPlayer, skillKey, 1)
        }

        val storage = CooldownStorage.get(level)
        val newApplied = applied + delta
        val newState = state.copy(appliedCharges = newApplied)

        if (newApplied >= total) {
            clearStorage(key)
        } else {
            storage.updateState(key, newState)
        }
    }


    private fun sendPacket(key: CooldownKey, onSend: (ServerPlayer) -> Unit) {
        cooldownSubscribers[key]?.let(onSend)
    }

    private fun onCooldownFinish(
        key: CooldownKey,
        state: CooldownStorage.CooldownState?
    ) {
        updateStorage(
            key,
            state
        )
        sendPacket(key) {
            Services.NETWORK_SERVER.broadcastCooldownFinished(
                player = it,
                key = key.toNetworkCooldownKey()
            )
        }
        cooldownSubscribers.remove(key)
        activeJobs.remove(key)
    }

    private fun updateStorage(
        key: CooldownKey,
        state: CooldownStorage.CooldownState?
    ) {
        val storage = CooldownStorage.get(level)
        storage.updateState(
            key,
            state
        )
    }

    private fun Int.toGameTime(): Long {
        val now: Long = level.gameTime

        val cooldownTicks: Long = this.toLong() * 20L
        return now + cooldownTicks

    }

    private fun clearStorage(key: CooldownKey) {
        val storage = CooldownStorage.get(level)
        storage.updateState(key, null)
    }

}

