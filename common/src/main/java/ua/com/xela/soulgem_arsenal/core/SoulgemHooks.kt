package ua.com.xela.soulgem_arsenal.core

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object SoulgemHooks {

    fun onItemPickedFromGround(player: ServerPlayer, stack: ItemStack) {
        SkillCooldownManager.subscribePlayer(player, stack)
    }

    fun onItemPutIntoInventory(player: ServerPlayer, stack: ItemStack) {
       SkillCooldownManager.subscribePlayer(player, stack)

    }

    fun onItemRemovedFromInventory(stack: ItemStack) {
        SkillCooldownManager.unsubscribePlayer( stack)

    }

    fun onItemDropped(stack: ItemStack) {
        SkillCooldownManager.unsubscribePlayer( stack)

    }
}
