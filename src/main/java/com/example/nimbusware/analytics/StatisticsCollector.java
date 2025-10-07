package com.example.nimbusware.analytics;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAdder;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.List;
import java.util.ArrayList;

/**
 * Advanced statistics collection system for tracking usage and performance
 */
public class StatisticsCollector {
    private static volatile StatisticsCollector instance;
    private final Map<String, LongAdder> counters = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> gauges = new ConcurrentHashMap<>();
    private final Map<String, List<Long>> timers = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> maxValues = new ConcurrentHashMap<>();
    private final Map<String, AtomicLong> minValues = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "StatisticsCollector-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private StatisticsCollector() {
        startPeriodicReporting();
    }
    
    public static StatisticsCollector getInstance() {
        if (instance == null) {
            synchronized (StatisticsCollector.class) {
                if (instance == null) {
                    instance = new StatisticsCollector();
                }
            }
        }
        return instance;
    }
    
    /**
     * Increment a counter
     * @param name Counter name
     * @param value Value to add
     */
    public void incrementCounter(String name, long value) {
        counters.computeIfAbsent(name, k -> new LongAdder()).add(value);
    }
    
    /**
     * Increment a counter by 1
     * @param name Counter name
     */
    public void incrementCounter(String name) {
        incrementCounter(name, 1);
    }
    
    /**
     * Set a gauge value
     * @param name Gauge name
     * @param value Current value
     */
    public void setGauge(String name, long value) {
        gauges.computeIfAbsent(name, k -> new AtomicLong()).set(value);
        
        // Update min/max values
        updateMinMax(name, value);
    }
    
    /**
     * Record a timing measurement
     * @param name Timer name
     * @param duration Duration in milliseconds
     */
    public void recordTiming(String name, long duration) {
        timers.computeIfAbsent(name, k -> new ArrayList<>()).add(duration);
        
        // Keep only last 1000 measurements to prevent memory issues
        List<Long> measurements = timers.get(name);
        if (measurements.size() > 1000) {
            measurements.subList(0, measurements.size() - 1000).clear();
        }
        
        // Update min/max values
        updateMinMax(name + ".timing", duration);
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
     * Record module usage
     * @param moduleName Module name
     * @param duration Usage duration in milliseconds
     */
    public void recordModuleUsage(String moduleName, long duration) {
        incrementCounter("modules." + moduleName + ".activations");
        recordTiming("modules." + moduleName + ".usage", duration);
    }
    
    /**
     * Record command execution
     * @param command Command name
     * @param success Whether command succeeded
     */
    public void recordCommand(String command, boolean success) {
        incrementCounter("commands." + command + (success ? ".success" : ".failure"));
        incrementCounter("commands.total");
    }
    
    /**
     * Record error occurrence
     * @param errorType Error type
     * @param severity Error severity
     */
    public void recordError(String errorType, String severity) {
        incrementCounter("errors." + errorType + "." + severity);
        incrementCounter("errors.total");
    }
    
    /**
     * Record performance metric
     * @param metricName Metric name
     * @param value Metric value
     */
    public void recordPerformance(String metricName, long value) {
        setGauge("performance." + metricName, value);
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
     * Get timing statistics
     * @param name Timer name
     * @return Timing statistics
     */
    public TimingStatistics getTimingStatistics(String name) {
        List<Long> measurements = timers.get(name);
        if (measurements == null || measurements.isEmpty()) {
            return new TimingStatistics(0, 0, 0, 0, 0);
        }
        
        long total = 0;
        long min = Long.MAX_VALUE;
        long max = Long.MIN_VALUE;
        
        for (Long measurement : measurements) {
            total += measurement;
            min = Math.min(min, measurement);
            max = Math.max(max, measurement);
        }
        
        double average = (double) total / measurements.size();
        
        return new TimingStatistics(total, measurements.size(), min, max, average);
    }
    
    /**
     * Get comprehensive statistics report
     * @return Statistics report
     */
    public StatisticsReport getStatisticsReport() {
        StatisticsReport report = new StatisticsReport();
        
        // Add counters
        counters.forEach((name, counter) -> {
            report.addCounter(name, counter.sum());
        });
        
        // Add gauges
        gauges.forEach((name, gauge) -> {
            report.addGauge(name, gauge.get());
        });
        
        // Add timing statistics
        timers.forEach((name, measurements) -> {
            if (!measurements.isEmpty()) {
                report.addTimingStatistics(name, getTimingStatistics(name));
            }
        });
        
        return report;
    }
    
    /**
     * Get module usage statistics
     * @param moduleName Module name
     * @return Module usage statistics
     */
    public ModuleUsageStatistics getModuleUsageStatistics(String moduleName) {
        long activations = getCounter("modules." + moduleName + ".activations");
        TimingStatistics usage = getTimingStatistics("modules." + moduleName + ".usage");
        
        return new ModuleUsageStatistics(moduleName, activations, usage);
    }
    
    /**
     * Get all module usage statistics
     * @return Map of module names to usage statistics
     */
    public Map<String, ModuleUsageStatistics> getAllModuleUsageStatistics() {
        Map<String, ModuleUsageStatistics> stats = new ConcurrentHashMap<>();
        
        counters.keySet().stream()
            .filter(name -> name.startsWith("modules.") && name.endsWith(".activations"))
            .forEach(name -> {
                String moduleName = name.substring(8, name.lastIndexOf('.'));
                stats.put(moduleName, getModuleUsageStatistics(moduleName));
            });
        
        return stats;
    }
    
    private void updateMinMax(String name, long value) {
        maxValues.computeIfAbsent(name, k -> new AtomicLong(Long.MIN_VALUE))
            .updateAndGet(current -> Math.max(current, value));
        
        minValues.computeIfAbsent(name, k -> new AtomicLong(Long.MAX_VALUE))
            .updateAndGet(current -> Math.min(current, value));
    }
    
    private void startPeriodicReporting() {
        // Generate statistics report every 10 minutes
        scheduler.scheduleAtFixedRate(this::generateReport, 10, 10, TimeUnit.MINUTES);
        
        // Clean up old timing data every hour
        scheduler.scheduleAtFixedRate(this::cleanupOldData, 1, 1, TimeUnit.HOURS);
    }
    
    private void generateReport() {
        try {
            StatisticsReport report = getStatisticsReport();
            Logger.info("=== STATISTICS REPORT ===");
            Logger.info("Counters: " + report.getCounters().size());
            Logger.info("Gauges: " + report.getGauges().size());
            Logger.info("Timers: " + report.getTimingStatistics().size());
            
            // Log top modules by usage
            Map<String, ModuleUsageStatistics> moduleStats = getAllModuleUsageStatistics();
            moduleStats.entrySet().stream()
                .sorted((a, b) -> Long.compare(b.getValue().getActivations(), a.getValue().getActivations()))
                .limit(5)
                .forEach(entry -> {
                    ModuleUsageStatistics stats = entry.getValue();
                    Logger.info("Module " + entry.getKey() + ": " + stats.getActivations() + " activations, " +
                        String.format("%.2f", stats.getUsageStatistics().getAverage()) + "ms avg usage");
                });
            
            Logger.info("=========================");
        } catch (Exception e) {
            Logger.error("Error generating statistics report", e);
        }
    }
    
    private void cleanupOldData() {
        // Keep only last 100 measurements for each timer
        timers.forEach((name, measurements) -> {
            if (measurements.size() > 100) {
                measurements.subList(0, measurements.size() - 100).clear();
            }
        });
    }
    
    /**
     * Reset all statistics
     */
    public void reset() {
        counters.clear();
        gauges.clear();
        timers.clear();
        maxValues.clear();
        minValues.clear();
        Logger.info("All statistics reset");
    }
    
    /**
     * Shutdown the statistics collector
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
    
    /**
     * Timing statistics container
     */
    public static class TimingStatistics {
        private final long total;
        private final long count;
        private final long min;
        private final long max;
        private final double average;
        
        public TimingStatistics(long total, long count, long min, long max, double average) {
            this.total = total;
            this.count = count;
            this.min = min;
            this.max = max;
            this.average = average;
        }
        
        public long getTotal() { return total; }
        public long getCount() { return count; }
        public long getMin() { return min; }
        public long getMax() { return max; }
        public double getAverage() { return average; }
        
        @Override
        public String toString() {
            return String.format("count=%d, total=%dms, avg=%.2fms, min=%dms, max=%dms", 
                count, total, average, min, max);
        }
    }
    
    /**
     * Module usage statistics container
     */
    public static class ModuleUsageStatistics {
        private final String moduleName;
        private final long activations;
        private final TimingStatistics usageStatistics;
        
        public ModuleUsageStatistics(String moduleName, long activations, TimingStatistics usageStatistics) {
            this.moduleName = moduleName;
            this.activations = activations;
            this.usageStatistics = usageStatistics;
        }
        
        public String getModuleName() { return moduleName; }
        public long getActivations() { return activations; }
        public TimingStatistics getUsageStatistics() { return usageStatistics; }
    }
    
    /**
     * Comprehensive statistics report
     */
    public static class StatisticsReport {
        private final Map<String, Long> counters = new ConcurrentHashMap<>();
        private final Map<String, Long> gauges = new ConcurrentHashMap<>();
        private final Map<String, TimingStatistics> timingStatistics = new ConcurrentHashMap<>();
        
        public void addCounter(String name, long value) {
            counters.put(name, value);
        }
        
        public void addGauge(String name, long value) {
            gauges.put(name, value);
        }
        
        public void addTimingStatistics(String name, TimingStatistics stats) {
            timingStatistics.put(name, stats);
        }
        
        public Map<String, Long> getCounters() { return counters; }
        public Map<String, Long> getGauges() { return gauges; }
        public Map<String, TimingStatistics> getTimingStatistics() { return timingStatistics; }
    }
}