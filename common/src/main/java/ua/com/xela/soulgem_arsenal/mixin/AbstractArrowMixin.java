package ua.com.xela.soulgem_arsenal.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.projectile.AbstractArrow;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents;

@Mixin(AbstractArrow.class)
public abstract class AbstractArrowMixin {

    @Inject(method = "onHitBlock", at = @At("HEAD"))
    private void soulgem$onHitBlock(BlockHitResult hit, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        var level = self.level();
        var owner = self.getOwner();
        if (!level.isClientSide() && owner instanceof ServerPlayer shooter) {
            SkillEvents.INSTANCE.onArrowBlockHit((ServerLevel) level, self, shooter);
        }
    }

    @Inject(method = "onHitEntity", at = @At("HEAD"))
    private void soulgem$onHitEntity(EntityHitResult pResult, CallbackInfo ci) {
        AbstractArrow self = (AbstractArrow) (Object) this;
        var level = self.level();
        var owner = self.getOwner();
        if (!level.isClientSide() && owner instanceof ServerPlayer shooter) {
            SkillEvents.INSTANCE.onArrowEntityHit((ServerLevel) level, self, shooter, pResult);
        }
    }

}

