package ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.getOrCreateInstanceId
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object ArrowHailSkillCooldown {
    fun startForStack(
        player: ServerPlayer,
        stack: ItemStack,
        skill: ArrowsHailSkill
    ) {
        if (skill.charges >= skill.maxCharges) return

        val chargesToRestore = skill.maxCharges - skill.charges
        val secondsPerCharge = skill.maxCooldown

        val itemId = stack.getOrCreateInstanceId()
        SkillCooldownManager.startCooldown(
            player = player,
            skillKey = ArrowsHailSkill.KEY,
            itemId = itemId,
            chargesToRestore = chargesToRestore,
            secondsPerCharge = secondsPerCharge
        )
    }

    fun onCharge(
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
