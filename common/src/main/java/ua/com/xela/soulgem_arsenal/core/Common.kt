package ua.com.xela.soulgem_arsenal.core

import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.registry.KeyBinds
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.base.SkillBehaviorRegistry
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillBehavior


object Common {
    fun init() {
        ModItems.register()
        SkillComponents.register()
        registerSkillBehaviors()
        KeyBinds.registry()

        Services.NETWORK.registerReceivers()

    }

    fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
    }

    fun createResourceLocation(name: String): ResourceLocation {
        return ResourceLocation.fromNamespaceAndPath(Constants.MOD_ID, name)
    }
}
