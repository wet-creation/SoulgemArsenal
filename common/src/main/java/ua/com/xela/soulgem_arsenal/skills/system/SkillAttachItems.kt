package ua.com.xela.soulgem_arsenal.skills.system

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry
import ua.com.xela.soulgem_arsenal.skills.base.setSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill

object SkillAttachItems {
    val byItem: Map<Item, SkillEntry<out Skill>> = mapOf(
        ModItems.TeleportGem to SkillEntry(
            TeleportSkill::class
        ) {
            TeleportSkill(
                selected = true,
                armed = false,
                charges = 3,
                cooldown = 0,
                maxCooldown = 20
            )
        },
        ModItems.ArrowsHailGem to SkillEntry(
            ArrowsHailSkill::class
        ) {
            ArrowsHailSkill(
                selected = true,
                armed = false,
                charges = 1,
                cooldown = 0,
                maxCooldown = 20
            )
        },

    )

    fun onSmithingResult(addition: ItemStack, result: ItemStack) {
        val entry = byItem[addition.item] ?: return
        applySkillEntry(result, entry)
    }

    private fun <T : Skill> applySkillEntry(
        stack: ItemStack,
        entry: SkillEntry<T>
    ) {
        val skill = entry.factory()
        stack.setSkill(entry.clazz, skill)
    }
}
