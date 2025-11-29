package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.base.*
import kotlin.reflect.KClass

object TeleportSkillBehavior : SkillBehavior<TeleportSkill> {

    override val skillClass: KClass<TeleportSkill> = TeleportSkill::class

    override fun onActivate(ctx: ActivationContext): Skill? {
        val skill = ctx.skill as? TeleportSkill ?: return null

        if (!skill.selected) return null
        if (skill.charges <= 0) return null

        val newSkill = skill.copy(
            armed = !skill.armed
        )

        return newSkill
    }

    override fun onBowShot(skill: TeleportSkill, ctx: BowShotContext) {
       ctx.defaultFlow(skill, skillClass) {
           val player = ctx.player
           val newState = skill.copy(
               armed = false,
               charges = skill.charges - 1
           )
           val hand = player.getHand(stack = ctx.stack)
           ctx.stack.setSkill(newState)
           TeleportSkillCooldown.startForStack(
               stack = ctx.stack,
               player = player,
               hand = hand,
               skill = newState
           )
       }
    }

    override fun onArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {
        ctx.defaultFlow(skillClass) {
            teleportToTarget(
                arrow = ctx.arrow,
                shooter = ctx.shooter
            )
        }
    }

    override fun onArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {
        ctx.defaultFlow(skillClass) {
            teleportToTarget(
                arrow = ctx.arrow,
                shooter = ctx.shooter
            )
        }
    }

    private fun teleportToTarget(
        arrow: AbstractArrow,
        shooter: ServerPlayer
    ) {
        shooter.teleportTo(arrow.x, arrow.y, arrow.z)
    }
}

fun ItemStack.hasTeleportSkill(): Boolean {
    val data = getSkill<TeleportSkill>() ?: return false
    return data.armedOrEnabled && data.charges > 0
}

fun ItemStack.armTeleportSkill(): Boolean {
    val data = getSkill<TeleportSkill>() ?: return false
    if (!data.selected || data.charges <= 0) return false

    val updated = data.copy(armed = true)
    this.set(SkillComponents.Teleport.get(), updated)
    return true
}

fun ItemStack.disarmTeleportSkill() {
    val data = getSkill<TeleportSkill>() ?: return
    if (!data.armed) return
    this.set(SkillComponents.Teleport.get(), data.copy(armed = false))
}
