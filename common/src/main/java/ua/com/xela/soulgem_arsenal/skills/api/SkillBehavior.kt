package ua.com.xela.soulgem_arsenal.skills.api

import ua.com.xela.soulgem_arsenal.skills.base.ActivationContext
import ua.com.xela.soulgem_arsenal.skills.base.ArrowBlockHitContext
import ua.com.xela.soulgem_arsenal.skills.base.ArrowEntityHitContext
import ua.com.xela.soulgem_arsenal.skills.base.BowShotContext
import kotlin.reflect.KClass

interface SkillBehavior<T : Skill> {
    val skillClass: KClass<T>

    fun onBowShot(skill: T, ctx: BowShotContext) {}

    fun onArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {}

    fun onArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {}

    fun onActivate(ctx: ActivationContext): Skill?
}