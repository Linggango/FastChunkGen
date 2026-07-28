package com.misanthropy.fastchunkgen.base;

import com.misanthropy.fastchunkgen.base.common.ModuleMixinPlugin;
import com.misanthropy.fastchunkgen.base.common.compat.ModCompat;

/**
 * Used internally for c2me-base, do not subclass.
 */
public final class TheMixinPlugin extends ModuleMixinPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!super.shouldApplyMixin(targetClassName, mixinClassName)) {
            return false;
        }

        if (mixinClassName.startsWith("com.misanthropy.fastchunkgen.base.mixin.util.log4j2shutdownhookisnomore."))
            return ModuleEntryPoint.disableLoggingShutdownHook;

        if (mixinClassName.equals("com.misanthropy.fastchunkgen.base.mixin.priority.MixinServerChunkManager"))
            return !ModCompat.isLithiumFamilyPresent();

        return true;
    }
}
