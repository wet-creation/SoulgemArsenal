package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.loader.api.FabricLoader
import ua.com.xela.soulgem_arsenal.client.FabricClientHooks
import ua.com.xela.soulgem_arsenal.core.Common

object FabricMod : ModInitializer {

    override fun onInitialize() {
        Common.init()

        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            FabricClientHooks.init()
        }
    }
}
