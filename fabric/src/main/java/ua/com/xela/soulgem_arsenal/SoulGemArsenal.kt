package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionResultHolder
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

object SoulGemArsenal : ModInitializer {

    override fun onInitialize() {
        Common.init()

        UseItemCallback.EVENT.register(UseItemCallback { player, world, hand ->
            val stack = player.mainHandItem ?: player.offhandItem

            if (world.isClientSide || player !is ServerPlayer) {
                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            SkillEvents.onSkillActivate(
                player,
                stack = stack,
                hand = hand,
                isSneaking = player.isCrouching,
            )

            return@UseItemCallback InteractionResultHolder.pass(stack)
            
        })
    }
}