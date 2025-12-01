package ua.com.xela.soulgem_arsenal.skills.base

import com.mojang.serialization.Codec
import net.minecraft.core.UUIDUtil
import net.minecraft.network.RegistryFriendlyByteBuf
import net.minecraft.network.codec.StreamCodec
import net.minecraft.resources.ResourceLocation
import net.minecraft.server.MinecraftServer
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.item.ItemEntity
import net.minecraft.world.item.ItemStack
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


        fun findStackById(
            server: MinecraftServer,
            id: ItemInstanceId
        ): ItemStack? {
            for (player in server.playerList.players) {
                findInPlayer(player, id)?.let { return it }
            }

            for (level in server.allLevels) {
                for (entity in level.allEntities) {
                    if (entity is ItemEntity) {
                        val stack = entity.item
                        if (!stack.isEmpty && stack.getInstanceIdOrNull() == id) {
                            return stack
                        }
                    }
                }
            }

            return null
        }

        private fun findInPlayer(player: ServerPlayer, id: ItemInstanceId): ItemStack? {
            listOf(player.mainHandItem, player.offhandItem).forEach { stack ->
                if (!stack.isEmpty && stack.getInstanceIdOrNull() == id) return stack
            }

            val inv = player.inventory
            for (slot in 0 until inv.containerSize) {
                val stack = inv.getItem(slot)
                if (!stack.isEmpty && stack.getInstanceIdOrNull() == id) return stack
            }

            return null
        }


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
