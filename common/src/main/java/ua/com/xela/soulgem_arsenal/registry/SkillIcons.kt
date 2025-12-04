package ua.com.xela.soulgem_arsenal.registry

import net.minecraft.resources.ResourceLocation
import ua.com.xela.soulgem_arsenal.core.Common
import ua.com.xela.soulgem_arsenal.skills.base.SkillKey
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill

object SkillIcons {

    private val TELEPORT_ICON = Common.createResourceLocation("textures/gui/skills/teleport.png")
    private val ARROWS_HAIL_ICON = Common.createResourceLocation("textures/gui/skills/arrows_hail.png")

    private val map: Map<SkillKey, ResourceLocation> = mapOf(
        TeleportSkill.KEY to TELEPORT_ICON,
        ArrowsHailSkill.KEY to ARROWS_HAIL_ICON
    )

    fun getIcon(skillKey: SkillKey): ResourceLocation? = map[skillKey]
}
