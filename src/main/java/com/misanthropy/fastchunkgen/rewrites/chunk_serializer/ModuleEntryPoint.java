package com.misanthropy.fastchunkgen.rewrites.chunk_serializer;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public final class ModuleEntryPoint {

    @SuppressWarnings("unused")
    public static final boolean enabled = new ConfigSystem.ConfigAccessor()
            .key("ioSystem.gcFreeChunkSerializer")
            .comment("Allocation-free chunk writer. Cannot fire Forge ChunkDataEvent.Save, so mods lose their per-chunk data. Back up first.")
            .incompatibleMod("architectury", "*")
            .getBoolean(false, false);

}
