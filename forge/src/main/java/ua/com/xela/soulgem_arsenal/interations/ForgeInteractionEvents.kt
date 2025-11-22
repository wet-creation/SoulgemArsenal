package ua.com.xela.soulgem_arsenal.interations

import net.minecraft.server.level.ServerPlayer
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ForgeInteractionEvents {

    @SubscribeEvent
    fun onRightClick(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity as? ServerPlayer ?: return
        val stack = event.itemStack
        val isSneaking = player.isShiftKeyDown

        SkillEvents.onSkillActivate(
            player = player,
            stack = stack,
            hand = event.hand,
            isSneaking = isSneaking
        )

    }
}
