package com.misanthropy.fastchunkgen.client.embeddium;

import com.misanthropy.fastchunkgen.FastChunkGen;
import org.embeddedt.embeddium.api.OptionGUIConstructionEvent;

public class EmbeddiumCompat {

    private static boolean failed = false;

    public static void register() {
        OptionGUIConstructionEvent.BUS.addListener(event -> {
            if (failed) return;
            try {
                event.addPage(new FastChunkGenPage());
            } catch (Throwable t) {
                failed = true;
                FastChunkGen.LOGGER.error("Could not build the FastChunkGen options page, it will not be shown", t);
            }
        });
    }
}
