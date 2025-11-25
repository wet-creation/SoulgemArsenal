package ua.com.xela.soulgem_arsenal.client

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import ua.com.xela.soulgem_arsenal.client.input.KeyBinds
import ua.com.xela.soulgem_arsenal.platform.api.Services

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
