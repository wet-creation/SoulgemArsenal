package ua.com.xela.soulgem_arsenal.skills.bow.telport

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.AbstractArrow
import ua.com.xela.soulgem_arsenal.mixin_merge_logic.ModifiedPlayer
import ua.com.xela.soulgem_arsenal.skills.base.ArrowBlockHitContext
import ua.com.xela.soulgem_arsenal.skills.base.ArrowEntityHitContext
import ua.com.xela.soulgem_arsenal.skills.base.BowShotContext
import ua.com.xela.soulgem_arsenal.skills.base.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.setSkill
import kotlin.reflect.KClass

object TeleportSkillBehavior : SkillBehavior<TeleportSkill> {

    override val skillClass: KClass<TeleportSkill> = TeleportSkill::class

    override fun onBowShot(skill: TeleportSkill, ctx: BowShotContext) {
        val player = ctx.player

        if (!skill.enabled || !skill.armed || skill.charges <= 0) return

        if (player !is ModifiedPlayer) return

        player.`soulgem$SkillsPut`(skillClass, true)

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

    private fun teleportToTarget(
        arrow: AbstractArrow,
        shooter: ServerPlayer
    ) {
        val teleShooter = shooter as ModifiedPlayer

        if (teleShooter.`soulgem$SkillsGet`(skillClass).not()) return

        val arrow = arrow
        shooter.teleportTo(arrow.x, arrow.y, arrow.z)

        shooter.`soulgem$SkillsPut`(skillClass, false)
    }
}
