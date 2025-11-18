package ua.com.xela.soulgem_arsenal

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.Constants.LOG
import ua.com.xela.soulgem_arsenal.registries.SkillComponents
import ua.com.xela.soulgem_arsenal.registries.ModItems
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
        CoroutineScope(Dispatchers.Default).launch {
            LOG.info("Before delay")
            delay(10.seconds)
            LOG.info("After 5 seconds {}", SkillComponents.Teleport.get() )
        }
    }
}