package ua.com.xela.soulgem_arsenal.network.payloads

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey


data class CooldownCanceledPayload(
    val key: NetworkCooldownKey
): CustomPacketPayload {

    companion object {
        val ID: ResourceLocation = Common.createResourceLocation("cooldown_canceled")

        val TYPE: CustomPacketPayload.Type<CooldownCanceledPayload> =
            CustomPacketPayload.Type(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, CooldownCanceledPayload> =
            StreamCodec.of(
                { buf, payload ->
                    buf.writeUUID(payload.key.playerId)
                    buf.writeEnum(payload.key.hand)
                    buf.writeResourceLocation(payload.key.skillId)

                    buf.writeNullable(payload.key.slotIndex) { buf, value ->
                        buf.writeVarInt(value)
                    }

                },
                { buf ->
                    val playerId = buf.readUUID()
                    val hand = buf.readEnum(InteractionHand::class.java)
                    val skillId = buf.readResourceLocation()


                    val slotIndex = buf.readNullable {
                        it.readVarInt()
                    }

                    CooldownCanceledPayload(
                        key = NetworkCooldownKey(
                            playerId = playerId,
                            hand = hand,
                            skillId = skillId,
                            slotIndex = slotIndex
                        ),
                    )
                }
            )
    }

    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload?> {
        return TYPE
    }

}