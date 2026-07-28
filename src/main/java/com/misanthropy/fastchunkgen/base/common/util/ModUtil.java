package com.misanthropy.fastchunkgen.base.common.util;

import net.minecraftforge.fml.loading.FMLLoader;
import net.minecraftforge.fml.loading.moddiscovery.ModFileInfo;
import net.minecraftforge.fml.loading.moddiscovery.ModInfo;
import net.minecraftforge.forgespi.language.IModInfo;
import org.apache.maven.artifact.versioning.ArtifactVersion;
import org.apache.maven.artifact.versioning.DefaultArtifactVersion;

import java.util.List;

public class ModUtil {

    public static boolean isModLoaded(String modId) {
        try {
            return FMLLoader.getLoadingModList() != null && FMLLoader.getLoadingModList().getModFileById(modId) != null;
        } catch (Throwable t) {
            return false;
        }
    }

    public static ArtifactVersion getModVersion(String modId) {
        try {
            final ModFileInfo info = FMLLoader.getLoadingModList().getModFileById(modId);
            if (info == null) return new DefaultArtifactVersion("0.0.0");
            final List<IModInfo> mods = info.getMods();
            for (IModInfo mod : mods) {
                if (mod.getModId().equals(modId)) return mod.getVersion();
            }
            return mods.isEmpty() ? new DefaultArtifactVersion("0.0.0") : mods.get(0).getVersion();
        } catch (Throwable t) {
            return new DefaultArtifactVersion("0.0.0");
        }
    }

    public static List<ModInfo> getModInfoList() {
        try {
            return FMLLoader.getLoadingModList().getMods();
        } catch (Throwable t) {
            return List.of();
        }
    }
}
