package ua.com.xela.soulgem_arsenal.server

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking
import ua.com.xela.soulgem_arsenal.network.payload.activation.SkillActivationPayload
import ua.com.xela.soulgem_arsenal.platform.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager
import ua.com.xela.soulgem_arsenal.skills.system.SkillEvents

object FabricServerHooks {

    fun init() {
        onWorldLoad()
        onPlayerEvents()
        skillActivate()
    }


    private fun onWorldLoad() {
        ServerWorldEvents.LOAD.register { _, world ->
            CooldownStorage.get(world)

        }
    }

    private fun onPlayerEvents() {
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            val player = handler.player
            val level = player.serverLevel()

            SkillCooldownManager.resumeForPlayer(level, player)
        }

        ServerPlayConnectionEvents.DISCONNECT.register { handler, _ ->
            SkillCooldownManager.onPlayerDisconnect(handler.player.serverLevel(), handler.player)
        }
    }

    private fun skillActivate() {
        ServerPlayNetworking.registerGlobalReceiver(SkillActivationPayload.TYPE) { _, context ->
            context.server().execute {
                val player = context.player() ?: return@execute
                SkillEvents.onSkillActivate(
                    player = player,
                )
            }
        }
    }
}