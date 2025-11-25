package ua.com.xela.soulgem_arsenal.mixin.injects;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.core.Constants;

@Mixin(Minecraft.class)
public class MixinFabric {

    @Inject(at = @At("TAIL"), method = "<init>")
    private void init(CallbackInfo info) {
        Constants.LOG.info("Mixin initialized on Fabric!");
    }
}
