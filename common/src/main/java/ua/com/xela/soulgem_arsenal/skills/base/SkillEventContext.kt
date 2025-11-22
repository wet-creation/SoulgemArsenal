package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.EntityHitResult
import ua.com.xela.soulgem_arsenal.skills.Skill

sealed interface SkillEventContext

data class BowShotContext(
    val level: ServerLevel,
    val player: ServerPlayer,
    val stack: ItemStack,
    val power: Float
) : SkillEventContext

data class ArrowBlockHitContext(
    val level: ServerLevel,
    val arrow: AbstractArrow,
    val shooter: ServerPlayer
) : SkillEventContext

data class ArrowEntityHitContext(
    val level: ServerLevel,
    val arrow: AbstractArrow,
    val shooter: ServerPlayer,
    val hitResult: EntityHitResult
) : SkillEventContext

data class ActivationContext(
    val player: ServerPlayer,
    val stack: ItemStack,
    val skill: Skill,
    val hand: InteractionHand,
    val isSneaking: Boolean
)


