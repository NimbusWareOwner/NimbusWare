package com.example.nimbusware.release;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Pre-release checker to ensure everything is ready for production
 */
public class ReleaseChecker {
    private static volatile ReleaseChecker instance;
    private final List<String> issues = new ArrayList<>();
    private final List<String> warnings = new ArrayList<>();
    
    private ReleaseChecker() {}
    
    public static ReleaseChecker getInstance() {
        if (instance == null) {
            synchronized (ReleaseChecker.class) {
                if (instance == null) {
                    instance = new ReleaseChecker();
                }
            }
        }
        return instance;
    }
    
    /**
     * Run comprehensive pre-release checks
     * @return true if ready for release
     */
    public boolean checkReleaseReadiness() {
        Logger.info("🔍 Starting pre-release checks...");
        
        issues.clear();
        warnings.clear();
        
        // Check core systems
        checkCoreSystems();
        
        // Check modules
        checkModules();
        
        // Check configuration
        checkConfiguration();
        
        // Check security
        checkSecurity();
        
        // Check performance
        checkPerformance();
        
        // Generate report
        generateReport();
        
        return issues.isEmpty();
    }
    
    private void checkCoreSystems() {
        Logger.info("Checking core systems...");
        
        try {
            // Check NimbusWare initialization
            if (NimbusWare.INSTANCE == null) {
                issues.add("NimbusWare not initialized");
            } else {
                Logger.info("✅ NimbusWare initialized");
            }
            
            // Check module manager
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            if (moduleManager == null) {
                issues.add("ModuleManager not available");
            } else {
                Logger.info("✅ ModuleManager available");
            }
            
            // Check logger
            Logger.info("✅ Logger system working");
            
        } catch (Exception e) {
            issues.add("Core system check failed: " + e.getMessage());
        }
    }
    
    private void checkModules() {
        Logger.info("Checking modules...");
        
        try {
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            java.util.List<com.example.nimbusware.core.Module> modules = moduleManager.getModules();
            
            if (modules.isEmpty()) {
                warnings.add("No modules registered");
            } else {
                Logger.info("✅ " + modules.size() + " modules registered");
                
                // Check each module
                for (com.example.nimbusware.core.Module module : modules) {
                    try {
                        // Test module initialization
                        if (module.getName() == null || module.getName().isEmpty()) {
                            issues.add("Module has null/empty name");
                        }
                        
                        // Test settings
                        com.example.nimbusware.core.ModuleSettings settings = module.getSettings();
                        if (settings == null) {
                            issues.add("Module " + module.getName() + " has null settings");
                        }
                        
                        // Test statistics
                        java.util.Map<String, Object> stats = module.getStatistics();
                        if (stats == null) {
                            issues.add("Module " + module.getName() + " has null statistics");
                        }
                        
                    } catch (Exception e) {
                        issues.add("Module " + module.getName() + " check failed: " + e.getMessage());
                    }
                }
            }
            
        } catch (Exception e) {
            issues.add("Module check failed: " + e.getMessage());
        }
    }
    
    private void checkConfiguration() {
        Logger.info("Checking configuration...");
        
        try {
            // Check config manager
            com.example.nimbusware.config.ConfigManager configManager = NimbusWare.INSTANCE.getConfigManager();
            if (configManager == null) {
                issues.add("ConfigManager not available");
            } else {
                Logger.info("✅ ConfigManager available");
            }
            
            // Check secure config manager
            com.example.nimbusware.security.SecureConfigManager secureConfigManager = NimbusWare.INSTANCE.getSecureConfigManager();
            if (secureConfigManager == null) {
                issues.add("SecureConfigManager not available");
            } else {
                Logger.info("✅ SecureConfigManager available");
            }
            
        } catch (Exception e) {
            issues.add("Configuration check failed: " + e.getMessage());
        }
    }
    
    private void checkSecurity() {
        Logger.info("Checking security...");
        
        try {
            // Check encryption manager
            com.example.nimbusware.security.EncryptionManager encryptionManager = NimbusWare.INSTANCE.getEncryptionManager();
            if (encryptionManager == null) {
                warnings.add("EncryptionManager not available");
            } else {
                Logger.info("✅ EncryptionManager available");
            }
            
            // Check security manager
            com.example.nimbusware.security.SecurityManager securityManager = com.example.nimbusware.security.SecurityManager.getInstance();
            if (securityManager == null) {
                warnings.add("SecurityManager not available");
            } else {
                Logger.info("✅ SecurityManager available");
            }
            
        } catch (Exception e) {
            warnings.add("Security check failed: " + e.getMessage());
        }
    }
    
    private void checkPerformance() {
        Logger.info("Checking performance systems...");
        
        try {
            // Check metrics collector
            com.example.nimbusware.monitoring.MetricsCollector metricsCollector = NimbusWare.INSTANCE.getMetricsCollector();
            if (metricsCollector == null) {
                warnings.add("MetricsCollector not available");
            } else {
                Logger.info("✅ MetricsCollector available");
            }
            
            // Check health checker
            com.example.nimbusware.monitoring.HealthChecker healthChecker = NimbusWare.INSTANCE.getHealthChecker();
            if (healthChecker == null) {
                warnings.add("HealthChecker not available");
            } else {
                Logger.info("✅ HealthChecker available");
            }
            
            // Check performance monitor
            com.example.nimbusware.monitoring.PerformanceMonitor performanceMonitor = com.example.nimbusware.monitoring.PerformanceMonitor.getInstance();
            if (performanceMonitor == null) {
                warnings.add("PerformanceMonitor not available");
            } else {
                Logger.info("✅ PerformanceMonitor available");
            }
            
        } catch (Exception e) {
            warnings.add("Performance check failed: " + e.getMessage());
        }
    }
    
    private void generateReport() {
        Logger.info("📊 Generating release readiness report...");
        
        Logger.info("=== RELEASE READINESS REPORT ===");
        
        if (issues.isEmpty()) {
            Logger.info("🎉 READY FOR RELEASE!");
        } else {
            Logger.info("❌ NOT READY FOR RELEASE - Issues found:");
            for (String issue : issues) {
                Logger.error("  - " + issue);
            }
        }
        
        if (!warnings.isEmpty()) {
            Logger.info("⚠️ Warnings (non-critical):");
            for (String warning : warnings) {
                Logger.warn("  - " + warning);
            }
        }
        
        Logger.info("Issues: " + issues.size());
        Logger.info("Warnings: " + warnings.size());
        Logger.info("================================");
    }
    
    /**
     * Get list of issues
     * @return List of issues
     */
    public List<String> getIssues() {
        return new ArrayList<>(issues);
    }
    
    /**
     * Get list of warnings
     * @return List of warnings
     */
    public List<String> getWarnings() {
        return new ArrayList<>(warnings);
    }
    
    /**
     * Check if ready for release
     * @return true if ready
     */
    public boolean isReadyForRelease() {
        return issues.isEmpty();
    }
}