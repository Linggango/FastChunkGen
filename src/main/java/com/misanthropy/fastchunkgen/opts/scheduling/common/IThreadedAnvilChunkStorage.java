package com.misanthropy.fastchunkgen.opts.scheduling.common;

import java.util.concurrent.Executor;

public interface IThreadedAnvilChunkStorage {

    Executor getMainInvokingExecutor();

}
