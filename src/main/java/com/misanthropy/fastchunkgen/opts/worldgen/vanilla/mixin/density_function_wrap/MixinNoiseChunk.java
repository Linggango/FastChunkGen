package com.misanthropy.fastchunkgen.opts.worldgen.vanilla.mixin.density_function_wrap;

import net.minecraft.world.level.levelgen.DensityFunction;
import net.minecraft.world.level.levelgen.NoiseChunk;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.IdentityHashMap;
import java.util.Map;
import java.util.function.Function;

@Mixin(NoiseChunk.class)
public class MixinNoiseChunk {

    @Unique
    private Map<DensityFunction, DensityFunction> fcg$wrappedByIdentity;

    @Redirect(
            method = "wrap",
            at = @At(
                    value = "INVOKE",
                    target = "Ljava/util/Map;computeIfAbsent(Ljava/lang/Object;Ljava/util/function/Function;)Ljava/lang/Object;"
            )
    )
    private Object fcg$wrapByIdentity(Map<DensityFunction, DensityFunction> original, Object key, Function<Object, Object> wrapNew) {
        Map<DensityFunction, DensityFunction> map = this.fcg$wrappedByIdentity;
        if (map == null) {
            map = this.fcg$wrappedByIdentity = new IdentityHashMap<>();
        }
        final DensityFunction function = (DensityFunction) key;
        DensityFunction wrapped = map.get(function);
        if (wrapped == null) {
            wrapped = (DensityFunction) wrapNew.apply(function);
            map.put(function, wrapped);
        }
        return wrapped;
    }

}
