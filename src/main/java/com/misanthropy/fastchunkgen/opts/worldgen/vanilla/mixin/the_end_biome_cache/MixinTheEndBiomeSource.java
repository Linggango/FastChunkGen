package com.misanthropy.fastchunkgen.opts.worldgen.vanilla.mixin.the_end_biome_cache;

import it.unimi.dsi.fastutil.longs.Long2ObjectLinkedOpenHashMap;
import net.minecraft.core.Holder;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Climate;
import net.minecraft.world.level.biome.TheEndBiomeSource;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TheEndBiomeSource.class)
public abstract class MixinTheEndBiomeSource {

    private final ThreadLocal<Long2ObjectLinkedOpenHashMap<Holder<Biome>>> cache = ThreadLocal.withInitial(Long2ObjectLinkedOpenHashMap::new);
    private final int cacheCapacity = 1024;

    @Inject(method = "getNoiseBiome", at = @At("HEAD"), cancellable = true)
    private void onGetNoiseBiomeHead(int biomeX, int biomeY, int biomeZ, Climate.Sampler multiNoiseSampler, CallbackInfoReturnable<Holder<Biome>> cir) {
        final Holder<Biome> biome = this.cache.get().get(ChunkPos.asLong(biomeX, biomeZ));
        if (biome != null) {
            cir.setReturnValue(biome);
        }
    }

    @Inject(method = "getNoiseBiome", at = @At("RETURN"))
    private void onGetNoiseBiomeReturn(int biomeX, int biomeY, int biomeZ, Climate.Sampler multiNoiseSampler, CallbackInfoReturnable<Holder<Biome>> cir) {
        final Long2ObjectLinkedOpenHashMap<Holder<Biome>> cache = this.cache.get();
        cache.put(ChunkPos.asLong(biomeX, biomeZ), cir.getReturnValue());
        if (cache.size() > cacheCapacity) {
            for (int i = 0; i < cacheCapacity / 16; i++) {
                cache.removeFirst();
            }
        }
    }

}
