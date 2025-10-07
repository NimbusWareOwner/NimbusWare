package com.example.nimbusware.utils;

import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Manager for asynchronous task execution
 */
public class AsyncTaskManager {
    private static final int CORE_POOL_SIZE = 2;
    private static final int MAX_POOL_SIZE = 4;
    private static final long KEEP_ALIVE_TIME = 60L;
    private static final int QUEUE_CAPACITY = 100;
    
    private final ThreadPoolExecutor executor;
    private final ScheduledExecutorService scheduledExecutor;
    private final AtomicInteger taskCounter = new AtomicInteger(0);
    
    private static volatile AsyncTaskManager instance;
    
    private AsyncTaskManager() {
        // Create thread pool with custom thread factory
        this.executor = new ThreadPoolExecutor(
            CORE_POOL_SIZE,
            MAX_POOL_SIZE,
            KEEP_ALIVE_TIME,
            TimeUnit.SECONDS,
            new LinkedBlockingQueue<>(QUEUE_CAPACITY),
            r -> {
                Thread t = new Thread(r, "AsyncTask-" + taskCounter.incrementAndGet());
                t.setDaemon(true);
                return t;
            },
            new ThreadPoolExecutor.CallerRunsPolicy() // Fallback to caller thread if queue is full
        );
        
        // Create scheduled executor
        this.scheduledExecutor = Executors.newScheduledThreadPool(2, r -> {
            Thread t = new Thread(r, "ScheduledTask-" + taskCounter.incrementAndGet());
            t.setDaemon(true);
            return t;
        });
    }
    
    /**
     * Get singleton instance
     * @return AsyncTaskManager instance
     */
    public static AsyncTaskManager getInstance() {
        if (instance == null) {
            synchronized (AsyncTaskManager.class) {
                if (instance == null) {
                    instance = new AsyncTaskManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Execute task asynchronously
     * @param task Task to execute
     * @return Future representing the task
     */
    public Future<?> execute(Runnable task) {
        return executor.submit(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Logger.error("Error in async task", e);
            }
        });
    }
    
    /**
     * Execute task asynchronously with result
     * @param task Task to execute
     * @param <T> Result type
     * @return Future representing the task result
     */
    public <T> Future<T> execute(Callable<T> task) {
        return executor.submit(() -> {
            try {
                return task.call();
            } catch (Exception e) {
                Logger.error("Error in async task", e);
                throw e;
            }
        });
    }
    
    /**
     * Schedule task to run after delay
     * @param task Task to execute
     * @param delay Delay before execution
     * @param unit Time unit for delay
     * @return ScheduledFuture representing the task
     */
    public ScheduledFuture<?> schedule(Runnable task, long delay, TimeUnit unit) {
        return scheduledExecutor.schedule(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Logger.error("Error in scheduled task", e);
            }
        }, delay, unit);
    }
    
    /**
     * Schedule task to run periodically
     * @param task Task to execute
     * @param initialDelay Initial delay before first execution
     * @param period Period between executions
     * @param unit Time unit
     * @return ScheduledFuture representing the task
     */
    public ScheduledFuture<?> scheduleAtFixedRate(Runnable task, long initialDelay, long period, TimeUnit unit) {
        return scheduledExecutor.scheduleAtFixedRate(() -> {
            try {
                task.run();
            } catch (Exception e) {
                Logger.error("Error in periodic task", e);
            }
        }, initialDelay, period, unit);
    }
    
    /**
     * Get current active thread count
     * @return Number of active threads
     */
    public int getActiveThreadCount() {
        return executor.getActiveCount();
    }
    
    /**
     * Get current queue size
     * @return Number of tasks in queue
     */
    public int getQueueSize() {
        return executor.getQueue().size();
    }
    
    /**
     * Get completed task count
     * @return Number of completed tasks
     */
    public long getCompletedTaskCount() {
        return executor.getCompletedTaskCount();
    }
    
    /**
     * Shutdown the task manager
     */
    public void shutdown() {
        Logger.info("Shutting down AsyncTaskManager...");
        
        executor.shutdown();
        scheduledExecutor.shutdown();
        
        try {
            if (!executor.awaitTermination(10, TimeUnit.SECONDS)) {
                Logger.warn("Forcing shutdown of thread pool");
                executor.shutdownNow();
            }
            
            if (!scheduledExecutor.awaitTermination(5, TimeUnit.SECONDS)) {
                Logger.warn("Forcing shutdown of scheduled executor");
                scheduledExecutor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            scheduledExecutor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        Logger.info("AsyncTaskManager shutdown complete");
    }
}