package com.misanthropy.fastchunkgen.fixes.worldgen.threading_issues.mixin.compat;

import com.misanthropy.fastchunkgen.fixes.worldgen.threading_issues.common.IsolatedFeaturePlacement;
import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.TreeConfiguration;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Pseudo;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Pseudo
@Mixin(targets = "com.teamabnormals.blueprint.common.levelgen.feature.BlueprintTreeFeature", remap = false)
public abstract class MixinBlueprintTreeFeature implements Cloneable {

    @SuppressWarnings("unchecked")
    @Inject(method = {"place", "m_142674_"}, at = @At("HEAD"), cancellable = true)
    private void fastchunkgen$isolateMutableState(FeaturePlaceContext<TreeConfiguration> context, CallbackInfoReturnable<Boolean> cir) {
        if (IsolatedFeaturePlacement.isIsolated(this)) {
            return;
        }
        final Feature<TreeConfiguration> copy;
        try {
            copy = (Feature<TreeConfiguration>) this.clone();
        } catch (CloneNotSupportedException e) {
            return;
        }
        cir.setReturnValue(IsolatedFeaturePlacement.place(copy, context));
    }
}
