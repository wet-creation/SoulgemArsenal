package ua.com.xela.soulgem_arsenal.interations

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BowItem
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import ua.com.xela.soulgem_arsenal.Constants
import ua.com.xela.soulgem_arsenal.skills.armTeleportSkill
import ua.com.xela.soulgem_arsenal.skills.bow.telport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.hasTeleportSkill
import ua.com.xela.soulgem_arsenal.skills.setSkill

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ForgeInteractionEvents {

    @SubscribeEvent
    fun onRightClickItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity
        val level = event.level
        val stack = event.itemStack

        if (level.isClientSide) return

        if (!player.isShiftKeyDown) return

        if (stack.item !is BowItem) return

        if (!stack.hasTeleportSkill()) {
            stack.setSkill(TeleportSkill(
                enabled = true,
                armed = true,
                charges = 3
            ))

            player.displayClientMessage(
                Component.literal("Teleport skill installed on this bow"),
                true
            )
            event.isCanceled = true
            event.cancellationResult = InteractionResult.SUCCESS
            return
        }

        val armed = stack.armTeleportSkill()
        if (!armed) return

        player.displayClientMessage(
            Component.literal("Teleport shot armed"),
            true
        )

    }
}
