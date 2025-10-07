package com.example.nimbusware.performance;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Advanced performance optimization system
 */
public class PerformanceOptimizer {
    private static volatile PerformanceOptimizer instance;
    private final Map<String, PerformanceProfile> profiles = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "PerformanceOptimizer-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private PerformanceProfile currentProfile;
    private final AtomicLong optimizationCount = new AtomicLong(0);
    
    private PerformanceOptimizer() {
        initializeDefaultProfiles();
        startOptimizationLoop();
    }
    
    public static PerformanceOptimizer getInstance() {
        if (instance == null) {
            synchronized (PerformanceOptimizer.class) {
                if (instance == null) {
                    instance = new PerformanceOptimizer();
                }
            }
        }
        return instance;
    }
    
    /**
     * Set performance profile
     * @param profileName Profile name
     */
    public void setProfile(String profileName) {
        PerformanceProfile profile = profiles.get(profileName);
        if (profile == null) {
            Logger.warn("Performance profile not found: " + profileName);
            return;
        }
        
        currentProfile = profile;
        applyProfile(profile);
        Logger.info("Performance profile set to: " + profileName);
    }
    
    /**
     * Get current profile
     * @return Current performance profile
     */
    public PerformanceProfile getCurrentProfile() {
        return currentProfile;
    }
    
    /**
     * Optimize memory usage
     */
    public void optimizeMemory() {
        try {
            // Force garbage collection
            System.gc();
            
            // Log memory usage
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            double usagePercent = (double) usedMemory / maxMemory * 100;
            
            Logger.debug("Memory optimization: " + String.format("%.1f%% used", usagePercent));
            
            if (usagePercent > 80) {
                Logger.warn("High memory usage detected: " + String.format("%.1f%%", usagePercent));
            }
        } catch (Exception e) {
            Logger.error("Error during memory optimization", e);
        }
    }
    
    /**
     * Optimize thread usage
     */
    public void optimizeThreads() {
        try {
            int activeThreads = Thread.activeCount();
            Logger.debug("Active threads: " + activeThreads);
            
            if (activeThreads > 50) {
                Logger.warn("High thread count detected: " + activeThreads);
            }
        } catch (Exception e) {
            Logger.error("Error during thread optimization", e);
        }
    }
    
    /**
     * Optimize module performance
     * @param moduleName Module name
     */
    public void optimizeModule(String moduleName) {
        try {
            // Module-specific optimizations would go here
            Logger.debug("Optimizing module: " + moduleName);
            optimizationCount.incrementAndGet();
        } catch (Exception e) {
            Logger.error("Error optimizing module: " + moduleName, e);
        }
    }
    
    /**
     * Get performance statistics
     * @return Performance statistics
     */
    public PerformanceStatistics getStatistics() {
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        int activeThreads = Thread.activeCount();
        
        return new PerformanceStatistics(
            usedMemory,
            maxMemory,
            activeThreads,
            optimizationCount.get(),
            currentProfile != null ? currentProfile.getName() : "None"
        );
    }
    
    private void initializeDefaultProfiles() {
        // High Performance Profile
        PerformanceProfile highPerf = new PerformanceProfile("High Performance", "Maximum performance settings");
        highPerf.setSetting("memory.optimization", true);
        highPerf.setSetting("thread.optimization", true);
        highPerf.setSetting("gc.aggressive", true);
        highPerf.setSetting("cache.size", 1000);
        highPerf.setSetting("update.interval", 10);
        profiles.put("high", highPerf);
        
        // Balanced Profile
        PerformanceProfile balanced = new PerformanceProfile("Balanced", "Balanced performance and stability");
        balanced.setSetting("memory.optimization", true);
        balanced.setSetting("thread.optimization", false);
        balanced.setSetting("gc.aggressive", false);
        balanced.setSetting("cache.size", 500);
        balanced.setSetting("update.interval", 50);
        profiles.put("balanced", balanced);
        
        // Low Resource Profile
        PerformanceProfile lowResource = new PerformanceProfile("Low Resource", "Minimal resource usage");
        lowResource.setSetting("memory.optimization", false);
        lowResource.setSetting("thread.optimization", false);
        lowResource.setSetting("gc.aggressive", false);
        lowResource.setSetting("cache.size", 100);
        lowResource.setSetting("update.interval", 100);
        profiles.put("low", lowResource);
        
        // Set default profile
        currentProfile = balanced;
        Logger.info("Initialized " + profiles.size() + " performance profiles");
    }
    
    private void applyProfile(PerformanceProfile profile) {
        // Apply profile settings
        boolean memoryOpt = profile.getBoolean("memory.optimization", true);
        boolean threadOpt = profile.getBoolean("thread.optimization", false);
        boolean gcAggressive = profile.getBoolean("gc.aggressive", false);
        
        if (memoryOpt) {
            optimizeMemory();
        }
        
        if (threadOpt) {
            optimizeThreads();
        }
        
        if (gcAggressive) {
            System.gc();
        }
    }
    
    private void startOptimizationLoop() {
        // Run optimization every 30 seconds
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (currentProfile != null) {
                    applyProfile(currentProfile);
                }
            } catch (Exception e) {
                Logger.error("Error in performance optimization loop", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
    }
    
    /**
     * Shutdown the performance optimizer
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
     * Performance profile
     */
    public static class PerformanceProfile {
        private final String name;
        private final String description;
        private final Map<String, Object> settings = new ConcurrentHashMap<>();
        
        public PerformanceProfile(String name, String description) {
            this.name = name;
            this.description = description;
        }
        
        public void setSetting(String key, Object value) {
            settings.put(key, value);
        }
        
        public Object getSetting(String key, Object defaultValue) {
            return settings.getOrDefault(key, defaultValue);
        }
        
        public boolean getBoolean(String key, boolean defaultValue) {
            Object value = settings.get(key);
            if (value instanceof Boolean) {
                return (Boolean) value;
            }
            return defaultValue;
        }
        
        public int getInt(String key, int defaultValue) {
            Object value = settings.get(key);
            if (value instanceof Number) {
                return ((Number) value).intValue();
            }
            return defaultValue;
        }
        
        public String getName() { return name; }
        public String getDescription() { return description; }
        public Map<String, Object> getSettings() { return new ConcurrentHashMap<>(settings); }
    }
    
    /**
     * Performance statistics
     */
    public static class PerformanceStatistics {
        private final long usedMemory;
        private final long maxMemory;
        private final int activeThreads;
        private final long optimizationCount;
        private final String currentProfile;
        
        public PerformanceStatistics(long usedMemory, long maxMemory, int activeThreads, 
                                   long optimizationCount, String currentProfile) {
            this.usedMemory = usedMemory;
            this.maxMemory = maxMemory;
            this.activeThreads = activeThreads;
            this.optimizationCount = optimizationCount;
            this.currentProfile = currentProfile;
        }
        
        public long getUsedMemory() { return usedMemory; }
        public long getMaxMemory() { return maxMemory; }
        public int getActiveThreads() { return activeThreads; }
        public long getOptimizationCount() { return optimizationCount; }
        public String getCurrentProfile() { return currentProfile; }
        
        public double getMemoryUsagePercent() {
            return (double) usedMemory / maxMemory * 100;
        }
        
        @Override
        public String toString() {
            return String.format("Performance[Profile: %s, Memory: %.1f%%, Threads: %d, Optimizations: %d]",
                currentProfile, getMemoryUsagePercent(), activeThreads, optimizationCount);
        }
    }
}