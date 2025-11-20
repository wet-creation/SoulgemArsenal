package ua.com.xela.soulgem_arsenal.interations

import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResult
import net.minecraft.world.item.BowItem
import net.minecraftforge.event.entity.player.PlayerInteractEvent
import net.minecraftforge.eventbus.api.SubscribeEvent
import net.minecraftforge.fml.common.Mod
import ua.com.xela.soulgem_arsenal.Constants
import ua.com.xela.soulgem_arsenal.skills.armTeleportSkill
import ua.com.xela.soulgem_arsenal.skills.bow.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.hasTeleportSkill
import ua.com.xela.soulgem_arsenal.skills.setSkill

@Mod.EventBusSubscriber(modid = Constants.MOD_ID, bus = Mod.EventBusSubscriber.Bus.FORGE)
object ForgeInteractionEvents {

    @SubscribeEvent
    fun onRightClickItem(event: PlayerInteractEvent.RightClickItem) {
        val player = event.entity
        val level = event.level
        val stack = event.itemStack

        // только серверная сторона
        if (level.isClientSide) return

        // нужно зажатое приседание (Shift)
        if (!player.isShiftKeyDown) return

        // нас интересует только лук
        if (stack.item !is BowItem) return

        // у лука должен быть наш скилл
        if (!stack.hasTeleportSkill()) {
            // временно: навесим скилл прямо тут, для тестов
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

        // пробуем "армировать" следующий выстрел
        val armed = stack.armTeleportSkill()
        if (!armed) return

        // можно кинуть фидбек игроку
        player.displayClientMessage(
            Component.literal("Teleport shot armed"),
            true
        )

    }
}
