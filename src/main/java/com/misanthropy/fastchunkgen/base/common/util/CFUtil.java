package com.misanthropy.fastchunkgen.base.common.util;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.TimeUnit;

public class CFUtil {

    private static final long PARK_NANOS = 100_000_000L;

    public static <T> T join(CompletableFuture<T> future) {
        await(future, -1L);
        return future.join();
    }

    public static boolean await(CompletableFuture<?> future, long timeoutNanos) {
        if (future.isDone()) return true;
        final FutureBlocker blocker = new FutureBlocker(future, timeoutNanos);
        try {
            ForkJoinPool.managedBlock(blocker);
        } catch (InterruptedException e) {
            blocker.interrupted = true;
        } finally {
            if (blocker.interrupted) Thread.currentThread().interrupt();
        }
        return future.isDone();
    }

    private static final class FutureBlocker implements ForkJoinPool.ManagedBlocker {

        private final CompletableFuture<?> future;
        private final boolean bounded;
        private final long deadline;
        private boolean interrupted = false;

        private FutureBlocker(CompletableFuture<?> future, long timeoutNanos) {
            this.future = future;
            this.bounded = timeoutNanos >= 0L;
            this.deadline = this.bounded ? System.nanoTime() + timeoutNanos : 0L;
        }

        @Override
        public boolean block() {
            long park = PARK_NANOS;
            if (this.bounded) {
                final long remaining = this.deadline - System.nanoTime();
                if (remaining <= 0L) return true;
                park = Math.min(park, remaining);
            }
            try {
                this.future.get(park, TimeUnit.NANOSECONDS);
            } catch (InterruptedException e) {
                this.interrupted = true;
            } catch (Throwable ignored) {
            }
            return isReleasable();
        }

        @Override
        public boolean isReleasable() {
            return this.future.isDone() || (this.bounded && this.deadline - System.nanoTime() <= 0L);
        }
    }

}
