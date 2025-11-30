package ua.com.xela.soulgem_arsenal.network

import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.network.PacketDistributor
import ua.com.xela.soulgem_arsenal.network.ForgeNetwork.CHANNEL
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.*
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceServer
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

class ForgeNetworkServiceServer : INetworkServiceServer {

    override fun broadcastCooldownStart(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        CHANNEL.send(
            CooldownStartPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            ),
            PacketDistributor.PLAYER.with(player)
        )
    }

    override fun broadcastCooldownTick(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        CHANNEL.send(
            CooldownTickPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            ),
            PacketDistributor.PLAYER.with(player)
        )
    }

    override fun broadcastCooldownChargeRestored(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        restoredChargeIndex: Int
    ) {
        CHANNEL.send(
            CooldownChargeRestoredPayload(
                key = key,
                chargeIndex = restoredChargeIndex
            ),
            PacketDistributor.PLAYER.with(player)
        )
    }

    override fun broadcastCooldownFinished(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        CHANNEL.send(
            CooldownFinishedPayload(
                key = key
            ),
            PacketDistributor.PLAYER.with(player)
        )
    }

    override fun broadcastCooldownCanceled(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        CHANNEL.send(
            CooldownCanceledPayload(
                key = key
            ),
            PacketDistributor.PLAYER.with(player)
        )
    }
}
