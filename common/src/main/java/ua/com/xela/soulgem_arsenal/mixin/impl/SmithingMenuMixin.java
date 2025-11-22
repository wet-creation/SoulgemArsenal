package ua.com.xela.soulgem_arsenal.mixin.impl;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.mixin.impl.accessors.SmithingMenuAccessor;
import ua.com.xela.soulgem_arsenal.registry.SkillAttachItems;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin {
    @Inject(method = "createResult", at = @At("TAIL"))
    private void soulgem$onTake(CallbackInfo ci) {
        Container input = ((SmithingMenuAccessor) this).soulgem_getInputSlots();

        ItemStack base = input.getItem(1);
        ItemStack addition = input.getItem(2);
        ItemStack newStack = base.copy();

        SkillAttachItems.INSTANCE.onSmithingResult(addition, newStack);

        ((SmithingMenuAccessor) this).soulgem_resultSlots().setItem(0, newStack);
    }
}

