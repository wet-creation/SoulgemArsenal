package ua.com.xela.soulgem_arsenal.platform

import net.minecraftforge.fml.ModList
import net.minecraftforge.fml.loading.FMLLoader
import ua.com.xela.soulgem_arsenal.platform.services.IPlatformHelper

class ForgePlatformHelper: IPlatformHelper {
    override val platformName: String
        get() = "Forge"

    override fun isModLoaded(modId: String?): Boolean {
       return ModList.get().isLoaded(modId)
    }

    override val isDevelopmentEnvironment: Boolean
        get() = !FMLLoader.isProduction()
}