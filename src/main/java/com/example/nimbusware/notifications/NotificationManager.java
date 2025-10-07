package com.example.nimbusware.notifications;

import com.example.nimbusware.utils.Logger;

import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Consumer;

/**
 * Advanced notification system for NimbusWare
 */
public class NotificationManager {
    private static volatile NotificationManager instance;
    private final ConcurrentLinkedQueue<Notification> notificationQueue = new ConcurrentLinkedQueue<>();
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "NotificationManager-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private final AtomicBoolean running = new AtomicBoolean(false);
    private Consumer<Notification> notificationHandler;
    
    private NotificationManager() {
        startNotificationProcessor();
    }
    
    public static NotificationManager getInstance() {
        if (instance == null) {
            synchronized (NotificationManager.class) {
                if (instance == null) {
                    instance = new NotificationManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Set notification handler
     * @param handler Notification handler
     */
    public void setNotificationHandler(Consumer<Notification> handler) {
        this.notificationHandler = handler;
    }
    
    /**
     * Send a notification
     * @param notification Notification to send
     */
    public void sendNotification(Notification notification) {
        notificationQueue.offer(notification);
        Logger.debug("Notification queued: " + notification.getTitle());
    }
    
    /**
     * Send a simple notification
     * @param title Notification title
     * @param message Notification message
     * @param type Notification type
     */
    public void sendNotification(String title, String message, NotificationType type) {
        Notification notification = new Notification(title, message, type);
        sendNotification(notification);
    }
    
    /**
     * Send an info notification
     * @param title Title
     * @param message Message
     */
    public void sendInfo(String title, String message) {
        sendNotification(title, message, NotificationType.INFO);
    }
    
    /**
     * Send a warning notification
     * @param title Title
     * @param message Message
     */
    public void sendWarning(String title, String message) {
        sendNotification(title, message, NotificationType.WARNING);
    }
    
    /**
     * Send an error notification
     * @param title Title
     * @param message Message
     */
    public void sendError(String title, String message) {
        sendNotification(title, message, NotificationType.ERROR);
    }
    
    /**
     * Send a success notification
     * @param title Title
     * @param message Message
     */
    public void sendSuccess(String title, String message) {
        sendNotification(title, message, NotificationType.SUCCESS);
    }
    
    /**
     * Send a module notification
     * @param moduleName Module name
     * @param action Action performed
     * @param success Whether action was successful
     */
    public void sendModuleNotification(String moduleName, String action, boolean success) {
        String title = "Module " + action;
        String message = moduleName + " " + (success ? "successfully " + action + "ed" : "failed to " + action);
        NotificationType type = success ? NotificationType.SUCCESS : NotificationType.ERROR;
        sendNotification(title, message, type);
    }
    
    /**
     * Send a system notification
     * @param event System event
     * @param details Event details
     */
    public void sendSystemNotification(String event, String details) {
        sendNotification("System Event", event + ": " + details, NotificationType.INFO);
    }
    
    /**
     * Send a performance notification
     * @param metric Metric name
     * @param value Metric value
     * @param threshold Threshold value
     */
    public void sendPerformanceNotification(String metric, double value, double threshold) {
        if (value > threshold) {
            String title = "Performance Alert";
            String message = metric + " is " + value + " (threshold: " + threshold + ")";
            sendNotification(title, message, NotificationType.WARNING);
        }
    }
    
    /**
     * Send a security notification
     * @param event Security event
     * @param details Event details
     */
    public void sendSecurityNotification(String event, String details) {
        sendNotification("Security Alert", event + ": " + details, NotificationType.ERROR);
    }
    
    private void startNotificationProcessor() {
        if (running.compareAndSet(false, true)) {
            scheduler.scheduleAtFixedRate(this::processNotifications, 0, 100, TimeUnit.MILLISECONDS);
            Logger.info("Notification manager started");
        }
    }
    
    private void processNotifications() {
        while (!notificationQueue.isEmpty()) {
            Notification notification = notificationQueue.poll();
            if (notification != null) {
                processNotification(notification);
            }
        }
    }
    
    private void processNotification(Notification notification) {
        try {
            // Log notification
            logNotification(notification);
            
            // Send to handler if set
            if (notificationHandler != null) {
                notificationHandler.accept(notification);
            }
            
            // Send to console
            sendToConsole(notification);
            
            // Send to web interface if available
            sendToWebInterface(notification);
            
        } catch (Exception e) {
            Logger.error("Error processing notification", e);
        }
    }
    
    private void logNotification(Notification notification) {
        switch (notification.getType()) {
            case INFO:
                Logger.info("NOTIFICATION: " + notification.getTitle() + " - " + notification.getMessage());
                break;
            case WARNING:
                Logger.warn("NOTIFICATION: " + notification.getTitle() + " - " + notification.getMessage());
                break;
            case ERROR:
                Logger.error("NOTIFICATION: " + notification.getTitle() + " - " + notification.getMessage());
                break;
            case SUCCESS:
                Logger.info("NOTIFICATION: " + notification.getTitle() + " - " + notification.getMessage());
                break;
        }
    }
    
    private void sendToConsole(Notification notification) {
        String prefix = getNotificationPrefix(notification.getType());
        System.out.println(prefix + " " + notification.getTitle() + ": " + notification.getMessage());
    }
    
    private void sendToWebInterface(Notification notification) {
        // This would send to web interface via WebSocket or similar
        // For now, just log it
        Logger.debug("Web notification: " + notification.getTitle());
    }
    
    private String getNotificationPrefix(NotificationType type) {
        switch (type) {
            case INFO:
                return "ℹ️";
            case WARNING:
                return "⚠️";
            case ERROR:
                return "❌";
            case SUCCESS:
                return "✅";
            default:
                return "📢";
        }
    }
    
    /**
     * Get notification statistics
     * @return Notification statistics
     */
    public NotificationStatistics getStatistics() {
        return new NotificationStatistics(
            notificationQueue.size(),
            running.get()
        );
    }
    
    /**
     * Shutdown the notification manager
     */
    public void shutdown() {
        if (running.compareAndSet(true, false)) {
            scheduler.shutdown();
            try {
                if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                    scheduler.shutdownNow();
                }
            } catch (InterruptedException e) {
                scheduler.shutdownNow();
                Thread.currentThread().interrupt();
            }
            Logger.info("Notification manager shutdown");
        }
    }
    
    /**
     * Notification class
     */
    public static class Notification {
        private final String title;
        private final String message;
        private final NotificationType type;
        private final long timestamp;
        private final String id;
        
        public Notification(String title, String message, NotificationType type) {
            this.title = title;
            this.message = message;
            this.type = type;
            this.timestamp = System.currentTimeMillis();
            this.id = generateId();
        }
        
        private String generateId() {
            return "notif_" + timestamp + "_" + (int)(Math.random() * 1000);
        }
        
        public String getTitle() { return title; }
        public String getMessage() { return message; }
        public NotificationType getType() { return type; }
        public long getTimestamp() { return timestamp; }
        public String getId() { return id; }
        
        @Override
        public String toString() {
            return String.format("Notification[%s] %s: %s", type, title, message);
        }
    }
    
    /**
     * Notification types
     */
    public enum NotificationType {
        INFO("Info"),
        WARNING("Warning"),
        ERROR("Error"),
        SUCCESS("Success");
        
        private final String displayName;
        
        NotificationType(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
    
    /**
     * Notification statistics
     */
    public static class NotificationStatistics {
        private final int queuedNotifications;
        private final boolean running;
        
        public NotificationStatistics(int queuedNotifications, boolean running) {
            this.queuedNotifications = queuedNotifications;
            this.running = running;
        }
        
        public int getQueuedNotifications() { return queuedNotifications; }
        public boolean isRunning() { return running; }
        
        @Override
        public String toString() {
            return String.format("NotificationStats[Queued: %d, Running: %s]", queuedNotifications, running);
        }
    }
}