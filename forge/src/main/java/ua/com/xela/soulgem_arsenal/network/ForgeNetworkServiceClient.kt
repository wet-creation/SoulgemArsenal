package ua.com.xela.soulgem_arsenal.network

import net.minecraftforge.network.PacketDistributor
import ua.com.xela.soulgem_arsenal.network.ForgeNetwork.CHANNEL
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkServiceClient

class ForgeNetworkServiceClient: INetworkServiceClient {
    override fun sendActivateSkill() {
        CHANNEL.send(
            SkillActivationPayload,
            PacketDistributor.SERVER.noArg()
        )
    }
}