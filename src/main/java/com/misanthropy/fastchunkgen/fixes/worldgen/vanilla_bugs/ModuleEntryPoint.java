package com.misanthropy.fastchunkgen.fixes.worldgen.vanilla_bugs;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("fixes.vanillaWorldGenBugs")
            .comment("Fix vanilla world generation bugs.")
            .getBoolean(true, false);


}
