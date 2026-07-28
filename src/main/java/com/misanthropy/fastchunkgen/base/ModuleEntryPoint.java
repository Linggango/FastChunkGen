package com.misanthropy.fastchunkgen.base;

import com.misanthropy.fastchunkgen.base.common.config.ConfigSystem;
import io.netty.util.internal.PlatformDependent;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.loading.FMLEnvironment;

public class ModuleEntryPoint {

    private static final boolean enabled = true;

    public static final boolean disableLoggingShutdownHook = new ConfigSystem.ConfigAccessor()
            .key("fixes.disableLoggingShutdownHook")
            .comment("Remove the log4j2 shutdown hook so shutdown logs survive, and force exit if a dedicated server hangs.")
            .incompatibleMod("textile_backup", "*")
            .getBoolean(true, false);

    public static final int defaultParallelism;

    public static final long globalExecutorParallelism;

    private static boolean isClientSide() {
        return FMLEnvironment.dist == Dist.CLIENT;
    }

    private static int envTypeOffset() {
        return isClientSide() ? -2 : 0;
    }

    private static int parallelismFromCpu() {
        final int cpus = Runtime.getRuntime().availableProcessors();
        final double raw = PlatformDependent.isWindows() ? (cpus / 1.6 - 2) : (cpus / 1.2 - 2);
        return (int) raw + envTypeOffset();
    }

    private static int parallelismFromHeap() {
        final double memGiB = Runtime.getRuntime().maxMemory() / 1024.0 / 1024.0 / 1024.0;
        final double raw;
        if (PlatformDependent.isJ9Jvm()) {
            raw = (memGiB - (isClientSide() ? 0.6 : 0.2)) / 0.5;
        } else {
            raw = (memGiB - (isClientSide() ? 1.2 : 0.6)) / 1.2;
        }
        return (int) raw + envTypeOffset();
    }

    private static int computeDefaultParallelism() {
        return Math.max(1, Math.min(parallelismFromCpu(), parallelismFromHeap()));
    }

    static {
        int value;
        try {
            value = computeDefaultParallelism();
        } catch (Throwable t) {
            ConfigSystem.LOGGER.error("Failed to compute default global executor parallelism, falling back to 1", t);
            value = 1;
        }

        defaultParallelism = value;
        globalExecutorParallelism = new ConfigSystem.ConfigAccessor()
                .key("globalExecutorParallelism")
                .comment("Worker threads used for chunk generation and IO.")
                .getLong(value, value, ConfigSystem.LongChecks.THREAD_COUNT);
    }
}
