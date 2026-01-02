package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.skills.api.SkillCooldown
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object TeleportSkillCooldown: SkillCooldown<TeleportSkill> {

    override fun onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        charge: Int,
    ) {
        SkillCooldownManager.onCharge<TeleportSkill>(player, id) {
            it.copy(
                charges = (it.charges + charge).coerceAtMost(it.maxCharges)
            )
        }
    }
}
