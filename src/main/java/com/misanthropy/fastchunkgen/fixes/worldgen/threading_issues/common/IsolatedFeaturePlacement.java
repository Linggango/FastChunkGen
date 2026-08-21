package com.misanthropy.fastchunkgen.fixes.worldgen.threading_issues.common;

import net.minecraft.world.level.levelgen.feature.Feature;
import net.minecraft.world.level.levelgen.feature.FeaturePlaceContext;
import net.minecraft.world.level.levelgen.feature.configurations.FeatureConfiguration;

public final class IsolatedFeaturePlacement {

    private static final ThreadLocal<Object> CURRENT = new ThreadLocal<>();

    private IsolatedFeaturePlacement() {
    }

    public static boolean isIsolated(Object feature) {
        return CURRENT.get() == feature;
    }

    public static <FC extends FeatureConfiguration> boolean place(Feature<FC> copy, FeaturePlaceContext<FC> context) {
        final Object previous = CURRENT.get();
        CURRENT.set(copy);
        try {
            return copy.place(context);
        } finally {
            CURRENT.set(previous);
        }
    }
}
