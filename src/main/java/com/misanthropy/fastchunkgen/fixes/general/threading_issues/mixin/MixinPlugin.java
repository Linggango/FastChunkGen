package com.misanthropy.fastchunkgen.fixes.general.threading_issues.mixin;

import com.misanthropy.fastchunkgen.base.common.ModuleMixinPlugin;
import com.misanthropy.fastchunkgen.base.common.compat.ModCompat;

public class MixinPlugin extends ModuleMixinPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (super.shouldApplyMixin(targetClassName, mixinClassName)) {
            if (mixinClassName.endsWith(".MixinChunkTicketManager")) return !ModCompat.isCanaryPresent();
            return true;
        } else {
            return false;
        }
    }
}
