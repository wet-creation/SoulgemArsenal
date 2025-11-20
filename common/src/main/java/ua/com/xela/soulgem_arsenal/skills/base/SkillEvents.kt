package ua.com.xela.soulgem_arsenal.skills.base

import net.minecraft.server.level.ServerLevel
import net.minecraft.server.level.ServerPlayer
import net.minecraft.world.entity.projectile.AbstractArrow
import net.minecraft.world.item.ItemStack
import net.minecraft.world.phys.EntityHitResult
import ua.com.xela.soulgem_arsenal.registries.SkillComponents
import ua.com.xela.soulgem_arsenal.skills.Skill

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
}
