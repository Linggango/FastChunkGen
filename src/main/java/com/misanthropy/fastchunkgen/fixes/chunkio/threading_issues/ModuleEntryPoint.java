package com.misanthropy.fastchunkgen.fixes.chunkio.threading_issues;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("fixes.chunkIoThreadingIssues")
            .comment("Fix thread-safety issues in vanilla chunk IO.")
            .getBoolean(true, false);


}
