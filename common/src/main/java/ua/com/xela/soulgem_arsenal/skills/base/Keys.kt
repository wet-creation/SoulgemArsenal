package ua.com.xela.soulgem_arsenal.skills.base

import com.mojang.serialization.Codec
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import java.util.*


@JvmInline
value class ItemInstanceId(val value: UUID) {
    companion object {
        val CODEC: Codec<ItemInstanceId> =
            UUIDUtil.CODEC.xmap(
                { uuid -> ItemInstanceId(uuid) },
                { id -> id.value }
            )
        val STREAM_CODEC: StreamCodec<RegistryFriendlyByteBuf, ItemInstanceId> =
            StreamCodec.of(
                { buf, value ->
                    buf.writeUUID(value.value)
                },
                { buf ->
                    val value = buf.readUUID()
                    ItemInstanceId(value)
                }
            )
    }
}



data class SkillKey(
    val id: ResourceLocation
)

data class ItemRef(
    val hand: InteractionHand,
    val slotIndex: Int? = null
)

data class CooldownKey(
    val playerId: UUID,
    val item: ItemRef,
    val itemId: ItemInstanceId,
    val skill: SkillKey
) {
    fun toNetworkCooldownKey() = NetworkCooldownKey(
        playerId = playerId,
        hand = item.hand,
        skillId = skill.id,
        itemId = itemId,
        slotIndex = item.slotIndex
    )
}

data class NetworkCooldownKey(
    val playerId: UUID,
    val hand: InteractionHand,
    val skillId: ResourceLocation,
    val itemId: ItemInstanceId,
    val slotIndex: Int?
) {
    fun toCooldownKey() = CooldownKey(
        playerId = playerId,
        item = ItemRef(
            hand = hand,
            slotIndex = slotIndex
        ),
        itemId = itemId,
        skill = SkillKey(skillId)
    )
}
