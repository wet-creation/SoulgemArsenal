package ua.com.xela.soulgem_arsenal.platform

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import ua.com.xela.soulgem_arsenal.network.packets.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.services.INetworkService
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

class FabricaNetworkService : INetworkService {
    override fun registerReceivers() {
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

    override fun sendActivateSkill() {
        ClientPlayNetworking.send(SkillActivationPayload)
    }
}