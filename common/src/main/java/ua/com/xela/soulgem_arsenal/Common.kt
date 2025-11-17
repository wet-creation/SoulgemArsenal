package ua.com.xela.soulgem_arsenal

import net.minecraft.core.registries.BuiltInRegistries
import net.minecraft.world.item.Items
import ua.com.xela.soulgem_arsenal.platform.Services

object Common {

    fun init() {
        Constants.LOG.info(
            "Hello from Common init on {}! we are currently in a {} environment!",
            Services.PLATFORM.platformName,
            Services.PLATFORM.environmentName
        )
        Constants.LOG.info("The ID for diamonds is {}", BuiltInRegistries.ITEM.getKey(Items.DIAMOND))

        // It is common for all supported loaders to provide a similar feature that can not be used directly in the
        // common code. A popular way to get around this is using Java's built-in service loader feature to create
        // your own abstraction layer. You can learn more about this in our provided services class. In this example
        // we have an interface in the common code and use a loader specific implementation to delegate our call to
        // the platform specific approach.
        if (Services.PLATFORM.isModLoaded(Constants.MOD_ID)) {
            Constants.LOG.info("Hello to ${Constants.MOD_NAME}")
        }
    }
}