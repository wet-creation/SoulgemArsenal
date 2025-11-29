package ua.com.xela.soulgem_arsenal.network

import net.minecraft.network.codec.StreamCodec
import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.ChannelBuilder
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.SimpleChannel
import ua.com.xela.soulgem_arsenal.network.client.ClientCooldownTracker
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownTickPayload
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkService
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey
import ua.com.xela.soulgem_arsenal.skills.system.SkillEvents

class ForgeNetworkService : INetworkService {

    companion object {
        private const val PROTOCOL_VERSION = 1

        private val CHANNEL_NAME = Common.createResourceLocation("main")

        private val CHANNEL: SimpleChannel =
            ChannelBuilder
                .named(CHANNEL_NAME)
                .networkProtocolVersion(PROTOCOL_VERSION)
                .clientAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
                .serverAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
                .simpleChannel()

        private const val ID_SKILL_ACTIVATION = 0
        private const val ID_COOLDOWN_START = 1
        private const val ID_COOLDOWN_TICK = 2
        private const val ID_COOLDOWN_CHARGE_RESTORED = 3
        private const val ID_COOLDOWN_FINISHED = 4
        private const val ID_COOLDOWN_CANCELED = 5
    }

    override fun registerReceivers() {
        // C2S: активация скилла (клиент -> сервер)
        CHANNEL.messageBuilder(
            SkillActivationPayload::class.java,
            ID_SKILL_ACTIVATION
        )
            .codec(StreamCodec.unit(SkillActivationPayload))
            .consumerMainThread { _, ctx ->
                val player = ctx.sender ?: return@consumerMainThread
                SkillEvents.onSkillActivate(player)
            }
            .add()

        // S2C: старт кулдауна
        CHANNEL.messageBuilder(
            CooldownStartPayload::class.java,
            ID_COOLDOWN_START
        )
            .codec(CooldownStartPayload.CODEC)
            .consumerMainThread { msg, _ ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                    Runnable {
                        ClientCooldownTracker.onStart(msg)
                    }
                }
            }
            .add()

        // S2C: тик кулдауна
        CHANNEL.messageBuilder(
            CooldownTickPayload::class.java,
            ID_COOLDOWN_TICK
        )
            .codec(CooldownTickPayload.CODEC)
            .consumerMainThread { msg, _ ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                    Runnable {
                        ClientCooldownTracker.onTick(msg)
                    }
                }
            }
            .add()

        // S2C: один заряд восстановился
        CHANNEL.messageBuilder(
            CooldownChargeRestoredPayload::class.java,
            ID_COOLDOWN_CHARGE_RESTORED
        )
            .codec(CooldownChargeRestoredPayload.CODEC)
            .consumerMainThread { msg, _ ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                    Runnable {
                        ClientCooldownTracker.onChargeRestored(msg)
                    }
                }
            }
            .add()

        // S2C: кулдаун полностью закончился
        CHANNEL.messageBuilder(
            CooldownFinishedPayload::class.java,
            ID_COOLDOWN_FINISHED
        )
            .codec(CooldownFinishedPayload.CODEC)
            .consumerMainThread { msg, _ ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                    Runnable {
                        ClientCooldownTracker.onFinished(msg)
                    }
                }
            }
            .add()

        // S2C: кулдаун отменён
        CHANNEL.messageBuilder(
            CooldownCanceledPayload::class.java,
            ID_COOLDOWN_CANCELED
        )
            .codec(CooldownCanceledPayload.CODEC)
            .consumerMainThread { msg, _ ->
                DistExecutor.unsafeRunWhenOn(Dist.CLIENT) {
                    Runnable {
                        ClientCooldownTracker.onCanceled(msg)
                    }
                }
            }
            .add()
    }

    // CLIENT -> SERVER
    override fun sendActivateSkill() {
        CHANNEL.send(
            SkillActivationPayload,
            PacketDistributor.SERVER.noArg()
        )
    }

    // SERVER -> CLIENT

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
