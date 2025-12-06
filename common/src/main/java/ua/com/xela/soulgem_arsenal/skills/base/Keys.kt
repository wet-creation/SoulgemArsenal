package ua.com.xela.soulgem_arsenal.skills.base

import com.mojang.serialization.Codec
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
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

data class CooldownKey(
    val itemId: ItemInstanceId,
    val skill: SkillKey
) {
    fun toNetworkCooldownKey() = NetworkCooldownKey(
        skillId = skill.id,
        itemId = itemId,
    )
}

data class NetworkCooldownKey(
    val skillId: ResourceLocation,
    val itemId: ItemInstanceId,
) {
    fun toCooldownKey() = CooldownKey(
        itemId = itemId,
        skill = SkillKey(skillId)
    )
}
