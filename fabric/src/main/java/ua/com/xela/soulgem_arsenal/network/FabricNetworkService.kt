package ua.com.xela.soulgem_arsenal.network

import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import net.minecraft.server.level.ServerPlayer
import ua.com.xela.soulgem_arsenal.client.ClientCooldownTracker
import ua.com.xela.soulgem_arsenal.network.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.cooldown.CooldownTickPayload
import ua.com.xela.soulgem_arsenal.network.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.api.services.INetworkService
import ua.com.xela.soulgem_arsenal.skills.base.NetworkCooldownKey
import ua.com.xela.soulgem_arsenal.skills.system.SkillEvents

class FabricNetworkService : INetworkService {
    override fun registerReceivers() {
        cooldownCanceled()
        cooldownStart()
        cooldownChargeRestored()
        cooldownFinished()
        cooldownTick()
        skillActivate()
    }
//CLIENT TO SERVER
    fun skillActivate() {
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
//SERVER TO CLIENT
    private fun cooldownStart() {
        PayloadTypeRegistry.playS2C().register(
            CooldownStartPayload.TYPE,
            CooldownStartPayload.CODEC
        )
        ClientPlayNetworking.registerGlobalReceiver(CooldownStartPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onStart(payload)
            }
        }

    }

    private fun cooldownCanceled() {
        PayloadTypeRegistry.playS2C().register(
            CooldownCanceledPayload.TYPE,
            CooldownCanceledPayload.CODEC
        )
        ClientPlayNetworking.registerGlobalReceiver(CooldownCanceledPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onCanceled(payload)
            }
        }
    }

    private fun cooldownChargeRestored() {
        PayloadTypeRegistry.playS2C().register(
            CooldownChargeRestoredPayload.TYPE,
            CooldownChargeRestoredPayload.CODEC
        )

        ClientPlayNetworking.registerGlobalReceiver(CooldownChargeRestoredPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onChargeRestored(payload)
            }
        }
    }

    private fun cooldownFinished() {
        PayloadTypeRegistry.playS2C().register(
            CooldownFinishedPayload.TYPE,
            CooldownFinishedPayload.CODEC
        )

        ClientPlayNetworking.registerGlobalReceiver(CooldownFinishedPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onFinished(payload)
            }
        }
    }

    private fun cooldownTick() {
        PayloadTypeRegistry.playS2C().register(
            CooldownTickPayload.TYPE,
            CooldownTickPayload.CODEC
        )

        ClientPlayNetworking.registerGlobalReceiver(CooldownTickPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onTick(payload)
            }
        }
    }

    override fun sendActivateSkill() {
        ClientPlayNetworking.send(SkillActivationPayload)
    }

    override fun broadcastCooldownStart(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownStartPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            )
        )
    }

    override fun broadcastCooldownTick(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        totalSeconds: Int,
        remainingSeconds: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownTickPayload(
                key = key,
                totalSeconds = totalSeconds,
                remainingSeconds = remainingSeconds
            )
        )
    }

    override fun broadcastCooldownChargeRestored(
        player: ServerPlayer,
        key: NetworkCooldownKey,
        restoredChargeIndex: Int
    ) {
        ServerPlayNetworking.send(
            player, CooldownChargeRestoredPayload(
                key = key,
                chargeIndex = restoredChargeIndex,
            )
        )
    }

    override fun broadcastCooldownFinished(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        ServerPlayNetworking.send(
            player, CooldownFinishedPayload(
                key = key,
            )
        )
    }

    override fun broadcastCooldownCanceled(
        player: ServerPlayer,
        key: NetworkCooldownKey
    ) {
        ServerPlayNetworking.send(
            player, CooldownCanceledPayload(
                key = key,
            )
        )
    }
}
