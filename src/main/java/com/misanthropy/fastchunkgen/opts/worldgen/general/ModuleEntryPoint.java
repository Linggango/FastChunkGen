package com.misanthropy.fastchunkgen.opts.worldgen.general;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("vanillaWorldGenOptimizations.optimizeRandomInstances")
            .comment("Cheaper random instances during world generation.")
            .getBoolean(true, false);


}
