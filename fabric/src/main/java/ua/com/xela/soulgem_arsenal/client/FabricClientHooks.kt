package ua.com.xela.soulgem_arsenal.client

import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking
import ua.com.xela.soulgem_arsenal.network.client.ClientCooldownTracker
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownCanceledPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownChargeRestoredPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownFinishedPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownStartPayload
import ua.com.xela.soulgem_arsenal.network.payload.cooldown.CooldownTickPayload
import ua.com.xela.soulgem_arsenal.platform.api.Services
import ua.com.xela.soulgem_arsenal.registry.KeyBinds

object FabricClientHooks {
    fun init() {
        KeyBinds.register()
        registerReceivers()
    }

    private fun sendActiveSkill() {
        ClientTickEvents.END_CLIENT_TICK.register { _ ->
            if (KeyBinds.ActivateSkill.consumeClick()) {
                Services.NETWORK_CLIENT.sendActivateSkill()
            }
        }
    }

    private fun cooldownStart() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownStartPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onStart(payload)
            }
        }

    }

    private fun cooldownCanceled() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownCanceledPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onCanceled(payload)
            }
        }
    }

    private fun cooldownChargeRestored() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownChargeRestoredPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onChargeRestored(payload)
            }
        }
    }

    private fun cooldownFinished() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownFinishedPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onFinished(payload)
            }
        }
    }

    private fun cooldownTick() {
        ClientPlayNetworking.registerGlobalReceiver(CooldownTickPayload.TYPE) { payload, context ->
            context.client().execute {
                ClientCooldownTracker.onTick(payload)
            }
        }
    }


   private fun registerReceivers() {
        cooldownCanceled()
        cooldownStart()
        cooldownChargeRestored()
        cooldownFinished()
        sendActiveSkill()
        cooldownTick()
    }
}
