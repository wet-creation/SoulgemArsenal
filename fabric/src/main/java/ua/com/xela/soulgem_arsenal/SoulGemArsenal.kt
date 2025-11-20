package ua.com.xela.soulgem_arsenal

import net.fabricmc.api.ModInitializer
import net.fabricmc.fabric.api.event.player.UseItemCallback
import net.minecraft.network.chat.Component
import net.minecraft.world.InteractionResultHolder
import net.minecraft.world.item.BowItem
import ua.com.xela.soulgem_arsenal.skills.armTeleportSkill
import ua.com.xela.soulgem_arsenal.skills.bow.telport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.getSkill
import ua.com.xela.soulgem_arsenal.skills.setSkill

object SoulGemArsenal : ModInitializer {

    override fun onInitialize() {
        Common.init()

        UseItemCallback.EVENT.register(UseItemCallback { player, world, _ ->
            val stack = player.mainHandItem ?: player.offhandItem

            // клиент нам не нужен
            if (world.isClientSide) {
                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            // нужен Shift
            if (!player.isCrouching) {
                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            // интересует только лук
            if (stack.item !is BowItem) {
                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            // если скилла ещё нет — повесим его (как мы делали на Forge для теста)
            val teleportSkill =
                stack.getSkill<TeleportSkill>() ?: return@UseItemCallback InteractionResultHolder.pass(stack)
            if (teleportSkill.enabled.not() || teleportSkill.charges <= 0) {
                stack.setSkill(
                    TeleportSkill(
                        enabled = true,
                        charges = 3,
                        armed = false
                    )
                )

                player.sendSystemMessage(
                    Component.literal("Teleport skill installed on this bow")
                )

                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            // если уже есть — армируем следующий выстрел
            val armed = stack.armTeleportSkill()
            if (!armed) {
                player.sendSystemMessage(
                    Component.literal("No charges left")
                )
                return@UseItemCallback InteractionResultHolder.pass(stack)
            }

            player.sendSystemMessage(
                Component.literal("Next shot will teleport you")
            )

            InteractionResultHolder.pass(stack)
        })
    }
}