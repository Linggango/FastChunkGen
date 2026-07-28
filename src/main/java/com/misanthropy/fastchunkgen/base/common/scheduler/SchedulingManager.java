package com.misanthropy.fastchunkgen.base.common.scheduler;

import com.misanthropy.fastchunkgen.base.common.structs.DynamicPriorityQueue;
import it.unimi.dsi.fastutil.longs.Long2IntOpenHashMap;
import it.unimi.dsi.fastutil.longs.Long2ReferenceOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArraySet;
import net.minecraft.server.level.ChunkLevel;
import net.minecraft.world.level.ChunkPos;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class SchedulingManager {

    private static final Logger LOGGER = LoggerFactory.getLogger("FastChunkGen Scheduling Manager");

    public static final int MAX_LEVEL = ChunkLevel.MAX_LEVEL + 1;
    private final DynamicPriorityQueue<ScheduledTask> queue = new DynamicPriorityQueue<>(MAX_LEVEL + 1);
    private final Long2ReferenceOpenHashMap<ObjectArraySet<ScheduledTask>> pos2Tasks = new Long2ReferenceOpenHashMap<>();
    private final Long2IntOpenHashMap prioritiesFromLevel = new Long2IntOpenHashMap();
    private final NeighborLockingManager neighborLockingManager = new NeighborLockingManager();
    private final AtomicInteger scheduledCount = new AtomicInteger(0);
    private final AtomicBoolean scheduled = new AtomicBoolean(false);
    private ChunkPos currentSyncLoad = null;

    private final Executor executor;
    private final int maxScheduled;

    {
        prioritiesFromLevel.defaultReturnValue(MAX_LEVEL);
    }

    public SchedulingManager(Executor executor, int maxScheduled) {
        this.executor = executor;
        this.maxScheduled = maxScheduled;
    }

    private void submitGuarded(Runnable runnable) {
        this.executor.execute(() -> {
            try {
                runnable.run();
            } catch (Throwable t) {
                LOGGER.error("Error while running chunk scheduling task", t);
            }
        });
    }

    public void enqueue(ScheduledTask task) {
        submitGuarded(() -> {
            if (task.isAsync()) {
                schedule0(task);
            } else {
                queue.enqueue(task, prioritiesFromLevel.get(task.centerPos()));
                pos2Tasks.computeIfAbsent(task.centerPos(), unused -> new ObjectArraySet<>()).add(task);
                scheduleExecution();
            }
        });
    }

    public void updatePriorityFromLevel(long pos, int level) {
        submitGuarded(() -> {
            if (prioritiesFromLevel.get(pos) == level) return;
            if (level < MAX_LEVEL) {
                prioritiesFromLevel.put(pos, level);
            } else {
                prioritiesFromLevel.remove(pos);
            }
            updatePriorityInternal(pos);
        });
    }

    private void updatePriorityInternal(long pos) {
        int fromLevel = prioritiesFromLevel.get(pos);
        int fromSyncLoad;
        if (currentSyncLoad != null) {
            final int chebyshevDistance = chebyshev(new ChunkPos(pos), currentSyncLoad);
            if (chebyshevDistance <= 8) {
                fromSyncLoad = chebyshevDistance;
//                System.out.println("dist for chunk [%d,%d] is %d".formatted(currentSyncLoad.x, currentSyncLoad.z, chebyshevDistance));
            } else {
                fromSyncLoad = MAX_LEVEL;
            }
        } else {
            fromSyncLoad = MAX_LEVEL;
        }
        int priority = Math.min(fromLevel, fromSyncLoad);
        final ObjectArraySet<ScheduledTask> locks = this.pos2Tasks.get(pos);
        if (locks != null) {
            for (ScheduledTask lock : locks) {
                queue.changePriority(lock, priority);
            }
        }
    }

    public void setCurrentSyncLoad(ChunkPos pos) {
        submitGuarded(() -> {
            if (this.currentSyncLoad != null) {
                final ChunkPos lastSyncLoad = this.currentSyncLoad;
                this.currentSyncLoad = null;
                updateSyncLoadInternal(lastSyncLoad);
            }
            if (pos != null) {
                this.currentSyncLoad = pos;
                updateSyncLoadInternal(pos);
            }
        });
    }

    public NeighborLockingManager getNeighborLockingManager() {
        return this.neighborLockingManager;
    }

    public Executor getExecutor() {
        return executor;
    }

    private void updateSyncLoadInternal(ChunkPos pos) {
        long startTime = System.nanoTime();
        for (int xOff = -8; xOff <= 8; xOff++) {
            for (int zOff = -8; zOff <= 8; zOff++) {
                updatePriorityInternal(ChunkPos.asLong(pos.x + xOff, pos.z + zOff));
            }
        }
        long endTime = System.nanoTime();
    }

    private void scheduleExecution() {
        if (scheduledCount.get() < maxScheduled && scheduled.compareAndSet(false, true)) {
            this.executor.execute(() -> {
                try {
                    ScheduleStatus status;
                    while (scheduledCount.get() < maxScheduled && (status = scheduleExecutionInternal()).success) {
                        if (!status.async) scheduledCount.incrementAndGet();
                    }
                } catch (Throwable t) {
                    LOGGER.error("Error while scheduling chunk tasks", t);
                } finally {
                    scheduled.set(false);
                }
            });
        }
    }

    private ScheduleStatus scheduleExecutionInternal() {
        final ScheduledTask task = queue.dequeue();
        if (task != null) {
            final ObjectArraySet<ScheduledTask> tasks = this.pos2Tasks.get(task.centerPos());
            if (tasks != null) tasks.remove(task);
            runPos2TasksMaintenance(task.centerPos());
            boolean scheduled1;
            try {
                scheduled1 = schedule0(task);
            } catch (Throwable t) {
                LOGGER.error("Error while preparing chunk task at {}", new ChunkPos(task.centerPos()), t);
                return ScheduleStatus.NOT_SCHEDULED;
            }
            if (scheduled1) return ScheduleStatus.SCHEDULED;
        }
        return ScheduleStatus.NOT_SCHEDULED;
    }

    private boolean schedule0(ScheduledTask task) {
        if (task.tryPrepare()) {
            task.runTask(() -> {
                scheduledCount.decrementAndGet();
                scheduleExecution();
            });
            return true;
        }
        return false;
    }

    private static int chebyshev(ChunkPos a, ChunkPos b) {
        return Math.max(Math.abs(a.x - b.x), Math.abs(a.z - b.z));
    }

    private static int chebyshev(long a, long b) {
        return Math.max(Math.abs(ChunkPos.getX(a) - ChunkPos.getX(b)), Math.abs(ChunkPos.getZ(a) - ChunkPos.getZ(b)));
    }

    private enum ScheduleStatus {
        SCHEDULED(true, false),
        SCHEDULED_ASYNC(true, true),
        NOT_SCHEDULED(false, false);

        public final boolean success;
        public final boolean async;

        ScheduleStatus(boolean success, boolean async) {
            this.success = success;
            this.async = async;
        }
    };

    private void runPos2TasksMaintenance(long pos) {
        final ObjectArraySet<ScheduledTask> locks = this.pos2Tasks.get(pos);
        if (locks != null && locks.isEmpty()) {
            this.pos2Tasks.remove(pos);
        }
    }

}
