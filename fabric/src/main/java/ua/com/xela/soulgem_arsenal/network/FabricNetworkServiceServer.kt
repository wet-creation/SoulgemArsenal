package ua.com.xela.soulgem_arsenal.network

import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.*
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceServer
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

class FabricNetworkServiceServer : INetworkServiceServer {

    override fun broadcastCooldownStart(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownStartPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            )
        )
    }

    override fun broadcastCooldownTick(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownTickPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            )
        )
    }

    override fun broadcastCooldownChargeRestored(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        restoredChargeIndex: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownChargeRestoredPayload(
                key = key,
                chargeIndex = restoredChargeIndex,
            )
        )
    }

    override fun broadcastCooldownFinished(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        ServerPlayNetworking.send(
            player, CooldownFinishedPayload(
                key = key,
            )
        )
    }

    override fun broadcastCooldownCanceled(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        ServerPlayNetworking.send(
            player, CooldownCanceledPayload(
                key = key,
            )
        )
    }
}
