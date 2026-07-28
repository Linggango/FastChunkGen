package com.misanthropy.fastchunkgen.threading.worldgen;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;
import com.misanthropy.fastchunkgen.threading.worldgen.common.Config;

import static com.misanthropy.fastchunkgen.base.ModuleEntryPoint.globalExecutorParallelism;

public class ModuleEntryPoint {

    public static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("threadedWorldGen.enabled")
            .comment("Generate chunks on multiple threads.")
            .getBoolean(globalExecutorParallelism >= 3, false);

    static {
        Config.init();
    }

}
