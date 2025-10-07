package com.example.nimbusware.utils;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * Enhanced logging system with multiple levels, file output, and async processing
 */
public class Logger {
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String LOG_FILE = "nimbusware.log";
    
    public enum Level {
        DEBUG(0, "DEBUG"),
        INFO(1, "INFO"),
        WARN(2, "WARN"),
        ERROR(3, "ERROR");
        
        private final int priority;
        private final String name;
        
        Level(int priority, String name) {
            this.priority = priority;
            this.name = name;
        }
        
        public int getPriority() { return priority; }
        public String getName() { return name; }
    }
    
    private static Level currentLevel = Level.INFO;
    private static final BlockingQueue<LogEntry> logQueue = new LinkedBlockingQueue<>();
    private static final AtomicBoolean running = new AtomicBoolean(false);
    private static Thread logThread;
    
    static {
        startLogThread();
    }
    
    public static void setLevel(Level level) {
        currentLevel = level;
    }
    
    public static void debug(String message) {
        log(Level.DEBUG, message, null);
    }
    
    public static void debug(String message, Throwable throwable) {
        log(Level.DEBUG, message, throwable);
    }
    
    public static void info(String message) {
        log(Level.INFO, message, null);
    }
    
    public static void info(String message, Throwable throwable) {
        log(Level.INFO, message, throwable);
    }
    
    public static void warn(String message) {
        log(Level.WARN, message, null);
    }
    
    public static void warn(String message, Throwable throwable) {
        log(Level.WARN, message, throwable);
    }
    
    public static void error(String message) {
        log(Level.ERROR, message, null);
    }
    
    public static void error(String message, Throwable throwable) {
        log(Level.ERROR, message, throwable);
    }
    
    private static void log(Level level, String message, Throwable throwable) {
        if (level.getPriority() < currentLevel.getPriority()) {
            return;
        }
        
        LogEntry entry = new LogEntry(level, message, throwable, Thread.currentThread().getName());
        logQueue.offer(entry);
    }
    
    private static void startLogThread() {
        if (running.compareAndSet(false, true)) {
            logThread = new Thread(() -> {
                try (PrintWriter fileWriter = new PrintWriter(new FileWriter(LOG_FILE, true))) {
                    while (running.get() || !logQueue.isEmpty()) {
                        try {
                            LogEntry entry = logQueue.take();
                            String formattedMessage = formatLogEntry(entry);
                            
                            // Console output
                            System.out.println(formattedMessage);
                            
                            // File output
                            fileWriter.println(formattedMessage);
                            fileWriter.flush();
                            
                        } catch (InterruptedException e) {
                            Thread.currentThread().interrupt();
                            break;
                        }
                    }
                } catch (IOException e) {
                    System.err.println("Failed to write to log file: " + e.getMessage());
                }
            }, "Logger-Thread");
            
            logThread.setDaemon(true);
            logThread.start();
        }
    }
    
    public static void shutdown() {
        running.set(false);
        if (logThread != null) {
            logThread.interrupt();
        }
    }
    
    private static String formatLogEntry(LogEntry entry) {
        String timestamp = LocalDateTime.now().format(FORMATTER);
        String threadName = entry.getThreadName();
        String level = entry.getLevel().getName();
        String message = entry.getMessage();
        
        StringBuilder sb = new StringBuilder();
        sb.append("[").append(timestamp).append("] ");
        sb.append("[").append(threadName).append("] ");
        sb.append("[").append(level).append("] ");
        sb.append(message);
        
        if (entry.getThrowable() != null) {
            sb.append("\n").append(getStackTrace(entry.getThrowable()));
        }
        
        return sb.toString();
    }
    
    private static String getStackTrace(Throwable throwable) {
        java.io.StringWriter sw = new java.io.StringWriter();
        java.io.PrintWriter pw = new java.io.PrintWriter(sw);
        throwable.printStackTrace(pw);
        return sw.toString();
    }
    
    private static class LogEntry {
        private final Level level;
        private final String message;
        private final Throwable throwable;
        private final String threadName;
        
        public LogEntry(Level level, String message, Throwable throwable, String threadName) {
            this.level = level;
            this.message = message;
            this.throwable = throwable;
            this.threadName = threadName;
        }
        
        public Level getLevel() { return level; }
        public String getMessage() { return message; }
        public Throwable getThrowable() { return throwable; }
        public String getThreadName() { return threadName; }
    }
}