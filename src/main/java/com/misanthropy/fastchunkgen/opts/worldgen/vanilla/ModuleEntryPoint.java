package com.misanthropy.fastchunkgen.opts.worldgen.vanilla;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("vanillaWorldGenOptimizations.enabled")
            .comment("Optimize vanilla world generation.")
            .getBoolean(true, false);


}
