package ua.com.xela.soulgem_arsenal.mixin.injects.gameplay.bow;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.skills.base.ArrowBlockHitContext;
import ua.com.xela.soulgem_arsenal.skills.base.ArrowEntityHitContext;
import ua.com.xela.soulgem_arsenal.skills.system.SkillEvents;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void soulgem$onHitBlock(BlockHitResult hit, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        var level = self.level();
        var owner = self.getOwner();
        var itemStack = self.getPickupItemStackOrigin();
        if (!level.isClientSide() && owner instanceof ServerPlayer shooter) {
            SkillEvents.INSTANCE
                    .onArrowBlockHit(new ArrowBlockHitContext((ServerLevel) level, self, shooter, itemStack));
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void soulgem$onHitEntity(EntityHitResult pResult, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        var level = self.level();
        var owner = self.getOwner();
        var itemStack = self.getPickupItemStackOrigin();
        if (!level.isClientSide() && owner instanceof ServerPlayer shooter) {
            SkillEvents.INSTANCE
                    .onArrowEntityHit(new ArrowEntityHitContext((ServerLevel) level, self, shooter, itemStack, pResult));
        }
    }

}
