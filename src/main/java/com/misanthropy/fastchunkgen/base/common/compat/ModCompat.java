package com.misanthropy.fastchunkgen.base.common.compat;

import com.misanthropy.fastchunkgen.base.common.util.ModUtil;
import net.minecraftforge.fml.loading.FMLPaths;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;

public class ModCompat {

    private static final Logger LOGGER = LoggerFactory.getLogger("FastChunkGen Compat");

    private static final String[] LITHIUM_FAMILY = {"lithium", "radium", "canary"};

    private static Boolean lithiumFamily;

    private static Properties modernFixMixins;

    public static boolean isLithiumFamilyPresent() {
        if (lithiumFamily == null) {
            boolean present = false;
            for (String id : LITHIUM_FAMILY) {
                if (ModUtil.isModLoaded(id)) {
                    present = true;
                    break;
                }
            }
            lithiumFamily = present;
        }
        return lithiumFamily;
    }

    public static boolean isModernFixPresent() {
        return ModUtil.isModLoaded("modernfix");
    }

    private static Properties modernFixMixins() {
        if (modernFixMixins == null) {
            final Properties properties = new Properties();
            try {
                final Path path = FMLPaths.CONFIGDIR.get().resolve("modernfix-mixins.properties");
                if (Files.isRegularFile(path)) {
                    try (InputStream in = Files.newInputStream(path)) {
                        properties.load(in);
                    }
                }
            } catch (Throwable t) {
                LOGGER.warn("Could not read modernfix-mixins.properties, assuming ModernFix defaults", t);
            }
            modernFixMixins = properties;
        }
        return modernFixMixins;
    }

    public static boolean isModernFixFeatureEnabled(String key) {
        if (!isModernFixPresent()) return false;
        final String value = modernFixMixins().getProperty(key);
        return value == null || !value.equalsIgnoreCase("false");
    }
}
