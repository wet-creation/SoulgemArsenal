package ua.com.xela.soulgem_arsenal.network

import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownTickPayload

object FabricNetwork {

    fun registerPayloads() {
        PayloadTypeRegistry.playS2C().register(
            CooldownStartPayload.TYPE,
            CooldownStartPayload.CODEC
        )
        PayloadTypeRegistry.playS2C().register(
            CooldownCanceledPayload.TYPE,
            CooldownCanceledPayload.CODEC
        )

        PayloadTypeRegistry.playS2C().register(
            CooldownChargeRestoredPayload.TYPE,
            CooldownChargeRestoredPayload.CODEC
        )

        PayloadTypeRegistry.playS2C().register(
            CooldownFinishedPayload.TYPE,
            CooldownFinishedPayload.CODEC
        )

        PayloadTypeRegistry.playS2C().register(
            CooldownTickPayload.TYPE,
            CooldownTickPayload.CODEC
        )

        PayloadTypeRegistry.playC2S().register(
            SkillActivationPayload.TYPE,
            SkillActivationPayload.CODEC
        )

    }
}