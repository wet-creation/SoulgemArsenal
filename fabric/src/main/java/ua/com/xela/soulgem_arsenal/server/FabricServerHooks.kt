package ua.com.xela.soulgem_arsenal.server

import net.fabricmc.fabric.api.event.lifecycle.v1.ServerWorldEvents
import net.fabricmc.fabric.api.networking.v1.ServerPlayConnectionEvents
import ua.com.xela.soulgem_arsenal.network.server.CooldownStorage
import ua.com.xela.soulgem_arsenal.skills.system.SkillCooldownManager

object FabricServerHooks {

    fun init() {
        ServerWorldEvents.LOAD.register { _, world ->
           CooldownStorage.get(world)

        }
        ServerPlayConnectionEvents.JOIN.register { handler, _, _ ->
            val player = handler.player
            val level = player.serverLevel()

            SkillCooldownManager.resumeForPlayer(level, player)
        }



    }
}