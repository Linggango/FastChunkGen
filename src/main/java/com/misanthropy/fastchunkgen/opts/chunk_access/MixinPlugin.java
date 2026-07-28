package com.misanthropy.fastchunkgen.opts.chunk_access;

import com.misanthropy.fastchunkgen.base.common.ModuleMixinPlugin;
import com.misanthropy.fastchunkgen.opts.chunk_access.asm.ASMTransformerLithiumChunkAccessWorkaround;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

public class MixinPlugin extends ModuleMixinPlugin {

    @Override
    public void postApply(String targetClassName, ClassNode targetClass, String mixinClassName, IMixinInfo mixinInfo) {
        super.postApply(targetClassName, targetClass, mixinClassName, mixinInfo);
//        ASMTransformerLithiumChunkAccessWorkaround.transform(targetClass); // SJhub - I don't need compat patch with lithium.
    }

}
