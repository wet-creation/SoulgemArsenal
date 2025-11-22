package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.EntityHitResult
import ua.com.xela.soulgem_arsenal.registry.ComponentRef
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.Skill
import ua.com.xela.soulgem_arsenal.skills.setSkillDynamic
import kotlin.reflect.KClass

object SkillEvents {

    /**
     * Вызывается из миксина BowItem.releaseUsing (после проверки power / перед shoot).
     * Здесь мы находим все скиллы на предмете и рассылаем им onBowShot().
     */
    fun onBowShot(level: ServerLevel, player: ServerPlayer, stack: ItemStack, power: Float) {
        SkillComponents.skills.forEach { (_, ref) ->
            val type = ref.get()
            val raw = stack.get(type) ?: return@forEach
            SkillBehaviorRegistry.dispatchBowShot(raw, BowShotContext(level, player, stack, power))
        }
    }

    /**
     * Вызывается из миксина AbstractArrow.onHitBlock.
     */
    fun onArrowBlockHit(level: ServerLevel, arrow: AbstractArrow, shooter: ServerPlayer) {
        SkillBehaviorRegistry.dispatchArrowBlockHitGlobal(
            ArrowBlockHitContext(level, arrow, shooter)
        )
    }

    fun onArrowEntityHit(
        level: ServerLevel,
        arrow: AbstractArrow,
        shooter: ServerPlayer,
        entityHitResult: EntityHitResult
    ) {
        SkillBehaviorRegistry.dispatchArrowEntityHitGlobal(
            ArrowEntityHitContext(
                level = level,
                arrow = arrow,
                hitResult = entityHitResult,
                shooter = shooter
            )
        )
    }


    fun onSkillActivate(
        player: ServerPlayer,
        stack: ItemStack,
        hand: InteractionHand,
        isSneaking: Boolean
    ) {
        val (skillClass, skill) = findSkillOnItem(stack) ?: return
        val behavior = SkillBehaviorRegistry.getByClass(skillClass) ?: return

        val ctx = ActivationContext(
            player = player,
            stack = stack,
            skill = skill,
            hand = hand,
            isSneaking = isSneaking
        )

        val updated = behavior.onActivate(ctx) ?: return
        stack.setSkillDynamic(skillClass, updated)
    }

    fun findSkillOnItem(stack: ItemStack): Pair<KClass<out Skill>, Skill>? {
        for ((clazz, ref) in SkillComponents.skills) {
            @Suppress("UNCHECKED_CAST")
            val component = (ref as ComponentRef<Skill>).get()
            val skill = stack.get(component) ?: continue
            return clazz to skill
        }
        return null
    }

}
