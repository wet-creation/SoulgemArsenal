package ua.com.xela.soulgem_arsenal.network.cooldown

import net.minecraft.network.FriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.network.protocol.common.custom.CustomPacketPayload
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import ua.com.xela.soulgem_arsenal.core.Common
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
                    buf.writeUUID(payload.key.playerId)
                    buf.writeEnum(payload.key.hand)
                    buf.writeResourceLocation(payload.key.skillId)
                    buf.writeVarInt(payload.totalSeconds)
                    buf.writeVarInt(payload.remainingSeconds)

                    buf.writeNullable(payload.key.slotIndex) { buf, value ->
                        buf.writeVarInt(value)
                    }
                },
                { buf ->
                    val playerId = buf.readUUID()
                    val hand = buf.readEnum(InteractionHand::class.java)
                    val skillId = buf.readResourceLocation()

                    val total = buf.readVarInt()
                    val remaining = buf.readVarInt()

                    val slotIndex = buf.readNullable {
                        it.readVarInt()
                    }

                    CooldownStartPayload(
                        key = NetworkCooldownKey(
                            playerId = playerId,
                            hand = hand,
                            skillId = skillId,
                            slotIndex = slotIndex
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
