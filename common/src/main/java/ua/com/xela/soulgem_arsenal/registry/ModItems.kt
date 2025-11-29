package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import ua.com.xela.soulgem_arsenal.platform.api.Services

object ModItems {

    lateinit var TeleportGem: Item
    lateinit var ArrowsHailGem: Item
    lateinit var SkillTemplate: Item
    fun register() {
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
            id = "arrows_hail_gem",
            item = {
                ArrowsHailGem = Item(
                    Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.RARE)
                )
                ArrowsHailGem
            }
        )


    }
}
