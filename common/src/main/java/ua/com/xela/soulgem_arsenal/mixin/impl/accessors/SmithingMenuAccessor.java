package ua.com.xela.soulgem_arsenal.mixin.impl.accessors;

import net.minecraft.world.Container;
import net.minecraft.world.inventory.ItemCombinerMenu;
import net.minecraft.world.inventory.ResultContainer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(ItemCombinerMenu.class)
public interface SmithingMenuAccessor {
    @Accessor("inputSlots") // имя поля в SmithingMenu
    Container soulgem_getInputSlots();

    @Accessor("resultSlots")
    ResultContainer soulgem_resultSlots();
}
