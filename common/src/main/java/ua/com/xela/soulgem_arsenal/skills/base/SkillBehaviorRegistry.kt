package ua.com.xela.soulgem_arsenal.skills.base

import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import kotlin.reflect.KClass

object SkillBehaviorRegistry {

    private val behaviors: MutableMap<KClass<out Skill>, SkillBehavior<out Skill>> = mutableMapOf()

    fun <T : Skill> register(behavior: SkillBehavior<T>) {
        behaviors[behavior.skillClass] = behavior
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Skill> get(skill: T): SkillBehavior<T>? {
        return behaviors[skill::class] as? SkillBehavior<T>
    }

    @Suppress("UNCHECKED_CAST")
    fun <T : Skill> getByClass(clazz: KClass<T>): SkillBehavior<T>? {
        return behaviors[clazz] as? SkillBehavior<T>
    }

    fun dispatchBowShot(skill: Skill, ctx: BowShotContext) {
        @Suppress("UNCHECKED_CAST")
        val behavior = behaviors[skill::class] as? SkillBehavior<Skill> ?: return
        behavior.onBowShot(skill, ctx)
    }

    fun dispatchArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {
        behaviors.values.forEach { behavior ->
            @Suppress("UNCHECKED_CAST")
            (behavior as SkillBehavior<Skill>).onArrowBlockHitGlobal(ctx)
        }
    }
    fun dispatchArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {
        behaviors.values.forEach { behavior ->
            @Suppress("UNCHECKED_CAST")
            (behavior as SkillBehavior<Skill>).onArrowEntityHitGlobal(ctx)
        }
    }


}
