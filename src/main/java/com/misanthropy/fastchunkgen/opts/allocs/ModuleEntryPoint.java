package com.misanthropy.fastchunkgen.opts.allocs;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.reduceAllocations")
            .comment("Reduce allocations in hot paths.")
            .getBoolean(true, false);

    static {
        Config.init();
    }

}
