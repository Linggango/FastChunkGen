package com.misanthropy.fastchunkgen.opts.chunk_access;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    public static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.optimizeAsyncChunkRequest")
            .comment("Serve off-thread chunk requests without blocking the server thread.")
            .getBoolean(true, false);

}
