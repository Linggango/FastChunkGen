package com.misanthropy.fastchunkgen.threading.chunkio;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("ioSystem.async")
            .comment("Load and unload chunks off the server thread.")
            .incompatibleMod("radon", "*")
            .getBoolean(true, false);

}
