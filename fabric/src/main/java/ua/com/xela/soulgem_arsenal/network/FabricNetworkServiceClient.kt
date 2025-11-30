package ua.com.xela.soulgem_arsenal.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceClient

class FabricNetworkServiceClient : INetworkServiceClient {

    override fun sendActivateSkill() {
        ClientPlayNetworking.send(SkillActivationPayload)
    }

}