package ua.com.xela.soulgem_arsenal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.AbstractArrow
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.Constants.LOG
import ua.com.xela.soulgem_arsenal.registries.SkillComponents
import ua.com.xela.soulgem_arsenal.registries.ModItems
import ua.com.xela.soulgem_arsenal.skills.base.SkillBehaviorRegistry
import ua.com.xela.soulgem_arsenal.skills.bow.telport.TeleportSkillBehavior
import kotlin.time.Duration.Companion.seconds


object Common {
    fun init() {
        LOG.debug(
            "Hello from Common init on {}! we are currently in a {} environment!",
            Services.PLATFORM.platformName,
            Services.PLATFORM.environmentName
        )
        ModItems.register()
        SkillComponents.register()
        registerSkillBehaviors()
        CoroutineScope(Dispatchers.Default).launch {
            LOG.info("Before delay")
            delay(10.seconds)
            LOG.info("After 5 seconds {}", SkillComponents.Teleport.get())
        }
    }

    fun registerSkillBehaviors() {
        SkillBehaviorRegistry.register(TeleportSkillBehavior)
    }
}
