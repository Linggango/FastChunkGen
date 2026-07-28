package com.misanthropy.fastchunkgen.threading.lighting;

import com.misanthropy.fastchunkgen.base.common.util.ModUtil;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("threadedLighting.enabled")
            .comment("Run light updates on a dedicated thread.")
            .getBoolean(!ModUtil.isModLoaded("lightbench"), false);

}
