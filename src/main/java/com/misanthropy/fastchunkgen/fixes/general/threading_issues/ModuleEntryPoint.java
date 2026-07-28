package com.misanthropy.fastchunkgen.fixes.general.threading_issues;
import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class ModuleEntryPoint {

    private static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("fixes.generalThreadingIssues")
            .comment("Fix thread-safety issues in the vanilla chunk system.")
            .getBoolean(true, false);


}
