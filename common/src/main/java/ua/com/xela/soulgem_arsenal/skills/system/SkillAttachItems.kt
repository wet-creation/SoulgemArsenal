package ua.com.xela.soulgem_arsenal.skills.system

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry

object SkillAttachItems {
    val byItem: Map<Item, SkillEntry<out Skill>> = mapOf(
        ModItems.TeleportGem to SkillFactory.Teleport,
        ModItems.ArrowsHailGem to SkillFactory.ArrowsHail,
        )

    fun onSmithingResult(addition: ItemStack, result: ItemStack) {
        val entry = byItem[addition.item] ?: return
        SkillFactory.applySkillEntry(result, entry)
    }

}
