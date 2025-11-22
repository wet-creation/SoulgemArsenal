package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.AbstractArrow
import ua.com.xela.soulgem_arsenal.mixin.api.ModifiedPlayer
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.base.ActivationContext
import ua.com.xela.soulgem_arsenal.skills.base.ArrowBlockHitContext
import ua.com.xela.soulgem_arsenal.skills.base.ArrowEntityHitContext
import ua.com.xela.soulgem_arsenal.skills.base.BowShotContext
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.setSkill
import kotlin.reflect.KClass

object TeleportSkillBehavior : SkillBehavior<TeleportSkill> {

    override val skillClass: KClass<TeleportSkill> = TeleportSkill::class

    override fun onBowShot(skill: TeleportSkill, ctx: BowShotContext) {
        val player = ctx.player

        if (!skill.enabled || !skill.armed || skill.charges <= 0) return

        if (player !is ModifiedPlayer) return

        player.soulgem_SkillsPut(skillClass, true)

        val newState = skill.copy(
            armed = false,
            charges = skill.charges - 1
        )
        ctx.stack.setSkill(newState)
    }

    override fun onArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {
        teleportToTarget(
            arrow = ctx.arrow,
            shooter = ctx.shooter
        )
    }

    override fun onArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {
        teleportToTarget(
            arrow = ctx.arrow,
            shooter = ctx.shooter
        )
    }

    override fun onActivate(ctx: ActivationContext): Skill? {
        val skill = ctx.skill as? TeleportSkill ?: return null

        if (!skill.enabled) return null
        if (skill.charges <= 0) return null
        if (!ctx.isSneaking) return null

        val newSkill = skill.copy(
            armed = !skill.armed
        )


        return newSkill
    }

    private fun teleportToTarget(
        arrow: AbstractArrow,
        shooter: ServerPlayer
    ) {
        val teleShooter = shooter as ModifiedPlayer

        if (teleShooter.soulgem_SkillsGet(skillClass).not()) return

        val arrow = arrow
        shooter.teleportTo(arrow.x, arrow.y, arrow.z)

        shooter.soulgem_SkillsPut(skillClass, false)
    }
}
