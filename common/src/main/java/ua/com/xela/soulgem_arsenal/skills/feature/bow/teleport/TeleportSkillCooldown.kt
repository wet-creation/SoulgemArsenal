package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.base.ItemInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.ItemRef
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import ua.com.xela.soulgem_arsenal.skills.base.getOrCreateInstanceId
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
        val itemId = stack.getOrCreateInstanceId()
        SkillCooldownManager.startCooldown(
            player = player,
            itemRef = itemRef,
            skillKey = TeleportSkill.KEY,
            itemId = itemId,
            chargesToRestore = chargesToRestore,
            secondsPerCharge = secondsPerCharge
        ) { player1: ServerPlayer, ref: ItemRef, id: ItemInstanceId, key: SkillKey, i: Int ->
            onCharge(stack)
        }
    }

    fun onCharge(stack: ItemStack) {
        val current = stack.getSkill<TeleportSkill>() ?: return
        val updated = current.copy(
            charges = (current.charges + 1).coerceAtMost(3),
            cooldown = 0
        )
        stack.setSkill(updated)
    }
}
