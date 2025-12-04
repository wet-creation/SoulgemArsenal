package ua.com.xela.soulgem_arsenal.skills.system

import kotlinx.coroutines.Job
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.core.coroutines.MinecraftServerScope
import ua.com.xela.soulgem_arsenal.platform.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.*
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowHailSkillCooldown
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillCooldown
import java.util.concurrent.ConcurrentHashMap

object SkillCooldownManager {
    private val activeJobs = ConcurrentHashMap<CooldownKey, Job>()

    fun resumeForPlayer(level: ServerLevel, player: ServerPlayer) {
        val storage = CooldownStorage.get(level)
        val listOfInstanceId = player.getAllInstanceId()

        val statesToResume = storage.states.filterKeys { listOfInstanceId.contains(it.itemId) }

        statesToResume.forEach { (key, stored) ->
            resumeSingleCooldown(player, key, stored)
        }
    }

    fun onPlayerDisconnect(level: ServerLevel, player: ServerPlayer) {
        val storage = CooldownStorage.get(level)
        val listOfInstanceId = player.getAllInstanceId()

        val statesToCancel = storage.states.filterKeys { listOfInstanceId.contains(it.itemId) }

        statesToCancel.keys.forEach { key ->
            cancel(player, key)
        }
    }


    private fun resumeSingleCooldown(
        player: ServerPlayer,
        key: CooldownKey,
        stored: CooldownStorage.CooldownState,
    ) {
        startCooldown(
            player = player,
            skillKey = key.skill,
            itemId = key.itemId,
            chargesToRestore = stored.chargeToRestore - stored.currentChargeIndex,
            secondsPerCharge = stored.totalSecondsPerCharge,
            onChargeRestored = { player, id, key, i ->
                defaultChargeFlow(player, id, key)
            }
        )
    }


    fun defaultChargeFlow(
        player: ServerPlayer,
        id: ItemInstanceId,
        key: SkillKey
    ) {
        when (key) {
            TeleportSkill.KEY -> TeleportSkillCooldown.onCharge(player, id)
            ArrowsHailSkill.KEY -> ArrowHailSkillCooldown.onCharge(player, id)
            else -> error("$key WAS NOT mapped for cooldown restore")
        }
    }

    inline fun <reified T : Skill> onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        updater: (T) -> T
    ) {
        val stack = ItemInstanceId.findStackById(player.server, id = id) ?: return
        val current = stack.getSkill<T>() ?: return
        stack.setSkill(updater(current))
    }

    fun startCooldown(
        player: ServerPlayer,
        itemId: ItemInstanceId,
        skillKey: SkillKey,
        chargesToRestore: Int,
        secondsPerCharge: Int,
        onChargeRestored: suspend (ServerPlayer, ItemInstanceId, SkillKey, Int) -> Unit
    ) {
        if (chargesToRestore <= 0 || secondsPerCharge <= 0) return

        val key = CooldownKey(itemId, skillKey)
        val wholeTime = (secondsPerCharge * chargesToRestore).toGameTime(player.serverLevel())
        activeJobs.remove(key)?.cancel()

        val job = MinecraftServerScope.launchInScope {
            repeat(chargesToRestore) { chargeIndex ->
                var remaining = secondsPerCharge
                updateStorage(
                    player,
                    key,
                    CooldownStorage.CooldownState(
                        totalSecondsPerCharge = secondsPerCharge,
                        remainingSeconds = remaining,
                        currentChargeIndex = chargeIndex,
                        chargeToRestore = chargesToRestore,
                        playerId = player.uuid,
                        endGameTime = wholeTime
                    )
                )

                Services.NETWORK_SERVER.broadcastCooldownStart(
                    player = player,
                    key = key.toNetworkCooldownKey(),
                    totalSeconds = secondsPerCharge,
                    remainingSeconds = remaining
                )

                while (remaining > 0) {
                    delayAndWaitIfPaused(1000L)
                    remaining--
                    updateStorage(
                        player,
                        key,
                        CooldownStorage.CooldownState(
                            totalSecondsPerCharge = secondsPerCharge,
                            remainingSeconds = remaining,
                            currentChargeIndex = chargeIndex,
                            chargeToRestore = chargesToRestore,
                            playerId = player.uuid,
                            endGameTime = wholeTime
                        )
                    )
                    Services.NETWORK_SERVER.broadcastCooldownTick(
                        player = player,
                        key = key.toNetworkCooldownKey(),
                        totalSeconds = secondsPerCharge,
                        remainingSeconds = remaining
                    )

                }

                onChargeRestored(player, itemId, skillKey, chargeIndex)
                Services.NETWORK_SERVER.broadcastCooldownChargeRestored(
                    player = player,
                    key = key.toNetworkCooldownKey(),
                    restoredChargeIndex = chargeIndex
                )
            }
            updateStorage(
                player,
                key,
                null
            )
            Services.NETWORK_SERVER.broadcastCooldownFinished(
                player = player,
                key = key.toNetworkCooldownKey()
            )

            activeJobs.remove(key)
        }

        activeJobs[key] = job
    }

    fun cancel(player: ServerPlayer, key: CooldownKey) {
        activeJobs.remove(key)?.cancel()
        Services.NETWORK_SERVER.broadcastCooldownCanceled(player, key.toNetworkCooldownKey())
    }

    private fun updateStorage(
        player: ServerPlayer,
        key: CooldownKey,
        state: CooldownStorage.CooldownState?
    ) {
        val level = player.serverLevel()
        val storage = CooldownStorage.get(level)

        if (state == null) {
            storage.states.remove(key)
        } else {
            storage.states[key] = state
        }

        storage.setDirty()
    }

    private fun Int.toGameTime(level: ServerLevel): Long {
        val now: Long = level.gameTime

        val cooldownTicks: Long = this.toLong() * 20L
        return now + cooldownTicks

    }

}

