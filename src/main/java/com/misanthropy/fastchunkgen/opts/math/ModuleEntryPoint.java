package com.misanthropy.fastchunkgen.opts.math;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.optimizeNoiseSampling")
            .comment("Faster Perlin noise sampling. Produces identical terrain.")
            .getBoolean(true, false);

}
