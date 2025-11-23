package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.resources.ResourceLocation
import net.minecraft.world.InteractionHand
import java.util.UUID

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
    val skill: SkillKey
) {
    fun toNetworkCooldownKey() = NetworkCooldownKey(
        playerId = playerId,
        hand = item.hand,
        skillId = skill.id,
        slotIndex = item.slotIndex
    )
}

data class NetworkCooldownKey(
    val playerId: UUID,
    val hand: InteractionHand,
    val skillId: ResourceLocation,
    val slotIndex: Int?
) {
    fun toCooldownKey() = CooldownKey(
        playerId = playerId,
        item = ItemRef(
            hand = hand,
            slotIndex = slotIndex
        ),
        skill = SkillKey(skillId)
    )
}
