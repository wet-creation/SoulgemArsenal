package ua.com.xela.soulgem_arsenal.skills.feature.sword.strength

import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.skills.api.SkillCooldown
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object StrengthSkillCooldown: SkillCooldown<StrengthSkill> {

    override fun onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        charge: Int
    ) {
        SkillCooldownManager.onCharge<StrengthSkill>(player, id) {
            it.copy(
                charges = (it.charges + charge).coerceAtMost(it.maxCharges)
            )
        }
    }
}