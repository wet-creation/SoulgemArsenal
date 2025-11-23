package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.InteractionHand
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.EntityHitResult
import ua.com.xela.soulgem_arsenal.registry.ComponentRef
import ua.com.xela.soulgem_arsenal.registry.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.api.Skill
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
    ) {
        val found = findSkillOnPlayerHands(player) ?: return
        val behavior = SkillBehaviorRegistry.getByClass(found.skillClass) ?: return

        val ctx = ActivationContext(
            player = player,
            stack = found.stack,
            skill = found.skill,
            hand = found.hand,
            isSneaking = player.isCrouching
        )

        val updated = behavior.onActivate(ctx) ?: return
        found.stack.setSkillDynamic(found.skillClass, updated)
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

    private data class FoundSkill(
        val hand: InteractionHand,
        val stack: ItemStack,
        val skillClass: KClass<out Skill>,
        val skill: Skill,
    )

    // Returns first skill found on main/off hand or null
    private fun findSkillOnPlayerHands(player: ServerPlayer): FoundSkill? {
        val candidates = listOf(
            InteractionHand.MAIN_HAND to player.mainHandItem,
            InteractionHand.OFF_HAND to player.offhandItem,
        )

        return candidates.firstNotNullOfOrNull { (hand, stack) ->
            if (stack.isEmpty) return@firstNotNullOfOrNull null

            val (skillClass, skill) = findSkillOnItem(stack) ?: return@firstNotNullOfOrNull null

            FoundSkill(
                hand = hand,
                stack = stack,
                skillClass = skillClass,
                skill = skill,
            )
        }
    }

}
