package com.misanthropy.fastchunkgen.fixes.worldgen.threading_issues.mixin.threading;

import com.misanthropy.fastchunkgen.base.common.util.Log;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.level.levelgen.feature.stateproviders.RandomizedIntStateProvider;
import org.jetbrains.annotations.Nullable;
import org.objectweb.asm.Opcodes;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

@Mixin(RandomizedIntStateProvider.class)
public class MixinRandomizedIntBlockStateProvider {

    @Shadow @Nullable private IntegerProperty property;

    @Redirect(method = "getState", at = @At(value = "FIELD", target = "Lnet/minecraft/world/level/levelgen/feature/stateproviders/RandomizedIntStateProvider;property:Lnet/minecraft/world/level/block/state/properties/IntegerProperty;", opcode = Opcodes.PUTFIELD))
    private void redirectGetProperty(RandomizedIntStateProvider randomizedIntBlockStateProvider, IntegerProperty value) {
        if (this.property != null) Log.WORLDGEN_FIXES.warn("Detected different property settings in RandomizedIntBlockStateProvider, expected {} but got {}", this.property, value);
        synchronized (this) {
            this.property = value;
        }
    }

}
