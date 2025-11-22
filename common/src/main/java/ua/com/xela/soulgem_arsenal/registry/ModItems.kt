package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.world.item.Item
import net.minecraft.world.item.ItemStack
import net.minecraft.world.item.Rarity
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.setSkill

object ModItems {

    lateinit var TeleportGem: Item
    lateinit var SkillTemplate: Item
    fun register() {
        Services.PLATFORM.registerItem(
            id = "teleport_gem",
            item = {
                TeleportGem = Item(
                    Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.RARE)
                )
                TeleportGem
            }
        )
        Services.PLATFORM.registerItem(
            id = "skill_template",
            item = {
                SkillTemplate = Item(
                    Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.RARE)
                )
                SkillTemplate
            }
        )

    }
}

object SkillAttachItems {
    val byItem: Map<Item, SkillEntry<out Skill>> = mapOf(
        ModItems.TeleportGem to SkillEntry(
            TeleportSkill::class
        ) {
            TeleportSkill(
                enabled = true,
                armed = false,
                charges = 3,
                cooldown = 20
            )
        }
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