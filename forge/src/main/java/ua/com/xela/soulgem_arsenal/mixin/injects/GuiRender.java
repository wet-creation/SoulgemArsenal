package ua.com.xela.soulgem_arsenal.mixin.injects;


import net.minecraft.client.DeltaTracker;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Gui;
import net.minecraft.client.gui.GuiGraphics;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.platform.client.gui.ClientCooldownHud;

@Mixin(Gui.class)
public class GuiRender {
    @Inject(at = @At("TAIL"), method = "render")
    private void soulgemarsenal$RenderHud(
            GuiGraphics  graphics,
            DeltaTracker delta,
            CallbackInfo ci
    ) {
        var mc = Minecraft.getInstance();
        ClientCooldownHud.INSTANCE.render(graphics, mc.getWindow());
    }
}
