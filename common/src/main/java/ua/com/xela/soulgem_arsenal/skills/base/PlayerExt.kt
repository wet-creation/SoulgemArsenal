package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

fun Player.getHand(stack: ItemStack): InteractionHand {
    return if (mainHandItem === stack) InteractionHand.MAIN_HAND
    else if (offhandItem === stack) InteractionHand.OFF_HAND
    else error("item is not REEEEEAAAAAAL")
}