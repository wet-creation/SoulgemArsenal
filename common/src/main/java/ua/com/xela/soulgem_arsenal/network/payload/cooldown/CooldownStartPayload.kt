package ua.com.xela.soulgem_arsenal.network.payload.cooldown

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

data class CooldownStartPayload(
    val key: NetworkCooldownKey,
    val totalSeconds: Int,
    val remainingSeconds: Int
) : CustomPacketPayload {

    companion object {
        val ID: ResourceLocation = Common.createResourceLocation("cooldown_start")

        val TYPE: CustomPacketPayload.Type<CooldownStartPayload> =
            CustomPacketPayload.Type(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, CooldownStartPayload> =
            StreamCodec.of(
                { buf, payload ->
                    buf.writeUUID(payload.key.itemId.value)
                    buf.writeResourceLocation(payload.key.skillId)
                    buf.writeVarInt(payload.totalSeconds)
                    buf.writeVarInt(payload.remainingSeconds)

                },
                { buf ->
                    val itemId = ItemInstanceId(buf.readUUID())
                    val skillId = buf.readResourceLocation()

                    val total = buf.readVarInt()
                    val remaining = buf.readVarInt()


                    CooldownStartPayload(
                        key = NetworkCooldownKey(
                            skillId = skillId,
                            itemId = itemId
                        ),
                        totalSeconds = total,
                        remainingSeconds = remaining
                    )
                }
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }
}
