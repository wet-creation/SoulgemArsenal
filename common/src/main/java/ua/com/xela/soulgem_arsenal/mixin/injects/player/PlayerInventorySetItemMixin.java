package ua.com.xela.soulgem_arsenal.mixin.injects.player;


import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.core.SoulgemHooks;

@Mixin(Inventory.class)
public abstract class PlayerInventorySetItemMixin {

    @Shadow
    @Final
    public Player player;

    @Inject(method = "setItem", at = @At("TAIL"))
    private void onSetItem(int slot, ItemStack stack, CallbackInfo ci) {

        if (!(player instanceof ServerPlayer serverPlayer)) return;

        if (!stack.isEmpty()) {

            SoulgemHooks.INSTANCE.onItemPutIntoInventory(serverPlayer, stack);
        }
    }
}

