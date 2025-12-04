package ua.com.xela.soulgem_arsenal.platform.client

import net.minecraft.network.chat.Component
import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.core.Constants
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill

object SkillGUI {

    private const val TELEPORT = "teleport"
    private const val ARROWS_HAIL= "arrows_hail"

    private val map: Map<SkillKey, SkillTooltipComponent> = mapOf(
        TeleportSkill.KEY to SkillTooltipComponent(
            icon = TELEPORT.toIcon(),
            name = TELEPORT.toName(),
            description = TELEPORT.toDescription(),
            symbol = "»"
        ),
        ArrowsHailSkill.KEY to SkillTooltipComponent(
            icon = ARROWS_HAIL.toIcon(),
            name = ARROWS_HAIL.toName(),
            description = ARROWS_HAIL.toDescription(),
            symbol = "◆"
        ),
    )

    private fun String.toName(): Component {
        return Component.translatable("tooltip.skill.${Constants.MOD_ID}.${this}.name")
    }

    private fun String.toDescription(): Component {
        return Component.translatable("tooltip.skill.${Constants.MOD_ID}.${this}.desc")
    }

    private fun String.toIcon(format: String = "png"): ResourceLocation {
        return Common.createResourceLocation("textures/gui/skills/$this.$format")
    }

    fun getSkillGUIComponents(skillKey: SkillKey): SkillTooltipComponent? = map[skillKey]
}