package ua.com.xela.soulgem_arsenal.platform.client

import com.mojang.blaze3d.platform.Window
import net.minecraft.client.Minecraft
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.CooldownKey
import ua.com.xela.soulgem_arsenal.skills.base.getAllSkills
import ua.com.xela.soulgem_arsenal.skills.base.getInstanceIdOrNull
import kotlin.properties.Delegates

object ClientCooldownHud {
    private var width by Delegates.notNull<Int>()
    private var height by Delegates.notNull<Int>()
    private val mc: Minecraft
        get() = Minecraft.getInstance()

    private const val hotbarWidth = 182
    private val hotbarX: Int
        get() = (width - hotbarWidth) / 2
    private val hotbarY: Int
        get() = height - 22

    private const val bgSize = 22          // квадрат фона под иконку
    private const val iconSize = 16
    private const val padding = 3          // отступы внутри квадрата
    private const val marginRight = 4      // отступ от края хотбара

    private val bgX: Int
        get() = hotbarX + hotbarWidth + marginRight
    private val bgY: Int
        get() = hotbarY - 2

    private val iconX: Int
        get() = bgX + padding
    private val iconY: Int
        get() = bgY + padding

    fun render(graphics: GuiGraphics, window: Window) {
        val player = mc.player ?: return
        width = window.guiScaledWidth
        height = window.guiScaledHeight

        val stack = listOf(player.mainHandItem, player.offhandItem)
            .firstOrNull { !it.isEmpty } ?: return

        val itemId = stack.getInstanceIdOrNull() ?: return
        val skill = stack.getAllSkills().firstOrNull { it.selected } ?: return
        val key = CooldownKey(itemId, skill.key)
        val (charges, maxCharges) = skill.charges to skill.maxCharges


        graphics.drawSkillIcon(skill, stack)
        graphics.drawChargesText(charges, maxCharges)

        if (!ClientCooldownTracker.hasCooldown(key)) return

        val progress = ClientCooldownTracker.getProgress(key).coerceIn(0f, 1f)



        val pose = graphics.pose()
        pose.pushPose()

        // Фон под иконкой — показываем, когда скилл не фулл
        if (progress < 1f || charges < maxCharges) {
            graphics.fill(
                bgX,
                bgY,
                bgX + bgSize,
                bgY + bgSize,
                0xAA000000.toInt() // полупрозрачный черный фон
            )
        }


        // Оверлей «оставшегося» кулдауна поверх иконки
        // cooldownRemaining = часть, которая еще не восстановилась
        val cooldownRemaining = (1f - progress).coerceIn(0f, 1f)
        if (cooldownRemaining > 0f) {
            val coverHeight = (iconSize * cooldownRemaining).toInt()
            // затемняем нижнюю часть иконки на величину кулдауна
            graphics.fill(
                iconX,
                iconY + (iconSize - coverHeight),
                iconX + iconSize,
                iconY + iconSize,
                0xAA000000.toInt()
            )
        }

        pose.popPose()

    }


    private fun GuiGraphics.drawChargesText(charges: Int, maxCharges: Int) {
        val text = "$charges/$maxCharges"
        val textX = bgX + bgSize + 4
        val textY = bgY + (bgSize - mc.font.lineHeight) / 2

        drawString(mc.font, text, textX, textY, 0xFFFFFF, true)
    }

    private fun GuiGraphics.drawSkillIcon(skill: Skill, itemStack: ItemStack) {
        SkillGUI.getSkillGUIComponents(skill.key)?.let {
            blit(
                it.icon,
                iconX,
                iconY,
                0f,
                0f,
                16,
                16,
                16,
                16
            )
        } ?: run {
            renderItem(itemStack, iconX, iconY)
        }
    }

}