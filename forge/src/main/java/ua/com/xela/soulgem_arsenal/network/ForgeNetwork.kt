package ua.com.xela.soulgem_arsenal.network

import net.minecraft.network.codec.StreamCodec
import net.minecraftforge.api.distmarker.Dist
import net.minecraftforge.fml.DistExecutor
import net.minecraftforge.network.ChannelBuilder
import net.minecraftforge.network.SimpleChannel
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.platform.client.system.ClientCooldownTracker
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownTickPayload
import ua.com.xela.soulgem_arsenal.skills.system.SkillEvents

object ForgeNetwork {
    var id = 0
    const val PROTOCOL_VERSION = 1
    val CHANNEL_NAME = Common.createResourceLocation("main")

    val CHANNEL: SimpleChannel =
        ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
            .serverAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
            .simpleChannel()


    fun registerReceivers() {
        // C2S: активация скилла (клиент -> сервер)
        CHANNEL.messageBuilder(
            SkillActivationPayload::class.java,
            id++
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
            id++

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
            id++
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
            id++

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
            id++
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
            id++
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
}