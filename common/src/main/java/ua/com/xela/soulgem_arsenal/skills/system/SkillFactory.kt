package ua.com.xela.soulgem_arsenal.skills.system

import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry
import ua.com.xela.soulgem_arsenal.skills.base.getOrCreateInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.setSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill

object SkillFactory {

    val ArrowsHail = SkillEntry(
        ArrowsHailSkill::class
    ) {
        ArrowsHailSkill(
            selected = true,
            armed = false,
            charges = 1,
            maxCharges = 1,
            maxCooldown = 20
        )
    }

    val Teleport = SkillEntry(
        TeleportSkill::class
    ) {
        TeleportSkill(
            selected = true,
            armed = false,
            charges = 1,
            maxCharges = 1,
            maxCooldown = 20
        )
    }

    fun <T : Skill> applySkillEntry(
        stack: ItemStack,
        entry: SkillEntry<T>
    ) {
        val skill = entry.factory()
        stack.getOrCreateInstanceId()
        stack.setSkill(entry.clazz, skill)
    }

}