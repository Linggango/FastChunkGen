package com.misanthropy.fastchunkgen.fixes.general.threading_issues.mixin.asynccatchers;

import com.misanthropy.fastchunkgen.base.common.util.Log;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import java.util.ConcurrentModificationException;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer {

    @Shadow @Final private Thread serverThread;

    @Inject(method = "saveAllChunks", at = @At("HEAD"))
    private void preventAsyncSave(CallbackInfoReturnable<Boolean> cir) {
        if (Thread.currentThread() != this.serverThread) {
            final ConcurrentModificationException exception = new ConcurrentModificationException("MinecraftServer.saveAllChunks called off the server thread");
            Log.ASYNC_CATCHER.error("Unsafe off-thread chunk system access, this is a bug in another mod", exception);
            throw exception;
        }
    }

    @Inject(method = "saveEverything", at = @At("HEAD"))
    private void preventAsyncSaveAll(CallbackInfoReturnable<Boolean> cir) {
        if (Thread.currentThread() != this.serverThread) {
            final ConcurrentModificationException exception = new ConcurrentModificationException("MinecraftServer.saveEverything called off the server thread");
            Log.ASYNC_CATCHER.error("Unsafe off-thread chunk system access, this is a bug in another mod", exception);
            throw exception;
        }
    }

}
