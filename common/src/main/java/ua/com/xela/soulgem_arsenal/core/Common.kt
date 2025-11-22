package ua.com.xela.soulgem_arsenal.core

import ua.com.xela.soulgem_arsenal.core.Constants.LOG
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.registry.ModItems
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.base.SkillBehaviorRegistry
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkillBehavior


object Common {
    fun init() {
        LOG.debug(
            "Mod initialized on {}!",
            Services.PLATFORM.platformName,
        )
        ModItems.register()
        SkillComponents.register()
        registerSkillBehaviors()
    }

    fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
    }
}
