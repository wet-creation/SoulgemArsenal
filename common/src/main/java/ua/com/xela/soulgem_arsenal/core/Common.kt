package ua.com.xela.soulgem_arsenal.core

import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.registry.ItemIdComponents
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowHailSkillBehavior
import ua.com.xela.soulgem_arsenal.skills.system.SkillBehaviorRegistry
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillBehavior


object Common {
   private val keys: MutableMap<String, Int> = mutableMapOf()

    fun init() {
        ModItems.register()
        SkillComponents.register()
        ItemIdComponents.register()
        registerSkillBehaviors()

        Services.NETWORK.registerReceivers()

    }

    fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
        SkillBehaviorRegistry.register(ArrowHailSkillBehavior)
    }

    fun createResourceLocation(name: String): ResourceLocation {

        val v = keys[name]
        if (v != null) error("This resource $name is already existed")
        keys[name] = 0
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)
    }
}
