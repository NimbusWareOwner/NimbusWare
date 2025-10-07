package com.example.nimbusware.init;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Centralized initialization manager for all systems
 */
public class InitializationManager {
    private static volatile InitializationManager instance;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "InitializationManager-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private volatile boolean initialized = false;
    private volatile boolean initializationInProgress = false;
    
    private InitializationManager() {}
    
    public static InitializationManager getInstance() {
        if (instance == null) {
            synchronized (InitializationManager.class) {
                if (instance == null) {
                    instance = new InitializationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Initialize all systems
     * @return true if initialization successful
     */
    public boolean initializeAll() {
        if (initialized) {
            Logger.info("Systems already initialized");
            return true;
        }
        
        if (initializationInProgress) {
            Logger.warn("Initialization already in progress");
            return false;
        }
        
        initializationInProgress = true;
        
        try {
            Logger.info("🚀 Starting system initialization...");
            
            // Initialize core systems
            initializeCoreSystems();
            
            // Initialize monitoring systems
            initializeMonitoringSystems();
            
            // Initialize security systems
            initializeSecuritySystems();
            
            // Initialize utility systems
            initializeUtilitySystems();
            
            // Initialize advanced systems
            initializeAdvancedSystems();
            
            // Finalize initialization
            finalizeInitialization();
            
            initialized = true;
            Logger.info("✅ All systems initialized successfully!");
            return true;
            
        } catch (Exception e) {
            Logger.error("❌ Initialization failed", e);
            return false;
        } finally {
            initializationInProgress = false;
        }
    }
    
    private void initializeCoreSystems() {
        Logger.info("Initializing core systems...");
        
        try {
            // Initialize NimbusWare
            if (NimbusWare.INSTANCE == null) {
                new NimbusWare();
            }
            
            // Initialize module manager
            com.example.nimbusware.core.ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            if (moduleManager != null) {
                Logger.info("✅ ModuleManager initialized");
            }
            
            // Initialize event manager
            com.example.nimbusware.core.EventManager eventManager = NimbusWare.INSTANCE.getEventManager();
            if (eventManager != null) {
                Logger.info("✅ EventManager initialized");
            }
            
        } catch (Exception e) {
            Logger.error("Failed to initialize core systems", e);
            throw e;
        }
    }
    
    private void initializeMonitoringSystems() {
        Logger.info("Initializing monitoring systems...");
        
        try {
            // Initialize metrics collector
            com.example.nimbusware.monitoring.MetricsCollector metricsCollector = NimbusWare.INSTANCE.getMetricsCollector();
            if (metricsCollector != null) {
                Logger.info("✅ MetricsCollector initialized");
            }
            
            // Initialize health checker
            com.example.nimbusware.monitoring.HealthChecker healthChecker = NimbusWare.INSTANCE.getHealthChecker();
            if (healthChecker != null) {
                Logger.info("✅ HealthChecker initialized");
            }
            
            // Initialize statistics collector
            com.example.nimbusware.analytics.StatisticsCollector statisticsCollector = NimbusWare.INSTANCE.getStatisticsCollector();
            if (statisticsCollector != null) {
                Logger.info("✅ StatisticsCollector initialized");
            }
            
            // Initialize performance monitor
            com.example.nimbusware.monitoring.PerformanceMonitor performanceMonitor = com.example.nimbusware.monitoring.PerformanceMonitor.getInstance();
            Logger.info("✅ PerformanceMonitor initialized");
            
        } catch (Exception e) {
            Logger.error("Failed to initialize monitoring systems", e);
            throw e;
        }
    }
    
    private void initializeSecuritySystems() {
        Logger.info("Initializing security systems...");
        
        try {
            // Initialize encryption manager
            com.example.nimbusware.security.EncryptionManager encryptionManager = NimbusWare.INSTANCE.getEncryptionManager();
            if (encryptionManager != null) {
                Logger.info("✅ EncryptionManager initialized");
            }
            
            // Initialize security manager
            com.example.nimbusware.security.SecurityManager securityManager = com.example.nimbusware.security.SecurityManager.getInstance();
            Logger.info("✅ SecurityManager initialized");
            
        } catch (Exception e) {
            Logger.error("Failed to initialize security systems", e);
            throw e;
        }
    }
    
    private void initializeUtilitySystems() {
        Logger.info("Initializing utility systems...");
        
        try {
            // Initialize error handler
            com.example.nimbusware.utils.ErrorHandler errorHandler = com.example.nimbusware.utils.ErrorHandler.getInstance();
            Logger.info("✅ ErrorHandler initialized");
            
            // Initialize notification manager
            com.example.nimbusware.notifications.NotificationManager notificationManager = com.example.nimbusware.notifications.NotificationManager.getInstance();
            Logger.info("✅ NotificationManager initialized");
            
            // Initialize backup manager
            com.example.nimbusware.backup.BackupManager backupManager = com.example.nimbusware.backup.BackupManager.getInstance();
            Logger.info("✅ BackupManager initialized");
            
        } catch (Exception e) {
            Logger.error("Failed to initialize utility systems", e);
            throw e;
        }
    }
    
    private void initializeAdvancedSystems() {
        Logger.info("Initializing advanced systems...");
        
        try {
            // Initialize AI behavior engine
            com.example.nimbusware.ai.AIBehaviorEngine aiEngine = com.example.nimbusware.ai.AIBehaviorEngine.getInstance();
            Logger.info("✅ AIBehaviorEngine initialized");
            
            // Initialize advanced analytics
            com.example.nimbusware.analytics.AdvancedAnalytics analytics = com.example.nimbusware.analytics.AdvancedAnalytics.getInstance();
            Logger.info("✅ AdvancedAnalytics initialized");
            
            // Initialize auto tester
            com.example.nimbusware.testing.AutoTester autoTester = com.example.nimbusware.testing.AutoTester.getInstance();
            Logger.info("✅ AutoTester initialized");
            
        } catch (Exception e) {
            Logger.error("Failed to initialize advanced systems", e);
            throw e;
        }
    }
    
    private void finalizeInitialization() {
        Logger.info("Finalizing initialization...");
        
        try {
            // Start background tasks
            startBackgroundTasks();
            
            // Run initial health check
            runInitialHealthCheck();
            
            // Log initialization complete
            Logger.info("🎉 Initialization complete! All systems ready.");
            
        } catch (Exception e) {
            Logger.error("Failed to finalize initialization", e);
            throw e;
        }
    }
    
    private void startBackgroundTasks() {
        // Start performance monitoring
        scheduler.scheduleAtFixedRate(() -> {
            try {
                com.example.nimbusware.monitoring.PerformanceMonitor performanceMonitor = com.example.nimbusware.monitoring.PerformanceMonitor.getInstance();
                // Performance monitoring is already started in constructor
            } catch (Exception e) {
                Logger.error("Error in performance monitoring", e);
            }
        }, 30, 30, TimeUnit.SECONDS);
        
        Logger.info("✅ Background tasks started");
    }
    
    private void runInitialHealthCheck() {
        try {
            com.example.nimbusware.monitoring.HealthChecker healthChecker = NimbusWare.INSTANCE.getHealthChecker();
            if (healthChecker != null) {
                com.example.nimbusware.monitoring.HealthChecker.HealthStatus healthStatus = healthChecker.checkAllHealth();
                if (healthStatus.isHealthy()) {
                    Logger.info("✅ Initial health check passed");
                } else {
                    Logger.warn("⚠️ Initial health check warnings: " + healthStatus.getMessage());
                }
            }
        } catch (Exception e) {
            Logger.error("Initial health check failed", e);
        }
    }
    
    /**
     * Check if systems are initialized
     * @return true if initialized
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Check if initialization is in progress
     * @return true if in progress
     */
    public boolean isInitializationInProgress() {
        return initializationInProgress;
    }
    
    /**
     * Shutdown all systems
     */
    public void shutdown() {
        Logger.info("🔄 Shutting down all systems...");
        
        try {
            // Shutdown background tasks
            scheduler.shutdown();
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
            
            // Shutdown NimbusWare
            if (NimbusWare.INSTANCE != null) {
                NimbusWare.INSTANCE.shutdown();
            }
            
            Logger.info("✅ All systems shutdown complete");
            
        } catch (Exception e) {
            Logger.error("Error during shutdown", e);
        }
    }
}