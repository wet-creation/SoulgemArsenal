package ua.com.xela.soulgem_arsenal.client

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.registry.KeyBinds

object FabricClientHooks {
    fun init() {
        KeyBinds.register()

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK.sendActivateSkill()
            }
        }
    }
}
