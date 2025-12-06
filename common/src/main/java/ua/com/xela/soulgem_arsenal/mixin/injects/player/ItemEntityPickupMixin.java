package ua.com.xela.soulgem_arsenal.mixin.injects.player;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.core.SoulgemHooks;

@Mixin(ItemEntity.class)
public class ItemEntityPickupMixin {

    @Inject(
            method = "playerTouch",
            at = @At(
                    value = "INVOKE",
                    target = "Lnet/minecraft/world/entity/player/Player;onItemPickup(Lnet/minecraft/world/entity/item/ItemEntity;)V"
            )
    )
    private void onPlayerPickup(Player player, CallbackInfo ci) {
        ItemEntity self = (ItemEntity) (Object) this;
        if (!(player instanceof ServerPlayer serverPlayer)) return;

        var stack = self.getItem();

        SoulgemHooks.INSTANCE.onItemPickedFromGround(serverPlayer, stack);
    }
}
