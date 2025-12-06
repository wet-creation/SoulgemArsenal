package ua.com.xela.soulgem_arsenal.mixin.injects.player;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import ua.com.xela.soulgem_arsenal.core.SoulgemHooks;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerDropMixin {

    @Inject(method = "drop", at = @At("TAIL"))
    private void onDrop(ItemStack stack, boolean dropAround, boolean includeName,
                        CallbackInfoReturnable<ItemEntity> cir) {

        SoulgemHooks.INSTANCE.onItemDropped(stack);
    }
}

