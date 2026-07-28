package com.misanthropy.fastchunkgen.base.common.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.locks.LockSupport;

public class CFUtil {

    public static <T> T join(CompletableFuture<T> future) {
        if (future.isDone()) return future.join();
        final Thread current = Thread.currentThread();
        future.whenComplete((result, throwable) -> LockSupport.unpark(current));
        boolean interrupted = false;
        try {
            while (!future.isDone()) {
                LockSupport.parkNanos("Waiting for future", 100_000_000L);
                if (Thread.interrupted()) interrupted = true;
            }
        } finally {
            if (interrupted) current.interrupt();
        }
        return future.join();
    }

}
