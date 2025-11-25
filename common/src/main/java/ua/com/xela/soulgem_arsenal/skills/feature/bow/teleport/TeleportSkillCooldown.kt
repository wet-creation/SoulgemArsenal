package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.base.ItemRef
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager
import ua.com.xela.soulgem_arsenal.skills.base.getSkill
import ua.com.xela.soulgem_arsenal.skills.base.setSkill

object TeleportSkillCooldown {


    fun startForStack(
        player: ServerPlayer,
        hand: InteractionHand,
        stack: ItemStack,
        skill: TeleportSkill
    ) {
        val maxCharges = 3
        if (skill.charges >= maxCharges) return

        val chargesToRestore = maxCharges - skill.charges
        val secondsPerCharge = skill.maxCooldown

        val itemRef = ItemRef(hand = hand)

        SkillCooldownManager.startCooldown(
            player = player,
            itemRef = itemRef,
            skillKey = TeleportSkill.KEY,
            chargesToRestore = chargesToRestore,
            secondsPerCharge = secondsPerCharge
        ) { _, _, _, _ ->

            val current = stack.getSkill<TeleportSkill>() ?: return@startCooldown
            val updated = current.copy(
                charges = (current.charges + 1).coerceAtMost(maxCharges),
                cooldown = 0
            )
            stack.setSkill(updated)
        }
    }
}
