package ua.com.xela.soulgem_arsenal.mixin.impl;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BowItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;
import ua.com.xela.soulgem_arsenal.skills.base.SkillEvents;


@Mixin(BowItem.class)
public class BowItemMixin {

    @Inject(
            method = "releaseUsing",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/level/Level;playSound(Lnet/minecraft/world/entity/player/Player;DDDLnet/minecraft/sounds/SoundEvent;Lnet/minecraft/sounds/SoundSource;FF)V"
            ),
            locals = LocalCapture.CAPTURE_FAILHARD
    )
    private void shootProjectile(
            ItemStack pStack, Level pLevel, LivingEntity pEntityLiving, int pTimeLeft, CallbackInfo ci, Player player, ItemStack itemstack, int i, float f
    ) {
        if (!(pLevel instanceof ServerLevel serverlevel)) return;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        SkillEvents.INSTANCE.onBowShot(serverlevel, serverPlayer, pStack, f);
    }


}
