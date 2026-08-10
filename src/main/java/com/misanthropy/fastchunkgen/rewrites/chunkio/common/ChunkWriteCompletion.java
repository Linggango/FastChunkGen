package com.misanthropy.fastchunkgen.rewrites.chunkio.common;

import java.util.concurrent.CompletableFuture;

public class ChunkWriteCompletion {

    private static final ThreadLocal<CompletableFuture<Void>> LAST_WRITE = new ThreadLocal<>();

    public static void set(CompletableFuture<Void> future) {
        LAST_WRITE.set(future);
    }

    public static CompletableFuture<Void> take() {
        final CompletableFuture<Void> future = LAST_WRITE.get();
        LAST_WRITE.remove();
        return future != null ? future : CompletableFuture.completedFuture(null);
    }

}
