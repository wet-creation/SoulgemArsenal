package ua.com.xela.soulgem_arsenal.platform

import net.minecraft.network.codec.StreamCodec
import net.minecraftforge.network.ChannelBuilder
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.SimpleChannel
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.network.packets.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.services.INetworkService
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

class ForgeNetworkService : INetworkService {
    companion object {
        private const val PROTOCOL_VERSION = 1
        private val CHANNEL_NAME = Common.createResourceLocation("main")
        private val CHANNEL: SimpleChannel =
            ChannelBuilder
                .named(CHANNEL_NAME)
                .networkProtocolVersion(PROTOCOL_VERSION)
                .clientAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
                .serverAcceptedVersions { _, protocol ->
                    protocol == PROTOCOL_VERSION
                }
                .simpleChannel()
    }

    override fun registerReceivers() {
        CHANNEL.messageBuilder(
            SkillActivationPayload.javaClass, PROTOCOL_VERSION
        )
            .codec(StreamCodec.unit(SkillActivationPayload))
            .consumerMainThread { _, ctx ->
                val player = ctx.sender ?: return@consumerMainThread
                SkillEvents.onSkillActivate(player)
            }
            .add()
    }

    override fun sendActivateSkill() {
        CHANNEL.send(SkillActivationPayload, PacketDistributor.SERVER.noArg())
    }
}