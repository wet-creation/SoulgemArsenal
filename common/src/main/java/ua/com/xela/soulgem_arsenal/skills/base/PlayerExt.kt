package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.player.Player
import net.minecraft.world.item.ItemStack

fun Player.getHand(stack: ItemStack): InteractionHand {
    return if (mainHandItem === stack) InteractionHand.MAIN_HAND
    else if (offhandItem === stack) InteractionHand.OFF_HAND
    else error("item is not REEEEEAAAAAAL")
}


fun ServerPlayer.getAllInstanceId(): Set<ItemInstanceId> {
    val player = this
    return buildSet {
        listOf(player.mainHandItem, player.offhandItem).forEach { stack ->
            if (!stack.isEmpty) {
                stack.getInstanceIdOrNull()?.let {
                    add(it)
                }
            }
        }
        val inv = player.inventory
        for (slot in 0 until inv.containerSize) {
            val stack = inv.getItem(slot)
            if (!stack.isEmpty) {
                stack.getInstanceIdOrNull()?.let {
                    add(it)
                }
            }
        }
    }


}

fun ServerPlayer.ownsInstanceId(id: ItemInstanceId): Boolean = findInPlayer(id) != null

fun ServerPlayer.findInPlayer(id: ItemInstanceId): ItemStack? {

    listOf(mainHandItem, offhandItem).forEach { stack ->
        if (!stack.isEmpty && stack.getInstanceIdOrNull() == id) return stack
    }

    val inv = inventory
    for (slot in 0 until inv.containerSize) {
        val stack = inv.getItem(slot)
        if (!stack.isEmpty && stack.getInstanceIdOrNull() == id) return stack
    }

    val carried = containerMenu.carried
    if (!carried.isEmpty && carried.getInstanceIdOrNull() == id) return carried

    return null
}

