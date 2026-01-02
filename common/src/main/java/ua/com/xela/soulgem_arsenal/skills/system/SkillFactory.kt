package ua.com.xela.soulgem_arsenal.skills.system

import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.SkillEntry
import ua.com.xela.soulgem_arsenal.skills.base.getOrCreateInstanceId
import ua.com.xela.soulgem_arsenal.skills.base.setSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail.ArrowsHailSkill
import ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport.TeleportSkill
import ua.com.xela.soulgem_arsenal.skills.feature.sword.strength.StrengthSkill

object SkillFactory {

    val BowArrowsHail = SkillEntry(
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

    val BowTeleport = SkillEntry(
        TeleportSkill::class
    ) {
        TeleportSkill(
            selected = true,
            armed = false,
            charges = 3,
            maxCharges = 3,
            maxCooldown = 20
        )
    }
    val SwordStrength = SkillEntry(
        StrengthSkill::class
    ) {
        StrengthSkill(
            selected = true,
            armed = false,
            charges = 3,
            maxCharges = 3,
            maxCooldown = 120,
            amplifier = 0,
            durationTicks = 20 * 30,
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