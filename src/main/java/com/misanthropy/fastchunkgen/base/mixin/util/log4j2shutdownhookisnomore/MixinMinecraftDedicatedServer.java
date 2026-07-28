package com.misanthropy.fastchunkgen.base.mixin.util.log4j2shutdownhookisnomore;

import com.misanthropy.fastchunkgen.base.common.util.Log;
import net.minecraft.server.dedicated.DedicatedServer;
import org.apache.logging.log4j.LogManager;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(DedicatedServer.class)
public class MixinMinecraftDedicatedServer {

    private static final long FORCE_EXIT_DELAY_MILLIS = 30_000L;

    @Inject(method = "onServerExit", at = @At("RETURN"))
    private void onPostShutdown(CallbackInfo ci) {
        final Thread watchdog = new Thread(() -> {
            try {
                Thread.sleep(FORCE_EXIT_DELAY_MILLIS);
            } catch (InterruptedException ignored) {
                return;
            }
            Log.MAIN.warn("Server has not exited {} ms after shutdown, forcing exit", FORCE_EXIT_DELAY_MILLIS);
            LogManager.shutdown();
            Runtime.getRuntime().halt(0);
        }, "FastChunkGen shutdown watchdog");
        watchdog.setDaemon(true);
        watchdog.start();
    }

}
