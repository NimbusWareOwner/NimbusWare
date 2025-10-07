package com.example.nimbusware.monitoring;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Advanced performance monitoring system
 */
public class PerformanceMonitor {
    private static volatile PerformanceMonitor instance;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "PerformanceMonitor-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private final AtomicLong totalOperations = new AtomicLong(0);
    private final AtomicLong successfulOperations = new AtomicLong(0);
    private final AtomicLong failedOperations = new AtomicLong(0);
    private final AtomicLong totalExecutionTime = new AtomicLong(0);
    
    private long startTime = System.currentTimeMillis();
    private volatile boolean monitoring = false;
    
    private PerformanceMonitor() {
        startMonitoring();
    }
    
    public static PerformanceMonitor getInstance() {
        if (instance == null) {
            synchronized (PerformanceMonitor.class) {
                if (instance == null) {
                    instance = new PerformanceMonitor();
                }
            }
        }
        return instance;
    }
    
    /**
     * Record operation execution
     * @param operationName Operation name
     * @param executionTime Execution time in milliseconds
     * @param success Whether operation was successful
     */
    public void recordOperation(String operationName, long executionTime, boolean success) {
        totalOperations.incrementAndGet();
        totalExecutionTime.addAndGet(executionTime);
        
        if (success) {
            successfulOperations.incrementAndGet();
        } else {
            failedOperations.incrementAndGet();
        }
        
        Logger.debug("Operation " + operationName + " took " + executionTime + "ms (" + 
                    (success ? "success" : "failed") + ")");
    }
    
    /**
     * Get performance statistics
     * @return Performance statistics
     */
    public PerformanceStats getPerformanceStats() {
        long total = totalOperations.get();
        long successful = successfulOperations.get();
        long failed = failedOperations.get();
        long totalTime = totalExecutionTime.get();
        long uptime = System.currentTimeMillis() - startTime;
        
        double successRate = total > 0 ? (double) successful / total : 0.0;
        double avgExecutionTime = total > 0 ? (double) totalTime / total : 0.0;
        double operationsPerSecond = uptime > 0 ? (double) total / (uptime / 1000.0) : 0.0;
        
        return new PerformanceStats(total, successful, failed, successRate, 
                                  avgExecutionTime, operationsPerSecond, uptime);
    }
    
    /**
     * Start monitoring
     */
    public void startMonitoring() {
        if (monitoring) return;
        
        monitoring = true;
        startTime = System.currentTimeMillis();
        
        // Monitor every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                logPerformanceStats();
            } catch (Exception e) {
                Logger.error("Error in performance monitoring", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
        
        Logger.info("Performance monitoring started");
    }
    
    /**
     * Stop monitoring
     */
    public void stopMonitoring() {
        monitoring = false;
        scheduler.shutdown();
        Logger.info("Performance monitoring stopped");
    }
    
    private void logPerformanceStats() {
        PerformanceStats stats = getPerformanceStats();
        
        Logger.info("=== PERFORMANCE STATS ===");
        Logger.info("Total Operations: " + stats.getTotalOperations());
        Logger.info("Success Rate: " + String.format("%.2f%%", stats.getSuccessRate() * 100));
        Logger.info("Avg Execution Time: " + String.format("%.2fms", stats.getAvgExecutionTime()));
        Logger.info("Operations/sec: " + String.format("%.2f", stats.getOperationsPerSecond()));
        Logger.info("Uptime: " + formatUptime(stats.getUptime()));
        Logger.info("=========================");
    }
    
    private String formatUptime(long uptimeMs) {
        long seconds = uptimeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    /**
     * Performance statistics container
     */
    public static class PerformanceStats {
        private final long totalOperations;
        private final long successfulOperations;
        private final long failedOperations;
        private final double successRate;
        private final double avgExecutionTime;
        private final double operationsPerSecond;
        private final long uptime;
        
        public PerformanceStats(long totalOperations, long successfulOperations, 
                              long failedOperations, double successRate, 
                              double avgExecutionTime, double operationsPerSecond, long uptime) {
            this.totalOperations = totalOperations;
            this.successfulOperations = successfulOperations;
            this.failedOperations = failedOperations;
            this.successRate = successRate;
            this.avgExecutionTime = avgExecutionTime;
            this.operationsPerSecond = operationsPerSecond;
            this.uptime = uptime;
        }
        
        public long getTotalOperations() { return totalOperations; }
        public long getSuccessfulOperations() { return successfulOperations; }
        public long getFailedOperations() { return failedOperations; }
        public double getSuccessRate() { return successRate; }
        public double getAvgExecutionTime() { return avgExecutionTime; }
        public double getOperationsPerSecond() { return operationsPerSecond; }
        public long getUptime() { return uptime; }
        
        @Override
        public String toString() {
            return String.format("PerformanceStats[Total: %d, Success: %.2f%%, AvgTime: %.2fms, Ops/sec: %.2f]",
                totalOperations, successRate * 100, avgExecutionTime, operationsPerSecond);
        }
    }
}