package ua.com.xela.soulgem_arsenal.platform.client

import net.minecraft.client.Minecraft
import net.minecraft.client.gui.Font
import net.minecraft.client.gui.GuiGraphics
import net.minecraft.client.gui.screens.inventory.tooltip.ClientTooltipComponent
import net.minecraft.network.chat.FormattedText
import net.minecraft.util.FormattedCharSequence
import kotlin.math.max

class SkillTooltipClientComponent(
    private val data: SkillTooltipComponent
) : ClientTooltipComponent {

    private var cachedWidth = 0
    private var cachedHeight = 0
    private var wrappedLines: List<FormattedCharSequence> = emptyList()

    override fun getWidth(font: Font): Int {
        val iconWidth = 16 + 4

        val maxWidth = 150

        val t = FormattedText.of(data.description.string)
        wrappedLines = font.split(t, maxWidth)

        val nameWidth = font.width(data.name)

        val descWidth = wrappedLines.maxOfOrNull { font.width(it) } ?: 0

        val totalWidth = iconWidth + max(nameWidth, descWidth)

        cachedWidth = totalWidth
        return totalWidth
    }

    override fun getHeight(): Int {
        val font = Minecraft.getInstance().font

        val nameHeight = font.lineHeight
        val descHeight = wrappedLines.size * font.lineHeight

        // Иконка = 16 px
        val contentHeight = max(16, nameHeight + descHeight)

        cachedHeight = contentHeight
        return contentHeight
    }

    override fun renderImage(font: Font, x: Int, y: Int, graphics: GuiGraphics) {
        val pose = graphics.pose()

        pose.pushPose()
        graphics.blit(data.icon, x, y, 0f, 0f, 16, 16, 16, 16)
        pose.popPose()

        val textX = x + 16 + 4
        var textY = y

        graphics.drawString(font, data.name, textX, textY, data.titleColor.color ?: 0xFFFFFF, false)
        textY += font.lineHeight

        for (line in wrappedLines) {
            graphics.drawString(font, line, textX, textY, data.descColor.color ?: 0xAAAAAA, false)
            textY += font.lineHeight
        }
    }
}
