package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.api.EnvType
import net.fabricmc.fabric.api.command.v2.CommandRegistrationCallback
import net.fabricmc.loader.api.FabricLoader
import ua.com.xela.soulgem_arsenal.client.FabricClientHooks
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.network.FabricNetwork
import ua.com.xela.soulgem_arsenal.registry.CommandsRegister
import ua.com.xela.soulgem_arsenal.server.FabricServerHooks

object FabricMod : ModInitializer {

    override fun onInitialize() {
        Common.init()
        FabricNetwork.registerPayloads()
        registerCommands()
        if (FabricLoader.getInstance().environmentType == EnvType.CLIENT) {
            FabricClientHooks.init()
        }

        FabricServerHooks.init()
    }

    private fun registerCommands() {
        CommandRegistrationCallback.EVENT.register { dispatcher, _, _ ->
            CommandsRegister.register(dispatcher)
        }
    }
}
