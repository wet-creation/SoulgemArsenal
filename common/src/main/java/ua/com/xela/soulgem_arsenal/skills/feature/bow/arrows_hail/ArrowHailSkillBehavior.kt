package ua.com.xela.soulgem_arsenal.skills.feature.bow.arrows_hail

import net.minecraft.server.level.ServerLevel
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.entity.projectile.Arrow
import net.minecraft.world.entity.projectile.SpectralArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.Vec3
import ua.com.xela.soulgem_arsenal.core.coroutines.MinecraftServerScope
import ua.com.xela.soulgem_arsenal.skills.api.Skill
import ua.com.xela.soulgem_arsenal.skills.api.SkillBehavior
import ua.com.xela.soulgem_arsenal.skills.base.*
import kotlin.math.cos
import kotlin.math.sin
import kotlin.reflect.KClass

object ArrowHailSkillBehavior : SkillBehavior<ArrowsHailSkill> {
    override val skillClass: KClass<ArrowsHailSkill>
        get() = ArrowsHailSkill::class

    override fun onActivate(ctx: ActivationContext): Skill? {
        val skill = ctx.skill as? ArrowsHailSkill ?: return null

        if (!skill.selected) return null
        if (skill.charges <= 0) return null

        val newSkill = skill.copy(
            armed = !skill.armed
        )

        return newSkill
    }

    override fun onBowShot(skill: ArrowsHailSkill, ctx: BowShotContext) {
        super.onBowShot(skill, ctx)
        ctx.defaultFlow(skill, skillClass) {
            val newState = skill.copy(
                armed = false,
                charges = skill.charges - 1
            )
            ctx.stack.setSkill(newState)

            ArrowHailSkillCooldown.startForStack(
                stack = ctx.stack,
                player = player,
                skill = newState
            )
        }

    }


    override fun onArrowBlockHitGlobal(ctx: ArrowBlockHitContext) {
        ctx.defaultFlow(skillClass) {
            arrowHail(arrow, level, pickUpItemStack)
        }
    }

    override fun onArrowEntityHitGlobal(ctx: ArrowEntityHitContext) {
        ctx.defaultFlow(skillClass) {
            arrowHail(arrow, level, pickUpItemStack)
        }
    }

    private data class Center(val x: Double, val z: Double)

    private fun arrowHail(arrow: AbstractArrow, level: ServerLevel, pickUpItemStack: ItemStack) {
        val center = Center(
            arrow.x,
            arrow.z,
        )
        val heightOfHail = arrow.y + 10
        val rIteration = 1.5f
        val iteration = 5
        val step = 10
        val delay = 200L
        MinecraftServerScope.launchInScope {
            var r = 1f
            while (r < iteration * rIteration) {
                delayAndWaitIfPaused(delay)
                for (i in 0..360 step step) {
                    delayAndWaitIfPaused(50L)
                    val rad = Math.toRadians(i.toDouble())
                    val x: Double = center.x + r * cos(rad)
                    val z: Double = center.z + r * sin(rad)
                    level.summonArrow(
                        arrow = arrow,
                        pos = Vec3(x, heightOfHail, z),
                        pickupItem = pickUpItemStack
                    )

                }
                r += rIteration
            }
        }


    }

    private fun ServerLevel.summonArrow(
        arrow: AbstractArrow,
        pos: Vec3,
        pickupItem: ItemStack
    ) {
        val arrow = if (arrow is SpectralArrow)
            SpectralArrow(
                level, pos.x,
                pos.y,
                pos.z,
                pickupItem, null
            )
        else
            Arrow(
                level, pos.x,
                pos.y,
                pos.z,
                pickupItem, null
            )

        this.addFreshEntity(arrow)
    }


}