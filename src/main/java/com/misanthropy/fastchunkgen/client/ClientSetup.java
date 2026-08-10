package com.misanthropy.fastchunkgen.client;

import com.misanthropy.fastchunkgen.FastChunkGen;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.ModList;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ClientSetup {

    public static void init() {
        if (FMLEnvironment.dist != Dist.CLIENT) return;
        if (!ModList.get().isLoaded("embeddium")) return;
        try {
            com.misanthropy.fastchunkgen.client.embeddium.EmbeddiumCompat.register();
            FastChunkGen.LOGGER.info("Registered options page in Embeddium video settings");
        } catch (Throwable t) {
            FastChunkGen.LOGGER.warn("Could not register the Embeddium options page", t);
        }
    }
}
