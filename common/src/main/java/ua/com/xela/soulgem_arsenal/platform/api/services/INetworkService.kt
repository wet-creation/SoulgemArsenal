package ua.com.xela.soulgem_arsenal.platform.api.services

import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

interface INetworkService {

    fun registerReceivers()

    fun sendActivateSkill()
    fun broadcastCooldownStart(player: ServerPlayer, key: NetworkCooldownKey, totalSeconds: Int, remainingSeconds: Int)
    fun broadcastCooldownTick(player: ServerPlayer, key: NetworkCooldownKey, totalSeconds: Int, remainingSeconds: Int)
    fun broadcastCooldownChargeRestored(player: ServerPlayer, key: NetworkCooldownKey, restoredChargeIndex: Int)
    fun broadcastCooldownFinished(player: ServerPlayer, key: NetworkCooldownKey)
    fun broadcastCooldownCanceled(player: ServerPlayer, key: NetworkCooldownKey)
}
