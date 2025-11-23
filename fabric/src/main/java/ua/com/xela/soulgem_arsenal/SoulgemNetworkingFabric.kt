package ua.com.xela.soulgem_arsenal

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import ua.com.xela.soulgem_arsenal.network.packets.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

object SoulgemNetworkingFabric {

    fun registerReceivers() {
        PayloadTypeRegistry.playC2S().register(
            SkillActivationPayload.TYPE,
            SkillActivationPayload.CODEC
        )
        ServerPlayNetworking.registerGlobalReceiver(SkillActivationPayload.TYPE) { _, context ->
            context.server().execute {
                val player = context.player() ?: return@execute
                SkillEvents.onSkillActivate(
                    player = player,
                )
            }
        }
    }

    fun sendSkillActivate() {
        ClientPlayNetworking.send(SkillActivationPayload)
    }
}


