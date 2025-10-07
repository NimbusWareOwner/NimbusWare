package com.example.nimbusware.anti_detection;

import com.example.nimbusware.anti_detection.strategy.FuntimeBypassStrategy;
import com.example.nimbusware.anti_detection.strategy.HypixelBypassStrategy;
import com.example.nimbusware.anti_detection.strategy.MatrixBypassStrategy;
import com.example.nimbusware.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced Anti-Detection Manager using Strategy pattern
 * Provides centralized management of various server bypass strategies
 */
public class AntiDetectionManager {
    private static final Map<String, BypassInfo> activeBypasses = new ConcurrentHashMap<>();
    private static final Random random = new Random();
    
    // Strategy instances
    private static final FuntimeBypassStrategy funtimeStrategy = new FuntimeBypassStrategy();
    private static final MatrixBypassStrategy matrixStrategy = new MatrixBypassStrategy();
    private static final HypixelBypassStrategy hypixelStrategy = new HypixelBypassStrategy();
    
    // Strategy registry
    private static final Map<BypassType, BypassStrategy> strategies = new HashMap<>();
    
    static {
        strategies.put(BypassType.FUNTIME, funtimeStrategy);
        strategies.put(BypassType.MATRIX, matrixStrategy);
        strategies.put(BypassType.HYPIXEL, hypixelStrategy);
    }
    
    /**
     * Enable a specific bypass strategy for a module
     * @param moduleName Name of the module
     * @param bypassType Type of bypass to enable
     */
    public static void enableBypass(String moduleName, BypassType bypassType) {
        if (moduleName == null || moduleName.trim().isEmpty()) {
            Logger.warn("Cannot enable bypass: module name is null or empty");
            return;
        }
        
        BypassStrategy strategy = strategies.get(bypassType);
        if (strategy == null) {
            Logger.warn("Unknown bypass type: " + bypassType);
            return;
        }
        
        BypassInfo bypass = new BypassInfo();
        bypass.moduleName = moduleName;
        bypass.bypassType = bypassType;
        bypass.strategy = strategy;
        bypass.enabled = true;
        bypass.lastAction = System.currentTimeMillis();
        
        activeBypasses.put(moduleName, bypass);
        strategy.setActive(true);
        
        Logger.info("Enabled " + bypassType + " bypass for " + moduleName);
    }
    
    /**
     * Disable bypass for a specific module
     * @param moduleName Name of the module
     */
    public static void disableBypass(String moduleName) {
        if (moduleName == null || moduleName.trim().isEmpty()) {
            Logger.warn("Cannot disable bypass: module name is null or empty");
            return;
        }
        
        BypassInfo bypass = activeBypasses.remove(moduleName);
        if (bypass != null && bypass.strategy != null) {
            bypass.strategy.setActive(false);
            Logger.info("Disabled " + bypass.bypassType + " bypass for " + moduleName);
        }
    }
    
