package ua.com.xela.soulgem_arsenal.skills.system

import kotlinx.coroutines.Job
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.core.coroutines.MinecraftServerScope
import ua.com.xela.soulgem_arsenal.network.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.registry.ItemIdComponents
import ua.com.xela.soulgem_arsenal.skills.base.CooldownKey
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.ItemRef
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowHailSkillCooldown
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillCooldown
import java.util.concurrent.ConcurrentHashMap

object SkillCooldownManager {
    private val activeJobs = ConcurrentHashMap<CooldownKey, Job>()


    fun resumeForPlayer(level: ServerLevel, player: ServerPlayer) {
        val storage = CooldownStorage.get(level)

        val playerStates = storage.states
            .filterKeys { it.playerId == player.uuid }

        for ((key, stored) in playerStates) {
            if ((stored.chargeToRestore - stored.currentChargeIndex) <= 0) {
                storage.states.remove(key)
                storage.setDirty()
                continue
            }

            resumeSingleCooldown(player, key, stored)
        }
    }


    private fun resumeSingleCooldown(
        player: ServerPlayer,
        key: CooldownKey,
        stored: CooldownStorage.CooldownState,
    ) {
        startCooldown(
            player = player,
            itemRef = key.item,
            skillKey = key.skill,
            itemId = key.itemId,
            chargesToRestore = stored.chargeToRestore - stored.currentChargeIndex,
            secondsPerCharge = stored.totalSecondsPerCharge,
            onChargeRestored = { player, ref, id, key, i ->
                val stack = findItemByInstanceId(player.server, itemId = id) ?: return@startCooldown
                when (key) {
                    TeleportSkill.KEY -> TeleportSkillCooldown.onCharge(stack)
                    ArrowsHailSkill.KEY -> ArrowHailSkillCooldown.onCharge(stack)
                    else -> error("$key WAS NOT mapped for cooldown restore")
                }
            }
        )
    }

    private fun findItemByInstanceId(server: MinecraftServer, itemId: ItemInstanceId): ItemStack? {
        for (player in server.playerList.players) {
            listOf(player.mainHandItem, player.offhandItem).forEach { stack ->
                val id = stack.get(ItemIdComponents.ITEM_ID.get())
                if (id == itemId) return stack
            }

            for (stack in player.inventory.items) {
                val id = stack.get(ItemIdComponents.ITEM_ID.get())
                if (id == itemId) return stack
            }
        }

        for (level in server.allLevels) {
            for (entity in level.allEntities) {
                if (entity is ItemEntity) {
                    val stack = entity.item
                    val id = stack.get(ItemIdComponents.ITEM_ID.get())
                    if (id == itemId) return stack
                }
            }
        }

        return null
    }


    fun startCooldown(
        player: ServerPlayer,
        itemRef: ItemRef,
        itemId: ItemInstanceId,
        skillKey: SkillKey,
        chargesToRestore: Int,
        secondsPerCharge: Int,
        onChargeRestored: suspend (ServerPlayer, ItemRef, ItemInstanceId, SkillKey, Int) -> Unit
    ) {
        if (chargesToRestore <= 0 || secondsPerCharge <= 0) return

        val key = CooldownKey(player.uuid, itemRef, itemId, skillKey)

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
                        endGameTime = secondsPerCharge.toGameTime(player.serverLevel())
                    )
                )

                Services.NETWORK.broadcastCooldownStart(
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
                            endGameTime = secondsPerCharge.toGameTime(player.serverLevel())
                        )
                    )
                    Services.NETWORK.broadcastCooldownTick(
                        player = player,
                        key = key.toNetworkCooldownKey(),
                        totalSeconds = secondsPerCharge,
                        remainingSeconds = remaining
                    )
                }

                onChargeRestored(player, itemRef, itemId, skillKey, chargeIndex)
                Services.NETWORK.broadcastCooldownChargeRestored(
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
            Services.NETWORK.broadcastCooldownFinished(
                player = player,
                key = key.toNetworkCooldownKey()
            )

            activeJobs.remove(key)
        }

        activeJobs[key] = job
    }

    fun cancel(player: ServerPlayer, itemRef: ItemRef, itemId: ItemInstanceId, skillKey: SkillKey) {
        val key = CooldownKey(player.uuid, itemRef, itemId, skillKey)
        activeJobs.remove(key)?.cancel()
        updateStorage(
            player,
            key,
            null
        )
        Services.NETWORK.broadcastCooldownCanceled(player, key.toNetworkCooldownKey())
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

}

fun Int.toGameTime(level: ServerLevel): Long {
    val now: Long = level.gameTime
    val cooldownTicks: Long = this.toLong() * 20L
    return now + cooldownTicks

}
