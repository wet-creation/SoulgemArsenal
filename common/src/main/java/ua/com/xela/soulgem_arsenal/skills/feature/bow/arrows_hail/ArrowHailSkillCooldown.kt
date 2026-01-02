package ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail

import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.skills.api.SkillCooldown
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object ArrowHailSkillCooldown: SkillCooldown<ArrowsHailSkill> {

    override fun onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        charge: Int
    ) {
        SkillCooldownManager.onCharge<ArrowsHailSkill>(player, id) {
            it.copy(
                charges = (it.charges + charge).coerceAtMost(it.maxCharges)
            )
        }
    }
}
