package com.misanthropy.fastchunkgen.threading.chunkio;

import com.misanthropy.fastchunkgen.base.common.ModuleMixinPlugin;

public class MixinPlugin extends ModuleMixinPlugin {

    @Override
    public boolean shouldApplyMixin(String targetClassName, String mixinClassName) {
        if (!super.shouldApplyMixin(targetClassName, mixinClassName)) return false;

        if (mixinClassName.startsWith("com.misanthropy.fastchunkgen.threading.chunkio.mixin.gc_free_serializer.")) {
            return com.misanthropy.fastchunkgen.rewrites.chunk_serializer.ModuleEntryPoint.enabled;
        }

        return true;
    }
}
