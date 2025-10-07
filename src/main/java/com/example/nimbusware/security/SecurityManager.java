package com.example.nimbusware.security;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Advanced security management system
 */
public class SecurityManager {
    private static volatile SecurityManager instance;
    private final Map<String, AtomicLong> securityEvents = new ConcurrentHashMap<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "SecurityManager-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private volatile boolean securityEnabled = true;
    private volatile int maxFailedAttempts = 5;
    private final Map<String, Integer> failedAttempts = new ConcurrentHashMap<>();
    
    private SecurityManager() {
        startSecurityMonitoring();
    }
    
    public static SecurityManager getInstance() {
        if (instance == null) {
            synchronized (SecurityManager.class) {
                if (instance == null) {
                    instance = new SecurityManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Check if an action is allowed
     * @param action Action to check
     * @param context Context of the action
     * @return true if allowed
     */
    public boolean isActionAllowed(String action, String context) {
        if (!securityEnabled) {
            return true;
        }
        
        // Check for suspicious patterns
        if (isSuspiciousAction(action, context)) {
            recordSecurityEvent("suspicious_action", action + " in " + context);
            return false;
        }
        
        // Check rate limiting
        if (isRateLimited(action)) {
            recordSecurityEvent("rate_limited", action);
            return false;
        }
        
        return true;
    }
    
    /**
     * Record a security event
     * @param eventType Type of security event
     * @param details Event details
     */
    public void recordSecurityEvent(String eventType, String details) {
        securityEvents.computeIfAbsent(eventType, k -> new AtomicLong(0)).incrementAndGet();
        Logger.warn("Security event: " + eventType + " - " + details);
    }
    
    /**
     * Record a failed attempt
     * @param action Action that failed
     */
    public void recordFailedAttempt(String action) {
        int attempts = failedAttempts.compute(action, (k, v) -> v == null ? 1 : v + 1);
        
        if (attempts >= maxFailedAttempts) {
            recordSecurityEvent("max_attempts_exceeded", action);
            // Temporarily block the action
            scheduler.schedule(() -> {
                failedAttempts.remove(action);
                Logger.info("Rate limit reset for action: " + action);
            }, 5, TimeUnit.MINUTES);
        }
    }
    
    /**
     * Reset failed attempts for an action
     * @param action Action to reset
     */
    public void resetFailedAttempts(String action) {
        failedAttempts.remove(action);
    }
    
    private boolean isSuspiciousAction(String action, String context) {
        // Check for suspicious patterns
        String suspiciousPatterns[] = {
            "inject", "exploit", "bypass", "hack", "crack",
            "admin", "root", "system", "privilege"
        };
        
        String combined = (action + " " + context).toLowerCase();
        for (String pattern : suspiciousPatterns) {
            if (combined.contains(pattern)) {
                return true;
            }
        }
        
        return false;
    }
    
    private boolean isRateLimited(String action) {
        Integer attempts = failedAttempts.get(action);
        return attempts != null && attempts >= maxFailedAttempts;
    }
    
    private void startSecurityMonitoring() {
        // Monitor security events every minute
        scheduler.scheduleAtFixedRate(() -> {
            try {
                logSecurityStats();
            } catch (Exception e) {
                Logger.error("Error in security monitoring", e);
            }
        }, 1, 1, TimeUnit.MINUTES);
        
        Logger.info("Security monitoring started");
    }
    
    private void logSecurityStats() {
        if (securityEvents.isEmpty()) {
            return;
        }
        
        Logger.info("=== SECURITY STATS ===");
        securityEvents.forEach((event, count) -> {
            Logger.info(event + ": " + count.get());
        });
        Logger.info("=====================");
    }
    
    /**
     * Get security statistics
     * @return Security statistics
     */
    public Map<String, Long> getSecurityStatistics() {
        Map<String, Long> stats = new ConcurrentHashMap<>();
        securityEvents.forEach((event, count) -> stats.put(event, count.get()));
        return stats;
    }
    
    /**
     * Set security enabled
     * @param enabled Whether security is enabled
     */
    public void setSecurityEnabled(boolean enabled) {
        this.securityEnabled = enabled;
        Logger.info("Security " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Set maximum failed attempts
     * @param maxAttempts Maximum failed attempts before blocking
     */
    public void setMaxFailedAttempts(int maxAttempts) {
        this.maxFailedAttempts = maxAttempts;
        Logger.info("Max failed attempts set to: " + maxAttempts);
    }
    
    /**
     * Shutdown security manager
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
}