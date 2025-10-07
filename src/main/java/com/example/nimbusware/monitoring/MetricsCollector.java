package com.example.nimbusware.monitoring;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

/**
 * Advanced metrics collection system for monitoring performance and usage
 */
public class MetricsCollector {
    private static volatile MetricsCollector instance;
    private final Map<String, LongAdder> counters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> gauges = new ConcurrentHashMap<>();
    private final Map<String, TimingData> timers = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "MetricsCollector-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private MetricsCollector() {
        startPeriodicReporting();
    }
    
    public static MetricsCollector getInstance() {
        if (instance == null) {
            synchronized (MetricsCollector.class) {
                if (instance == null) {
                    instance = new MetricsCollector();
                }
            }
        }
        return instance;
    }
    
    /**
     * Increment a counter metric
     * @param name Metric name
     * @param value Value to add
     */
    public void incrementCounter(String name, long value) {
        counters.computeIfAbsent(name, k -> new LongAdder()).add(value);
    }
    
    /**
     * Increment a counter metric by 1
     * @param name Metric name
     */
    public void incrementCounter(String name) {
        incrementCounter(name, 1);
    }
    
    /**
     * Set a gauge metric value
     * @param name Metric name
     * @param value Current value
     */
    public void setGauge(String name, long value) {
        gauges.computeIfAbsent(name, k -> new AtomicLong()).set(value);
    }
    
    /**
     * Record timing data
     * @param name Timer name
     * @param duration Duration in milliseconds
     */
    public void recordTiming(String name, long duration) {
        timers.computeIfAbsent(name, k -> new TimingData()).record(duration);
    }
    
    /**
     * Time a block of code execution
     * @param name Timer name
     * @param runnable Code to execute
     */
    public void time(String name, Runnable runnable) {
        long start = System.currentTimeMillis();
        try {
            runnable.run();
        } finally {
            long duration = System.currentTimeMillis() - start;
            recordTiming(name, duration);
        }
    }
    
    /**
     * Get counter value
     * @param name Counter name
     * @return Current counter value
     */
    public long getCounter(String name) {
        LongAdder counter = counters.get(name);
        return counter != null ? counter.sum() : 0;
    }
    
    /**
     * Get gauge value
     * @param name Gauge name
     * @return Current gauge value
     */
    public long getGauge(String name) {
        AtomicLong gauge = gauges.get(name);
        return gauge != null ? gauge.get() : 0;
    }
    
    /**
     * Get timer statistics
     * @param name Timer name
     * @return Timer statistics or null if not found
     */
    public TimerStats getTimerStats(String name) {
        TimingData timer = timers.get(name);
        return timer != null ? timer.getStats() : null;
    }
    
    /**
     * Get all metrics as a map
     * @return Map of all metrics
     */
    public Map<String, Object> getAllMetrics() {
        Map<String, Object> allMetrics = new ConcurrentHashMap<>();
        
        // Add counters
        counters.forEach((name, counter) -> 
            allMetrics.put("counter." + name, counter.sum()));
        
        // Add gauges
        gauges.forEach((name, gauge) -> 
            allMetrics.put("gauge." + name, gauge.get()));
        
        // Add timer stats
        timers.forEach((name, timer) -> 
            allMetrics.put("timer." + name, timer.getStats()));
        
        return allMetrics;
    }
    
    /**
     * Reset all metrics
     */
    public void reset() {
        counters.clear();
        gauges.clear();
        timers.clear();
        Logger.info("All metrics reset");
    }
    
    private void startPeriodicReporting() {
        // Report metrics every 5 minutes
        scheduler.scheduleAtFixedRate(this::reportMetrics, 5, 5, TimeUnit.MINUTES);
        
        // Update system metrics every 30 seconds
        scheduler.scheduleAtFixedRate(this::updateSystemMetrics, 30, 30, TimeUnit.SECONDS);
    }
    
    private void reportMetrics() {
        try {
            Logger.info("=== METRICS REPORT ===");
            
            // Report counters
            counters.forEach((name, counter) -> {
                long value = counter.sum();
                if (value > 0) {
                    Logger.info("Counter " + name + ": " + value);
                }
            });
            
            // Report gauges
            gauges.forEach((name, gauge) -> {
                long value = gauge.get();
                Logger.info("Gauge " + name + ": " + value);
            });
            
            // Report timer stats
            timers.forEach((name, timer) -> {
                TimerStats stats = timer.getStats();
                if (stats.getCount() > 0) {
                    Logger.info("Timer " + name + ": " + stats);
                }
            });
            
            Logger.info("=====================");
        } catch (Exception e) {
            Logger.error("Error reporting metrics", e);
        }
    }
    
    private void updateSystemMetrics() {
        try {
            Runtime runtime = Runtime.getRuntime();
            
            // Memory metrics
            setGauge("memory.used", runtime.totalMemory() - runtime.freeMemory());
            setGauge("memory.free", runtime.freeMemory());
            setGauge("memory.total", runtime.totalMemory());
            setGauge("memory.max", runtime.maxMemory());
            
            // Thread metrics
            setGauge("threads.active", Thread.activeCount());
            
            // GC metrics
            long gcCount = 0;
            for (java.lang.management.GarbageCollectorMXBean gc : 
                 java.lang.management.ManagementFactory.getGarbageCollectorMXBeans()) {
                gcCount += gc.getCollectionCount();
            }
            setGauge("gc.collections", gcCount);
            
        } catch (Exception e) {
            Logger.error("Error updating system metrics", e);
        }
    }
    
    /**
     * Shutdown the metrics collector
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    private static class TimingData {
        private final LongAdder totalTime = new LongAdder();
        private final LongAdder count = new LongAdder();
        private final AtomicLong min = new AtomicLong(Long.MAX_VALUE);
        private final AtomicLong max = new AtomicLong(0);
        
        public void record(long duration) {
            totalTime.add(duration);
            count.increment();
            
            // Update min
            long currentMin = min.get();
            while (duration < currentMin && !min.compareAndSet(currentMin, duration)) {
                currentMin = min.get();
            }
            
            // Update max
            long currentMax = max.get();
            while (duration > currentMax && !max.compareAndSet(currentMax, duration)) {
                currentMax = max.get();
            }
        }
        
        public TimerStats getStats() {
            long total = totalTime.sum();
            long cnt = count.sum();
            long minVal = min.get() == Long.MAX_VALUE ? 0 : min.get();
            long maxVal = max.get();
            
            return new TimerStats(total, cnt, minVal, maxVal);
        }
    }
    
    public static class TimerStats {
        private final long totalTime;
        private final long count;
        private final long min;
        private final long max;
        
        public TimerStats(long totalTime, long count, long min, long max) {
            this.totalTime = totalTime;
            this.count = count;
            this.min = min;
            this.max = max;
        }
        
        public long getTotalTime() { return totalTime; }
        public long getCount() { return count; }
        public long getMin() { return min; }
        public long getMax() { return max; }
        public double getAverage() { return count > 0 ? (double) totalTime / count : 0; }
        
        @Override
        public String toString() {
            return String.format("count=%d, total=%dms, avg=%.2fms, min=%dms, max=%dms", 
                count, totalTime, getAverage(), min, max);
        }
    }
}