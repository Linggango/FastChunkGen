package com.misanthropy.fastchunkgen.rewrites.chunkio;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("ioSystem.replaceImpl")
            .comment("Use the rewritten IO implementation.")
            .getBoolean(com.misanthropy.fastchunkgen.base.ModuleEntryPoint.globalExecutorParallelism >= 2, false);

}
