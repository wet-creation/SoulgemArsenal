package ua.com.xela.soulgem_arsenal.network.payload.cooldown

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

data class CooldownChargeRestoredPayload(
    val key: NetworkCooldownKey,
    val chargeIndex: Int
) : CustomPacketPayload {

    companion object {
        val ID: ResourceLocation = Common.createResourceLocation("cooldown_charge_restore")

        val TYPE: CustomPacketPayload.Type<CooldownChargeRestoredPayload> =
            CustomPacketPayload.Type(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, CooldownChargeRestoredPayload> =
            StreamCodec.of(
                { buf, payload ->
                    buf.writeUUID(payload.key.itemId.value)
                    buf.writeResourceLocation(payload.key.skillId)

                    buf.writeVarInt(payload.chargeIndex)
                },
                { buf ->
                    val itemId = ItemInstanceId(buf.readUUID())
                    val skillId = buf.readResourceLocation()


                    val chargeIndex = buf.readVarInt()


                    CooldownChargeRestoredPayload(
                        key = NetworkCooldownKey(
                            skillId = skillId,
                            itemId = itemId,
                        ),
                        chargeIndex = chargeIndex
                    )
                }
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

}
