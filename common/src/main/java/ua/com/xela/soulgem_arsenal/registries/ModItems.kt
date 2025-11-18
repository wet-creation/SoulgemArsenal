package ua.com.xela.soulgem_arsenal.registries

import net.minecraft.world.item.Item
import net.minecraft.world.item.Rarity
import ua.com.xela.soulgem_arsenal.platform.Services

object ModItems {

    fun register() {
        Services.PLATFORM.registerItem(
            id = "teleport_bow_skill",
            item = {
                Item(
                    Item.Properties()
                        .stacksTo(1)
                        .rarity(Rarity.RARE)
                )
            }
        )
    }
}