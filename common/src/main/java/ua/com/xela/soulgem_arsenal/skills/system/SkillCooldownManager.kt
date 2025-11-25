package ua.com.xela.soulgem_arsenal.skills.system

import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.core.MinecraftServerScope
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.skills.base.CooldownKey
import ua.com.xela.soulgem_arsenal.skills.base.ItemRef
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import java.util.concurrent.ConcurrentHashMap

object SkillCooldownManager {

    private val activeJobs = ConcurrentHashMap<CooldownKey, Job>()


    fun startCooldown(
        player: ServerPlayer,
        itemRef: ItemRef,
        skillKey: SkillKey,
        chargesToRestore: Int,
        secondsPerCharge: Int,
        onChargeRestored: suspend (ServerPlayer, ItemRef, SkillKey, Int) -> Unit
    ) {
        if (chargesToRestore <= 0 || secondsPerCharge <= 0) return

        val key = CooldownKey(player.uuid, itemRef, skillKey)

        activeJobs.remove(key)?.cancel()

        val job = MinecraftServerScope.launchInScope {
            repeat(chargesToRestore) { chargeIndex ->
                var remaining = secondsPerCharge

                Services.NETWORK.broadcastCooldownStart(
                    player = player,
                    key = key.toNetworkCooldownKey(),
                    totalSeconds = secondsPerCharge,
                    remainingSeconds = remaining
                )

                while (remaining > 0) {
                    MinecraftServerScope.waitIfPaused()
                    delay(1000L)
                    remaining--

                    Services.NETWORK.broadcastCooldownTick(
                        player = player,
                        key = key.toNetworkCooldownKey(),
                        totalSeconds = secondsPerCharge,
                        remainingSeconds = remaining
                    )
                }

                onChargeRestored(player, itemRef, skillKey, chargeIndex)

                Services.NETWORK.broadcastCooldownChargeRestored(
                    player = player,
                    key = key.toNetworkCooldownKey(),
                    restoredChargeIndex = chargeIndex
                )
            }

            Services.NETWORK.broadcastCooldownFinished(
                player = player,
                key = key.toNetworkCooldownKey()
            )

            activeJobs.remove(key)
        }

        activeJobs[key] = job
    }

    fun cancel(player: ServerPlayer, itemRef: ItemRef, skillKey: SkillKey) {
        val key = CooldownKey(player.uuid, itemRef, skillKey)
        activeJobs.remove(key)?.cancel()
        Services.NETWORK.broadcastCooldownCanceled(player, key.toNetworkCooldownKey())
    }
}
