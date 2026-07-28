package com.misanthropy.fastchunkgen.opts.allocs;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class Config {

    public static final boolean overrideModernFixWorldGenAllocations = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.overrideModernFixWorldGenAllocations")
            .comment("Apply our world generation allocation patches ahead of ModernFix's equivalent ones. Disable to let ModernFix win instead.")
            .getBoolean(true, true);

    public static void init() {
    }
}
