package ua.com.xela.soulgem_arsenal.platform

import net.fabricmc.loader.api.FabricLoader
import ua.com.xela.soulgem_arsenal.platform.services.IPlatformHelper

class FabricPlatformHelper : IPlatformHelper {
    override val platformName: String
        get() = "Fabric"

    override fun isModLoaded(modId: String?): Boolean {
        return FabricLoader.getInstance().isModLoaded(modId)
    }

    override val isDevelopmentEnvironment: Boolean
        get() = FabricLoader.getInstance().isDevelopmentEnvironment
}