package com.misanthropy.fastchunkgen.fixes.worldgen.threading_issues.common;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class Config {

    public static final boolean enforceSafeWorldRandomAccess = new ConfigSystem.ConfigAccessor()
            .key("fixes.enforceSafeWorldRandomAccess")
            .comment("Crash instead of warn when something uses the world random off-thread. For debugging.")
            .getBoolean(false, false);

    public static void init() {
    }

}
