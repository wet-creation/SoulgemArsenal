package ua.com.xela.soulgem_arsenal.skills.base

import ua.com.xela.soulgem_arsenal.skills.api.Skill
import kotlin.reflect.KClass

data class SkillEntry<T : Skill>(
    val clazz: KClass<T>,
    val factory: () -> T
)