    // Legacy methods for backward compatibility
    public static void enableNCPBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as NCP alternative
    }
    
    public static void disableNCPBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableAACBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as AAC alternative
    }
    
    public static void disableAACBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableGrimBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as Grim alternative
    }
    
    public static void disableGrimBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableVerusBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as Verus alternative
    }
    
    public static void disableVerusBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableVulcanBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as Vulcan alternative
    }
    
    public static void disableVulcanBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableSpartanBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as Spartan alternative
    }
    
    public static void disableSpartanBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void enableIntaveBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX); // Use Matrix as Intave alternative
    }
    
    public static void disableIntaveBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    /**
     * Apply movement modification using the appropriate strategy
     * @param moduleName Name of the module
     * @param value Original movement value
     */
    public static void applyMovementModification(String moduleName, float value) {
        BypassInfo bypass = activeBypasses.get(moduleName);
        if (bypass == null || !bypass.enabled || bypass.strategy == null) {
            return;
        }
        
        try {
            float modifiedValue = bypass.strategy.applyMovementModification(value, random);
            applyMovementValue(moduleName, modifiedValue);
            bypass.lastAction = System.currentTimeMillis();
        } catch (Exception e) {
            Logger.error("Error applying movement modification for " + moduleName, e);
        }
    }
    
    /**
     * Apply combat modification using the appropriate strategy
     * @param moduleName Name of the module
     * @param value Original combat value
     */
    public static void applyCombatModification(String moduleName, float value) {
        BypassInfo bypass = activeBypasses.get(moduleName);
        if (bypass == null || !bypass.enabled || bypass.strategy == null) {
            return;
        }
        
        try {
            float modifiedValue = bypass.strategy.applyCombatModification(value, random);
            applyCombatValue(moduleName, modifiedValue);
            bypass.lastAction = System.currentTimeMillis();
        } catch (Exception e) {
            Logger.error("Error applying combat modification for " + moduleName, e);
        }
    }
    
    /**
     * Check if a bypass is active for a module
     * @param moduleName Name of the module
     * @return true if bypass is active, false otherwise
     */
    public static boolean isBypassActive(String moduleName) {
        BypassInfo bypass = activeBypasses.get(moduleName);
        return bypass != null && bypass.enabled;
    }
    
    /**
     * Get the bypass type for a module
     * @param moduleName Name of the module
     * @return BypassType or null if not found
     */
    public static BypassType getBypassType(String moduleName) {
        BypassInfo bypass = activeBypasses.get(moduleName);
        return bypass != null ? bypass.bypassType : null;
    }
    
    /**
     * Get all active bypasses
     * @return Map of module names to bypass info
     */
    public static Map<String, BypassInfo> getActiveBypasses() {
        return new HashMap<>(activeBypasses);
    }
    
    /**
     * Clear all bypasses
     */
    public static void clearAllBypasses() {
        for (BypassInfo bypass : activeBypasses.values()) {
            if (bypass.strategy != null) {
                bypass.strategy.setActive(false);
            }
        }
        activeBypasses.clear();
        Logger.info("Cleared all bypasses");
    }
    
    private static void applyMovementValue(String moduleName, float value) {
        // Mock implementation - in real client would apply to player movement
        Logger.debug("Applied movement modification for " + moduleName + ": " + value);
    }
    
    private static void applyCombatValue(String moduleName, float value) {
        // Mock implementation - in real client would apply to combat actions
        Logger.debug("Applied combat modification for " + moduleName + ": " + value);
    }
    
    // Convenience methods for backward compatibility
    public static void enableFuntimeBypass(String moduleName) {
        enableBypass(moduleName, BypassType.FUNTIME);
    }
    
    public static void enableMatrixBypass(String moduleName) {
        enableBypass(moduleName, BypassType.MATRIX);
    }
    
    public static void enableHypixelBypass(String moduleName) {
        enableBypass(moduleName, BypassType.HYPIXEL);
    }
    
    public static void disableFuntimeBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void disableMatrixBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    public static void disableHypixelBypass(String moduleName) {
        disableBypass(moduleName);
    }
    
    /**
     * Information about an active bypass
     */
    public static class BypassInfo {
        private String moduleName;
        private BypassType bypassType;
        private BypassStrategy strategy;
        private boolean enabled;
        private long lastAction;
        
        // Getters
        public String getModuleName() { return moduleName; }
        public BypassType getBypassType() { return bypassType; }
        public BypassStrategy getStrategy() { return strategy; }
        public boolean isEnabled() { return enabled; }
        public long getLastAction() { return lastAction; }
    }
    
    /**
     * Available bypass types
     */
    public enum BypassType {
        FUNTIME("Funtime"),
        MATRIX("Matrix"),
        HYPIXEL("Hypixel"),
        NCP("NCP"),
        AAC("AAC"),
        GRIM("Grim"),
        VERUS("Verus"),
        VULCAN("Vulcan"),
        SPARTAN("Spartan"),
        INTAVE("Intave"),
        CUSTOM("Custom");
        
        private final String displayName;
        
        BypassType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}