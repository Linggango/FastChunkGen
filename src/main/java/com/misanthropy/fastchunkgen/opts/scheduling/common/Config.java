package com.misanthropy.fastchunkgen.opts.scheduling.common;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;

public class Config {

    public static final long midTickChunkTasksInterval = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.midTickChunkTasksInterval")
            .comment("Nanoseconds between chunk tasks run inside the tick loop. -1 disables.")
            .incompatibleMod("dimthread", "*")
            .getLong(100_000, -1);

    public static final AutoSaveMode autoSaveMode = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.autoSave.mode")
            .comment("VANILLA, ENHANCED (save during idle time) or PERIODIC (every 6000 ticks). Keep the quotes.")
            .getEnum(AutoSaveMode.class, AutoSaveMode.VANILLA, AutoSaveMode.VANILLA);

    public static final long autoSaveDelayMillis = new ConfigSystem.ConfigAccessor()
            .key("generalOptimizations.autoSave.delay")
            .comment("Milliseconds before a dirty chunk is auto-saved. ENHANCED only.")
            .getLong(20000,20000);

    public static void init() {
    }


    public enum AutoSaveMode {
        VANILLA(false, false),
        ENHANCED(true, true),
        PERIODIC(true, false);

        public final boolean disableVanillaMidTickAutoSave;
        public final boolean enableEnhancedAutoSave;

        AutoSaveMode(boolean disableVanillaMidTickAutoSave, boolean enableEnhancedAutoSave) {
            this.disableVanillaMidTickAutoSave = disableVanillaMidTickAutoSave;
            this.enableEnhancedAutoSave = enableEnhancedAutoSave;
        }
    }

}
