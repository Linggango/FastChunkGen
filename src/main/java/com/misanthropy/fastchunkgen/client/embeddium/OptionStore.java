package com.misanthropy.fastchunkgen.client.embeddium;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;
import me.jellysquid.mods.sodium.client.gui.options.storage.OptionStorage;

public class OptionStore implements OptionStorage<Void> {

    public static final OptionStore INSTANCE = new OptionStore();

    @Override
    public Void getData() {
        return null;
    }

    @Override
    public void save() {
        ConfigSystem.saveToDisk();
    }
}
