package ua.com.xela.soulgem_arsenal.mixin.impl;

import net.minecraft.client.Minecraft;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.core.MinecraftScope;
import ua.com.xela.soulgem_arsenal.core.MinecraftServerScope;

@Mixin(Minecraft.class)
public abstract class MixinMinecraft {

    @Shadow
    private volatile boolean pause;

    @Unique
    private boolean soulgemArsenal$wasPaused = false;

    @Unique
    private boolean soulgemArsenal$wasLocalSingleplayerAtPause = false;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void soulgem$onClientInit(CallbackInfo ci) {
        MinecraftScope.INSTANCE.resume();
    }

    @Inject(method = "close", at = @At("HEAD"))
    private void soulgem$onClientClose(CallbackInfo ci) {
        MinecraftScope.INSTANCE.stop();
    }

    @Inject(method = "pauseGame", at = @At("HEAD"))
    private void soulgem$onPauseGame(boolean pPauseOnly, CallbackInfo ci) {
        Minecraft self = (Minecraft) (Object) this;

        boolean isLocalSingleplayer =
                self.hasSingleplayerServer()
                        && self.getSingleplayerServer() != null
                        && !self.getSingleplayerServer().isPublished();

        soulgemArsenal$wasPaused = true;
        MinecraftScope.INSTANCE.pause();

        if (isLocalSingleplayer) {
            soulgemArsenal$wasLocalSingleplayerAtPause = true;
            MinecraftServerScope.INSTANCE.pause();
        } else {
            soulgemArsenal$wasLocalSingleplayerAtPause = false;
        }
    }

    @Inject(method = "tick", at = @At("HEAD"))
    private void soulgem$onClientTick(CallbackInfo ci) {
        if (soulgemArsenal$wasPaused && !pause) {
            soulgemArsenal$wasPaused = false;

            MinecraftScope.INSTANCE.resume();

            if (soulgemArsenal$wasLocalSingleplayerAtPause) {
                MinecraftServerScope.INSTANCE.resume();
                soulgemArsenal$wasLocalSingleplayerAtPause = false;
            }
        }
    }
}
