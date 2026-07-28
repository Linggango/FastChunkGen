package com.misanthropy.fastchunkgen.opts.scheduling.common;

import net.minecraft.server.level.ServerLevel;

public interface ServerMidTickTask {

    void executeTasksMidTick(ServerLevel world);

}
