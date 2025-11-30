package ua.com.xela.soulgem_arsenal.core

import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.registry.ItemIdComponents
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowHailSkillBehavior
import ua.com.xela.soulgem_arsenal.skills.system.SkillBehaviorRegistry
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillBehavior


object Common {
   private val keys: MutableSet<String> = mutableSetOf()

    fun init() {
        ModItems.register()
        SkillComponents.register()
        ItemIdComponents.register()
        registerSkillBehaviors()
    }

    fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
        SkillBehaviorRegistry.register(ArrowHailSkillBehavior)
    }

    fun createResourceLocation(name: String): ResourceLocation {
        check(keys.add(name)) {
            "Resource $name is already existed"
        }
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)
    }
}
