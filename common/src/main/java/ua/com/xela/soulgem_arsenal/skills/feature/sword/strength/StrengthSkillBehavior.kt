package ua.com.xela.soulgem_arsenal.skills.feature.sword.strength

import net.minecraft.world.effect.MobEffectInstance
import net.minecraft.world.effect.MobEffects
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.base.ActivationContext
import ua.com.xela.soulgem_arsenal.skills.base.defaultFlow
import ua.com.xela.soulgem_arsenal.skills.base.setSkill
import kotlin.reflect.KClass

object StrengthSkillBehavior: SkillBehavior<StrengthSkill> {
    override val skillClass: KClass<StrengthSkill>
        get() = StrengthSkill::class

    override fun onActivate(ctx: ActivationContext): Skill? =
        ctx.defaultFlow(ctx.skill) {
            val skill = skill as? StrengthSkill ?: return null
            val newSkill = skill.copy(
                charges = skill.charges - 1
            )
            stack.setSkill(newSkill)
            val durationTicks = skill.durationTicks
            val amplifier = skill.amplifier
            val ambient = false
            val showParticles = true
            val showIcon = true
            player.addEffect(
                MobEffectInstance(MobEffects.DAMAGE_BOOST, durationTicks, amplifier, ambient, showParticles, showIcon)
            )
            StrengthSkillCooldown.startForStack(
                player = player,
                stack =  stack,
                skill = newSkill
            )
            return newSkill
        }
}