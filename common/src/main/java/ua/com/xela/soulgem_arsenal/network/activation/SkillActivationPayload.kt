package ua.com.xela.soulgem_arsenal.network.activation

import io.netty.buffer.ByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common

object SkillActivationPayload : CustomPacketPayload {

    val ID: ResourceLocation = Common.createResourceLocation("skill_activate")

    val TYPE: CustomPacketPayload.Type<SkillActivationPayload> =
        CustomPacketPayload.Type(ID)

    // PacketCodec is zero-length because payload has no data
    val CODEC: StreamCodec<ByteBuf, SkillActivationPayload> =
        StreamCodec.unit(SkillActivationPayload)

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
