package ua.com.xela.soulgem_arsenal.skills.base

import ua.com.xela.soulgem_arsenal.mixin.api.ModifiedPlayer
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import kotlin.reflect.KClass


inline fun <T : Skill> BowShotContext.defaultFlow(
    skill: T,
    skillClass: KClass<out Skill>,
    block: BowShotContext.(ModifiedPlayer) -> Unit
) {
    if (player !is ModifiedPlayer) return
    if (!skill.selected || !skill.armed || skill.charges <= 0) return
    player.soulgem_SkillsPut(skillClass, true)
    block(player)
}

inline fun ArrowBlockHitContext.defaultFlow(
    skillClass: KClass<out Skill>,
    block: ArrowBlockHitContext.(ModifiedPlayer) -> Unit
) {
    val shooter = shooter as? ModifiedPlayer ?: return
    if (!shooter.soulgem_SkillsGet(skillClass)) return

    try {
        block(shooter)
    } finally {
        shooter.soulgem_SkillsPut(skillClass, false)
    }
}

inline fun ArrowEntityHitContext.defaultFlow(
    skillClass: KClass<out Skill>,
    block: ArrowEntityHitContext.(ModifiedPlayer) -> Unit
) {
    val shooter = shooter as? ModifiedPlayer ?: return
    if (!shooter.soulgem_SkillsGet(skillClass)) return

    try {
        block(shooter)
    } finally {
        shooter.soulgem_SkillsPut(skillClass, false)
    }
}




