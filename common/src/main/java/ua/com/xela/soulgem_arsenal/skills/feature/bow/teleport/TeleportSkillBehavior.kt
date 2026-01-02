package ua.com.xela.soulgem_arsenal.skills.feature.bow.teleport

import net.minecraft.world.entity.player.Player
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.level.Level
import ua.com.xela.soulgem_arsenal.registry.ParticleRegister
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.base.*
import kotlin.reflect.KClass


object TeleportSkillBehavior : SkillBehavior<TeleportSkill> {

    override val skillClass: KClass<TeleportSkill> = TeleportSkill::class

    override fun onActivate(ctx: ActivationContext): Skill? =
        ctx.defaultFlow(ctx.skill) {
            val skill = ctx.skill as? TeleportSkill ?: return null
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
            ctx.stack.setSkill(newState)
            TeleportSkillCooldown.startForStack(
                stack = ctx.stack,
                player = player,
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
        shooter: Player
    ) {

        spawnTeleportBurst(shooter.level(), shooter.x, shooter.y, shooter.z)

        shooter.teleportTo(arrow.x, arrow.y, arrow.z)
    }


    private fun spawnTeleportBurst(level: Level, x: Double, y: Double, z: Double) {
        summonParticle {
            count = 25
            this.level = level
            this.x = x + (level.random.nextDouble() - 0.5) * 0.6
            this.y = y + (level.random.nextDouble() - 0.5) * 0.6
            this.z = z + (level.random.nextDouble() - 0.5) * 0.6
            particleType = ParticleRegister.ChaoticParticle.get()
            speed = level.random.nextDouble() * 0.15
        }
    }
}


fun ItemStack.hasTeleportSkill(): Boolean {
    val data = getSkill<TeleportSkill>() ?: return false
    return data.armedOrSelected && data.charges > 0
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
