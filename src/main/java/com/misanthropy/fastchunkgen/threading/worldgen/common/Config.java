package com.misanthropy.fastchunkgen.threading.worldgen.common;

import com.misanthropy.fastchunkgen.base.common.compat.ModCompat;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class Config {

    public static final boolean allowThreadedFeatures = new ConfigSystem.ConfigAccessor()
            .key("threadedWorldGen.allowThreadedFeatures")
            .comment("Generate features (trees, ores, structures) in parallel.")
            .getBoolean(true, false);

    public static final boolean reduceLockRadius = new ConfigSystem.ConfigAccessor()
            .key("threadedWorldGen.reduceLockRadius")
            .comment("Lock fewer neighbouring chunks per generation step. More parallelism.")
            .getBoolean(true, false);

    public static final boolean asyncScheduling = new ConfigSystem.ConfigAccessor()
            .key("threadedWorldGen.asyncScheduling")
            .comment("Schedule generation tasks off the server thread.")
            .getBoolean(true, false)
            && !ModCompat.isModernFixFeatureEnabled("mixin.perf.release_protochunks");

    public static void init() {
    }

}
