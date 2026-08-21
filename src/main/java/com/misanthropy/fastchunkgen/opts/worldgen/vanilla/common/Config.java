package com.misanthropy.fastchunkgen.opts.worldgen.vanilla.common;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class Config {

    public static final boolean optimizeAquifer = new ConfigSystem.ConfigAccessor()
            .key("vanillaWorldGenOptimizations.optimizeAquifer")
            .comment("Faster aquifer sampling.")
            .incompatibleMod("cavetweaks", "*")
            .getBoolean(true, false);

    public static final boolean optimizeDensityFunctionWrap = new ConfigSystem.ConfigAccessor()
            .key("vanillaWorldGenOptimizations.optimizeDensityFunctionWrap")
            .comment("Look up density function wrappers by identity instead of by value. Terrain is unchanged.")
            .getBoolean(true, false);

    public static final boolean useEndBiomeCache = new ConfigSystem.ConfigAccessor()
            .key("vanillaWorldGenOptimizations.useEndBiomeCache")
            .comment("Cache End biome lookups.")
            .getBoolean(true, false);

}
