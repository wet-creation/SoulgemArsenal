package ua.com.xela.soulgem_arsenal.network.payload.cooldown


import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey

data class CooldownTickPayload(
    val key: NetworkCooldownKey,
    val totalSeconds: Int,
    val remainingSeconds: Int
) : CustomPacketPayload {

    companion object {
        val ID: ResourceLocation = Common.createResourceLocation("cooldown_tick")
        val TYPE: CustomPacketPayload.Type<CooldownTickPayload> =
            CustomPacketPayload.Type(ID)

        val CODEC: StreamCodec<FriendlyByteBuf, CooldownTickPayload> =
            StreamCodec.of(
                // encoder
                { buf, payload ->
                    // NetworkCooldownKey
                    buf.writeUUID(payload.key.playerId)
                    buf.writeUUID(payload.key.itemId.value)
                    buf.writeEnum(payload.key.hand)
                    buf.writeResourceLocation(payload.key.skillId)
                    buf.writeNullable(payload.key.slotIndex) { buf, value ->
                        buf.writeVarInt(value)
                    }

                    // time data
                    buf.writeVarInt(payload.totalSeconds)
                    buf.writeVarInt(payload.remainingSeconds)
                },
                // decoder
                { buf ->
                    val playerId = buf.readUUID()
                    val itemId = ItemInstanceId(buf.readUUID())
                    val hand = buf.readEnum(InteractionHand::class.java)
                    val skillId = buf.readResourceLocation()
                    val slotIndex = buf.readNullable {
                        it.readVarInt()
                    }

                    val total = buf.readVarInt()
                    val remaining = buf.readVarInt()

                    CooldownTickPayload(
                        key = NetworkCooldownKey(
                            playerId = playerId,
                            hand = hand,
                            skillId = skillId,
                            slotIndex = slotIndex,
                            itemId = itemId
                        ),
                        totalSeconds = total,
                        remainingSeconds = remaining
                    )
                }
            )
    }

    // Обязательный метод CustomPacketPayload
    override fun type(): CustomPacketPayload.Type<out CustomPacketPayload> = TYPE
}
