package ua.com.xela.soulgem_arsenal.network.payload.cooldown

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey


data class CooldownFinishedPayload(
    val key: NetworkCooldownKey
): CustomPacketPayload {

    companion object {
        val ID: ResourceLocation = Common.createResourceLocation("cooldown_finished")

        val TYPE: CustomPacketPayload.Type<CooldownFinishedPayload> =
            CustomPacketPayload.Type(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, CooldownFinishedPayload> =
            StreamCodec.of(
                { buf, payload ->
                    buf.writeUUID(payload.key.itemId.value)
                    buf.writeResourceLocation(payload.key.skillId)


                },
                { buf ->
                    val itemId = ItemInstanceId(buf.readUUID())
                    val skillId = buf.readResourceLocation()



                    CooldownFinishedPayload(
                        key = NetworkCooldownKey(
                            skillId = skillId,
                            itemId = itemId,
                        ),
                    )
                }
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

}
