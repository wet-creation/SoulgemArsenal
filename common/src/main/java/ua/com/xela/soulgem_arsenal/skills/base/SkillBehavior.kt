package ua.com.xela.soulgem_arsenal.skills.base

import ua.com.xela.soulgem_arsenal.skills.Skill
import kotlin.reflect.KClass

interface SkillBehavior<T : Skill> {
    val skillClass: KClass<T>

    fun onBowShot(skill: T, ctx: BowShotContext) {}

    fun onArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {}

    fun onArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {}

    fun onActivate(ctx: ActivationContext): Skill?
}
