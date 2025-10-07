package com.example.nimbusware.utils;

import com.example.nimbusware.notifications.NotificationManager;

import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicLong;

/**
 * Centralized error handling system
 */
public class ErrorHandler {
    private static volatile ErrorHandler instance;
    private final Map<String, AtomicLong> errorCounts = new ConcurrentHashMap<>();
    private final NotificationManager notificationManager = NotificationManager.getInstance();
    
    private ErrorHandler() {}
    
    public static ErrorHandler getInstance() {
        if (instance == null) {
            synchronized (ErrorHandler.class) {
                if (instance == null) {
                    instance = new ErrorHandler();
                }
            }
        }
        return instance;
    }
    
    /**
     * Handle an error with automatic recovery
     * @param error Error to handle
     * @param context Context where error occurred
     * @param recoverable Whether error is recoverable
     */
    public void handleError(Throwable error, String context, boolean recoverable) {
        String errorType = error.getClass().getSimpleName();
        errorCounts.computeIfAbsent(errorType, k -> new AtomicLong(0)).incrementAndGet();
        
        Logger.error("Error in " + context + ": " + error.getMessage(), error);
        
        if (recoverable) {
            attemptRecovery(error, context);
        } else {
            notificationManager.sendError("Critical Error", 
                "Unrecoverable error in " + context + ": " + error.getMessage());
        }
    }
    
    /**
     * Handle a warning
     * @param message Warning message
     * @param context Context where warning occurred
     */
    public void handleWarning(String message, String context) {
        Logger.warn("Warning in " + context + ": " + message);
        notificationManager.sendWarning("Warning", message);
    }
    
    /**
     * Handle an info message
     * @param message Info message
     * @param context Context where info occurred
     */
    public void handleInfo(String message, String context) {
        Logger.info("Info in " + context + ": " + message);
        notificationManager.sendInfo("Info", message);
    }
    
    private void attemptRecovery(Throwable error, String context) {
        try {
            // Attempt basic recovery strategies
            if (error instanceof OutOfMemoryError) {
                System.gc(); // Force garbage collection
                notificationManager.sendInfo("Recovery", "Attempted memory recovery");
            } else if (error instanceof NullPointerException) {
                notificationManager.sendWarning("Recovery", "Null pointer detected, continuing...");
            } else {
                notificationManager.sendWarning("Recovery", "Attempting to continue after error");
            }
        } catch (Exception recoveryError) {
            Logger.error("Recovery failed", recoveryError);
        }
    }
    
    /**
     * Get error statistics
     * @return Map of error types to counts
     */
    public Map<String, Long> getErrorStatistics() {
        Map<String, Long> stats = new ConcurrentHashMap<>();
        errorCounts.forEach((type, count) -> stats.put(type, count.get()));
        return stats;
    }
    
    /**
     * Reset error counts
     */
    public void resetErrorCounts() {
        errorCounts.clear();
        Logger.info("Error counts reset");
    }
}