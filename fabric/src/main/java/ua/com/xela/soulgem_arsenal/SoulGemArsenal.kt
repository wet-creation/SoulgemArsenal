package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.platform.Services
import ua.com.xela.soulgem_arsenal.registry.KeyBinds

object SoulGemArsenal : ModInitializer {

    override fun onInitialize() {
        Common.init()

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK.sendActivateSkill()
            }
        }




    }
}