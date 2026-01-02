package ua.com.xela.soulgem_arsenal.skills.api

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.getOrCreateInstanceId
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

interface SkillCooldown<T: Skill> {

    fun startForStack(
        player: ServerPlayer,
        stack: ItemStack,
        skill: T
    ) {
        if (skill.charges >= skill.maxCharges) return

        val chargesToRestore = skill.maxCharges - skill.charges
        val secondsPerCharge = skill.maxCooldown
        val itemId = stack.getOrCreateInstanceId()
        SkillCooldownManager.startCooldown(
            player = player,
            skillKey = skill.key,
            itemId = itemId,
            chargesToRestore = chargesToRestore,
            secondsPerCharge = secondsPerCharge
        )
    }

    fun onCharge(
        player: ServerPlayer,
        id: ItemInstanceId,
        charge: Int,
    )
}