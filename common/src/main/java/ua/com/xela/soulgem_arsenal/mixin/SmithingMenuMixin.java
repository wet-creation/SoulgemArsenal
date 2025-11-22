package ua.com.xela.soulgem_arsenal.mixin;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.SmithingMenu;
import net.minecraft.world.item.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.Constants;
import ua.com.xela.soulgem_arsenal.registries.SkillAttachItems;
import ua.com.xela.soulgem_arsenal.registries.SkillComponents;

@Mixin(SmithingMenu.class)
public abstract class SmithingMenuMixin {
    @Inject(method = "createResult", at = @At("TAIL"))
    private void soulgem$onTake(CallbackInfo ci) {
        Constants.LOG.info("createResult");
        Container input = ((SmithingMenuAccessor) this).soulgem_getInputSlots();

        ItemStack base = input.getItem(1);
        ItemStack addition = input.getItem(2);
        ItemStack newStack = base.copy();
        Constants.LOG.info("before {}", newStack.get(SkillComponents.INSTANCE.getTeleport().get()));

        SkillAttachItems.INSTANCE.onSmithingResult(addition, newStack);

        Constants.LOG.info("after {}", newStack.get(SkillComponents.INSTANCE.getTeleport().get()));
        ((SmithingMenuAccessor) this).soulgem_resultSlots().setItem(0, newStack);
    }
}

