package com.example.nimbusware.monitoring;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.Map;
import java.util.List;
import java.util.ArrayList;
import java.util.function.Supplier;

/**
 * Health monitoring system for checking system status and components
 */
public class HealthChecker {
    private static volatile HealthChecker instance;
    private final Map<String, HealthCheck> healthChecks = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "HealthChecker-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private HealthChecker() {
        registerDefaultHealthChecks();
        startPeriodicChecks();
    }
    
    public static HealthChecker getInstance() {
        if (instance == null) {
            synchronized (HealthChecker.class) {
                if (instance == null) {
                    instance = new HealthChecker();
                }
            }
        }
        return instance;
    }
    
    /**
     * Register a health check
     * @param name Check name
     * @param check Health check function
     */
    public void registerHealthCheck(String name, Supplier<HealthStatus> check) {
        healthChecks.put(name, new HealthCheck(name, check));
        Logger.debug("Registered health check: " + name);
    }
    
    /**
     * Run a specific health check
     * @param name Check name
     * @return Health status
     */
    public HealthStatus checkHealth(String name) {
        HealthCheck check = healthChecks.get(name);
        if (check == null) {
            return HealthStatus.unhealthy("Health check not found: " + name);
        }
        
        try {
            return check.run();
        } catch (Exception e) {
            Logger.error("Health check failed: " + name, e);
            return HealthStatus.unhealthy("Health check error: " + e.getMessage());
        }
    }
    
    /**
     * Run all health checks
     * @return Overall health status
     */
    public HealthStatus checkAllHealth() {
        List<HealthStatus> results = new ArrayList<>();
        
        for (HealthCheck check : healthChecks.values()) {
            try {
                HealthStatus status = check.run();
                results.add(status);
                
                if (!status.isHealthy()) {
                    Logger.warn("Health check failed: " + check.getName() + " - " + status.getMessage());
                }
            } catch (Exception e) {
                Logger.error("Health check error: " + check.getName(), e);
                results.add(HealthStatus.unhealthy("Error: " + e.getMessage()));
            }
        }
        
        // Determine overall health
        boolean allHealthy = results.stream().allMatch(HealthStatus::isHealthy);
        String message = allHealthy ? "All systems healthy" : "Some systems unhealthy";
        
        return new HealthStatus(allHealthy, message, results);
    }
    
    /**
     * Get health check results
     * @return Map of check names to status
     */
    public Map<String, HealthStatus> getHealthResults() {
        Map<String, HealthStatus> results = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, HealthCheck> entry : healthChecks.entrySet()) {
            try {
                results.put(entry.getKey(), entry.getValue().run());
            } catch (Exception e) {
                results.put(entry.getKey(), HealthStatus.unhealthy("Error: " + e.getMessage()));
            }
        }
        
        return results;
    }
    
    private void registerDefaultHealthChecks() {
        // Memory health check
        registerHealthCheck("memory", () -> {
            Runtime runtime = Runtime.getRuntime();
            long usedMemory = runtime.totalMemory() - runtime.freeMemory();
            long maxMemory = runtime.maxMemory();
            double usagePercent = (double) usedMemory / maxMemory * 100;
            
            if (usagePercent > 90) {
                return HealthStatus.unhealthy("Memory usage critical: " + String.format("%.1f", usagePercent) + "%");
            } else if (usagePercent > 75) {
                return HealthStatus.warning("Memory usage high: " + String.format("%.1f", usagePercent) + "%");
            } else {
                return HealthStatus.healthy("Memory usage normal: " + String.format("%.1f", usagePercent) + "%");
            }
        });
        
        // Thread health check
        registerHealthCheck("threads", () -> {
            int activeThreads = Thread.activeCount();
            if (activeThreads > 100) {
                return HealthStatus.unhealthy("Too many active threads: " + activeThreads);
            } else if (activeThreads > 50) {
                return HealthStatus.warning("High thread count: " + activeThreads);
            } else {
                return HealthStatus.healthy("Thread count normal: " + activeThreads);
            }
        });
        
        // NimbusWare instance health check
        registerHealthCheck("nimbusware", () -> {
            if (NimbusWare.INSTANCE == null) {
                return HealthStatus.unhealthy("NimbusWare instance not initialized");
            }
            
            if (!NimbusWare.INSTANCE.isInitialized()) {
                return HealthStatus.unhealthy("NimbusWare not initialized");
            }
            
            return HealthStatus.healthy("NimbusWare running normally");
        });
        
        // Module manager health check
        registerHealthCheck("modules", () -> {
            if (NimbusWare.INSTANCE == null || !NimbusWare.INSTANCE.isInitialized()) {
                return HealthStatus.unhealthy("NimbusWare not initialized");
            }
            
            try {
                var moduleManager = NimbusWare.INSTANCE.getModuleManager();
                if (moduleManager == null) {
                    return HealthStatus.unhealthy("Module manager not available");
                }
                
                int moduleCount = moduleManager.getModules().size();
                int enabledCount = moduleManager.getEnabledModules().size();
                
                return HealthStatus.healthy(String.format("Modules: %d total, %d enabled", moduleCount, enabledCount));
            } catch (Exception e) {
                return HealthStatus.unhealthy("Module manager error: " + e.getMessage());
            }
        });
        
        // Event manager health check
        registerHealthCheck("events", () -> {
            if (NimbusWare.INSTANCE == null || !NimbusWare.INSTANCE.isInitialized()) {
                return HealthStatus.unhealthy("NimbusWare not initialized");
            }
            
            try {
                var eventManager = NimbusWare.INSTANCE.getEventManager();
                if (eventManager == null) {
                    return HealthStatus.unhealthy("Event manager not available");
                }
                
                return HealthStatus.healthy("Event manager running normally");
            } catch (Exception e) {
                return HealthStatus.unhealthy("Event manager error: " + e.getMessage());
            }
        });
    }
    
    private void startPeriodicChecks() {
        // Run health checks every 2 minutes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                HealthStatus overall = checkAllHealth();
                if (!overall.isHealthy()) {
                    Logger.warn("System health issues detected: " + overall.getMessage());
                }
            } catch (Exception e) {
                Logger.error("Error during periodic health check", e);
            }
        }, 2, 2, TimeUnit.MINUTES);
    }
    
    /**
     * Shutdown the health checker
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
    
    private static class HealthCheck {
        private final String name;
        private final Supplier<HealthStatus> check;
        
        public HealthCheck(String name, Supplier<HealthStatus> check) {
            this.name = name;
            this.check = check;
        }
        
        public String getName() {
            return name;
        }
        
        public HealthStatus run() {
            return check.get();
        }
    }
    
    public static class HealthStatus {
        private final boolean healthy;
        private final String message;
        private final List<HealthStatus> details;
        
        private HealthStatus(boolean healthy, String message, List<HealthStatus> details) {
            this.healthy = healthy;
            this.message = message;
            this.details = details != null ? details : new ArrayList<>();
        }
        
        public static HealthStatus healthy(String message) {
            return new HealthStatus(true, message, null);
        }
        
        public static HealthStatus warning(String message) {
            return new HealthStatus(false, message, null);
        }
        
        public static HealthStatus unhealthy(String message) {
            return new HealthStatus(false, message, null);
        }
        
        public boolean isHealthy() {
            return healthy;
        }
        
        public String getMessage() {
            return message;
        }
        
        public List<HealthStatus> getDetails() {
            return details;
        }
        
        @Override
        public String toString() {
            return (healthy ? "HEALTHY" : "UNHEALTHY") + ": " + message;
        }
    }
}