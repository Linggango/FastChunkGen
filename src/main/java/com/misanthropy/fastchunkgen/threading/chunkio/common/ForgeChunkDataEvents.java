package com.misanthropy.fastchunkgen.threading.chunkio.common;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.chunk.ChunkAccess;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.level.ChunkDataEvent;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

public class ForgeChunkDataEvents {

    public static CompletableFuture<Void> fireSave(ServerLevel level, ChunkAccess chunk, CompoundTag tag, Executor mainThreadExecutor) {
        if (!Config.fireForgeChunkDataEvents) {
            return CompletableFuture.completedFuture(null);
        }
        final LevelAccessor levelAccessor = chunk.getWorldForge() != null ? chunk.getWorldForge() : level;
        final Runnable task = () -> MinecraftForge.EVENT_BUS.post(new ChunkDataEvent.Save(chunk, levelAccessor, tag));
        if (Config.forgeChunkDataEventsOnMainThread) {
            return CompletableFuture.runAsync(task, mainThreadExecutor);
        }
        task.run();
        return CompletableFuture.completedFuture(null);
    }
}
