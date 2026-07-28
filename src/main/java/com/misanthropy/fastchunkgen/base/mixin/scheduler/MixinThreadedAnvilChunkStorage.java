package com.misanthropy.fastchunkgen.base.mixin.scheduler;

import com.misanthropy.fastchunkgen.base.common.GlobalExecutors;
import com.misanthropy.fastchunkgen.base.common.scheduler.IVanillaChunkManager;
import com.misanthropy.fastchunkgen.base.common.scheduler.SchedulingManager;
import net.minecraft.server.level.ChunkHolder;
import net.minecraft.server.level.ChunkMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ChunkMap.class)
public class MixinThreadedAnvilChunkStorage implements IVanillaChunkManager {

    private final SchedulingManager fcg$schedulingManager = new SchedulingManager(GlobalExecutors.asyncScheduler, GlobalExecutors.GLOBAL_EXECUTOR_PARALLELISM * 2);

    @Override
    public SchedulingManager fcg$getSchedulingManager() {
        return this.fcg$schedulingManager;
    }

    @Inject(method = "updateChunkScheduling", at = @At("RETURN"))
    private void onUpdateLevel(long pos, int level, ChunkHolder holder, int i, CallbackInfoReturnable<ChunkHolder> cir) {
        this.fcg$schedulingManager.updatePriorityFromLevel(pos, level);
    }

}
