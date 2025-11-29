package ua.com.xela.soulgem_arsenal.mixin.injects.server;


import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import ua.com.xela.soulgem_arsenal.core.Constants;
import ua.com.xela.soulgem_arsenal.core.coroutines.MinecraftServerScope;

@Mixin(MinecraftServer.class)
public abstract class MixinServerMinecraft {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void soulgem$onServerInit(CallbackInfo ci) {
        Constants.LOG.info("server started, starting server coroutine scope");
        MinecraftServerScope.INSTANCE.attachDispatcher((MinecraftServer) (Object) this);
        MinecraftServerScope.INSTANCE.start();
    }

    @Inject(method = "stopServer", at = @At("TAIL"))
    private void soulgem$onServerStop(CallbackInfo ci) {
        Constants.LOG.info("server stopping, cancelling server coroutine scope");
        MinecraftServerScope.INSTANCE.stop();
    }
}
