package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.network.packets.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.registry.KeyBinds

object SoulGemArsenal : ModInitializer {

    override fun onInitialize() {
        Common.init()

        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KeyBinds.ActivateSkill.consumeClick()) {
                SoulgemNetworkingFabric.sendSkillActivate()
            }
        }



        SoulgemNetworkingFabric.registerReceivers()

    }
}