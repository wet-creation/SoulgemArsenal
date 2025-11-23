package ua.com.xela.soulgem_arsenal


import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraftforge.network.ChannelBuilder
import net.minecraftforge.network.PacketDistributor
import net.minecraftforge.network.SimpleChannel
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.network.packets.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

object SoulgemNetworkingForge {
    private const val PROTOCOL_VERSION = 1
    private val CHANNEL_NAME = ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, "main")
    private val CHANNEL: SimpleChannel =
        ChannelBuilder
            .named(CHANNEL_NAME)
            .networkProtocolVersion(PROTOCOL_VERSION)
            .clientAcceptedVersions { _, protocol -> protocol == PROTOCOL_VERSION }
            .serverAcceptedVersions { _, protocol ->
                protocol == PROTOCOL_VERSION
            }
            .simpleChannel()


    fun register() {
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

    fun sendSkillActivate() {
        CHANNEL.send(SkillActivationPayload, PacketDistributor.SERVER.noArg())
    }
}