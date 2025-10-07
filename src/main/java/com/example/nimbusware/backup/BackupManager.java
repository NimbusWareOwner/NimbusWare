package com.example.nimbusware.backup;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.io.*;
import java.nio.file.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * Advanced backup system for NimbusWare
 */
public class BackupManager {
    private static volatile BackupManager instance;
    private final File backupDirectory;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "BackupManager-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private final int maxBackups = 10;
    private final long backupIntervalHours = 24;
    
    private BackupManager() {
        this.backupDirectory = new File("backups");
        if (!backupDirectory.exists()) {
            backupDirectory.mkdirs();
        }
        
        startAutoBackup();
        Logger.info("Backup manager initialized");
    }
    
    public static BackupManager getInstance() {
        if (instance == null) {
            synchronized (BackupManager.class) {
                if (instance == null) {
                    instance = new BackupManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Create a full backup
     * @return Backup info
     */
    public BackupInfo createBackup() {
        return createBackup("manual");
    }
    
    /**
     * Create a backup with custom name
     * @param backupName Backup name
     * @return Backup info
     */
    public BackupInfo createBackup(String backupName) {
        try {
            String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd_HH-mm-ss"));
            String fileName = backupName + "_" + timestamp + ".zip";
            File backupFile = new File(backupDirectory, fileName);
            
            Logger.info("Creating backup: " + fileName);
            
            // Create backup
            try (ZipOutputStream zipOut = new ZipOutputStream(new FileOutputStream(backupFile))) {
                // Backup configuration files
                backupConfigFiles(zipOut);
                
                // Backup profiles
                backupProfiles(zipOut);
                
                // Backup logs
                backupLogs(zipOut);
                
                // Backup modules
                backupModules(zipOut);
                
                // Backup plugins
                backupPlugins(zipOut);
            }
            
            // Clean old backups
            cleanOldBackups();
            
            BackupInfo backupInfo = new BackupInfo(fileName, backupFile.length(), System.currentTimeMillis(), backupName);
            Logger.info("Backup created successfully: " + fileName + " (" + formatFileSize(backupFile.length()) + ")");
            
            return backupInfo;
            
        } catch (Exception e) {
            Logger.error("Failed to create backup", e);
            return null;
        }
    }
    
    /**
     * Restore from backup
     * @param backupFileName Backup file name
     * @return true if restored successfully
     */
    public boolean restoreBackup(String backupFileName) {
        try {
            File backupFile = new File(backupDirectory, backupFileName);
            if (!backupFile.exists()) {
                Logger.warn("Backup file not found: " + backupFileName);
                return false;
            }
            
            Logger.info("Restoring from backup: " + backupFileName);
            
            // Create temporary directory for extraction
            File tempDir = new File("temp_restore_" + System.currentTimeMillis());
            tempDir.mkdirs();
            
            try {
                // Extract backup
                extractZip(backupFile, tempDir);
                
                // Restore files
                restoreConfigFiles(tempDir);
                restoreProfiles(tempDir);
                restoreModules(tempDir);
                restorePlugins(tempDir);
                
                Logger.info("Backup restored successfully: " + backupFileName);
                return true;
                
            } finally {
                // Clean up temporary directory
                deleteDirectory(tempDir);
            }
            
        } catch (Exception e) {
            Logger.error("Failed to restore backup: " + backupFileName, e);
            return false;
        }
    }
    
    /**
     * List available backups
     * @return List of backup info
     */
    public List<BackupInfo> listBackups() {
        List<BackupInfo> backups = new ArrayList<>();
        
        File[] backupFiles = backupDirectory.listFiles((dir, name) -> name.endsWith(".zip"));
        if (backupFiles != null) {
            for (File backupFile : backupFiles) {
                BackupInfo backupInfo = new BackupInfo(
                    backupFile.getName(),
                    backupFile.length(),
                    backupFile.lastModified(),
                    extractBackupName(backupFile.getName())
                );
                backups.add(backupInfo);
            }
        }
        
        // Sort by date (newest first)
        backups.sort((a, b) -> Long.compare(b.getTimestamp(), a.getTimestamp()));
        
        return backups;
    }
    
    /**
     * Delete a backup
     * @param backupFileName Backup file name
     * @return true if deleted successfully
     */
    public boolean deleteBackup(String backupFileName) {
        try {
            File backupFile = new File(backupDirectory, backupFileName);
            if (backupFile.exists()) {
                boolean deleted = backupFile.delete();
                if (deleted) {
                    Logger.info("Backup deleted: " + backupFileName);
                }
                return deleted;
            } else {
                Logger.warn("Backup file not found: " + backupFileName);
                return false;
            }
        } catch (Exception e) {
            Logger.error("Failed to delete backup: " + backupFileName, e);
            return false;
        }
    }
    
    private void backupConfigFiles(ZipOutputStream zipOut) throws IOException {
        String[] configFiles = {
            "nimbusware_config.json",
            "nimbusware_secure_config.json",
            "nimbusware.log"
        };
        
        for (String fileName : configFiles) {
            File file = new File(fileName);
            if (file.exists()) {
                addFileToZip(zipOut, file, "config/" + fileName);
            }
        }
    }
    
    private void backupProfiles(ZipOutputStream zipOut) throws IOException {
        File profilesDir = new File("profiles");
        if (profilesDir.exists()) {
            addDirectoryToZip(zipOut, profilesDir, "profiles/");
        }
    }
    
    private void backupLogs(ZipOutputStream zipOut) throws IOException {
        File logsDir = new File("logs");
        if (logsDir.exists()) {
            addDirectoryToZip(zipOut, logsDir, "logs/");
        }
    }
    
    private void backupModules(ZipOutputStream zipOut) throws IOException {
        // Backup module configurations
        File modulesDir = new File("modules");
        if (modulesDir.exists()) {
            addDirectoryToZip(zipOut, modulesDir, "modules/");
        }
    }
    
    private void backupPlugins(ZipOutputStream zipOut) throws IOException {
        File pluginsDir = new File("plugins");
        if (pluginsDir.exists()) {
            addDirectoryToZip(zipOut, pluginsDir, "plugins/");
        }
    }
    
    private void restoreConfigFiles(File tempDir) throws IOException {
        File configDir = new File(tempDir, "config");
        if (configDir.exists()) {
            File[] files = configDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    Files.copy(file.toPath(), new File(file.getName()).toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    
    private void restoreProfiles(File tempDir) throws IOException {
        File profilesDir = new File(tempDir, "profiles");
        if (profilesDir.exists()) {
            File targetDir = new File("profiles");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            copyDirectory(profilesDir, targetDir);
        }
    }
    
    private void restoreModules(File tempDir) throws IOException {
        File modulesDir = new File(tempDir, "modules");
        if (modulesDir.exists()) {
            File targetDir = new File("modules");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            copyDirectory(modulesDir, targetDir);
        }
    }
    
    private void restorePlugins(File tempDir) throws IOException {
        File pluginsDir = new File(tempDir, "plugins");
        if (pluginsDir.exists()) {
            File targetDir = new File("plugins");
            if (!targetDir.exists()) {
                targetDir.mkdirs();
            }
            copyDirectory(pluginsDir, targetDir);
        }
    }
    
    private void addFileToZip(ZipOutputStream zipOut, File file, String entryName) throws IOException {
        try (FileInputStream fis = new FileInputStream(file)) {
            ZipEntry zipEntry = new ZipEntry(entryName);
            zipOut.putNextEntry(zipEntry);
            
            byte[] buffer = new byte[1024];
            int length;
            while ((length = fis.read(buffer)) > 0) {
                zipOut.write(buffer, 0, length);
            }
            
            zipOut.closeEntry();
        }
    }
    
    private void addDirectoryToZip(ZipOutputStream zipOut, File directory, String basePath) throws IOException {
        File[] files = directory.listFiles();
        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    addDirectoryToZip(zipOut, file, basePath + file.getName() + "/");
                } else {
                    addFileToZip(zipOut, file, basePath + file.getName());
                }
            }
        }
    }
    
    private void extractZip(File zipFile, File destDir) throws IOException {
        try (java.util.zip.ZipInputStream zipIn = new java.util.zip.ZipInputStream(new FileInputStream(zipFile))) {
            java.util.zip.ZipEntry entry = zipIn.getNextEntry();
            
            while (entry != null) {
                File file = new File(destDir, entry.getName());
                
                if (entry.isDirectory()) {
                    file.mkdirs();
                } else {
                    file.getParentFile().mkdirs();
                    try (FileOutputStream fos = new FileOutputStream(file)) {
                        byte[] buffer = new byte[1024];
                        int length;
                        while ((length = zipIn.read(buffer)) > 0) {
                            fos.write(buffer, 0, length);
                        }
                    }
                }
                
                zipIn.closeEntry();
                entry = zipIn.getNextEntry();
            }
        }
    }
    
    private void copyDirectory(File sourceDir, File targetDir) throws IOException {
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        
        File[] files = sourceDir.listFiles();
        if (files != null) {
            for (File file : files) {
                File targetFile = new File(targetDir, file.getName());
                if (file.isDirectory()) {
                    copyDirectory(file, targetFile);
                } else {
                    Files.copy(file.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                }
            }
        }
    }
    
    private void cleanOldBackups() {
        List<BackupInfo> backups = listBackups();
        
        if (backups.size() > maxBackups) {
            List<BackupInfo> toDelete = backups.subList(maxBackups, backups.size());
            for (BackupInfo backup : toDelete) {
                deleteBackup(backup.getFileName());
            }
        }
    }
    
    private void deleteDirectory(File directory) {
        if (directory.exists()) {
            File[] files = directory.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isDirectory()) {
                        deleteDirectory(file);
                    } else {
                        file.delete();
                    }
                }
            }
            directory.delete();
        }
    }
    
    private String extractBackupName(String fileName) {
        int underscoreIndex = fileName.lastIndexOf('_');
        if (underscoreIndex > 0) {
            return fileName.substring(0, underscoreIndex);
        }
        return "unknown";
    }
    
    private String formatFileSize(long bytes) {
        if (bytes < 1024) return bytes + " B";
        int exp = (int) (Math.log(bytes) / Math.log(1024));
        String pre = "KMGTPE".charAt(exp - 1) + "";
        return String.format("%.1f %sB", bytes / Math.pow(1024, exp), pre);
    }
    
    private void startAutoBackup() {
        // Create backup every 24 hours
        scheduler.scheduleAtFixedRate(() -> {
            try {
                createBackup("auto");
            } catch (Exception e) {
                Logger.error("Error during auto backup", e);
            }
        }, backupIntervalHours, backupIntervalHours, TimeUnit.HOURS);
    }
    
    /**
     * Shutdown the backup manager
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
     * Backup information
     */
    public static class BackupInfo {
        private final String fileName;
        private final long size;
        private final long timestamp;
        private final String backupName;
        
        public BackupInfo(String fileName, long size, long timestamp, String backupName) {
            this.fileName = fileName;
            this.size = size;
            this.timestamp = timestamp;
            this.backupName = backupName;
        }
        
        public String getFileName() { return fileName; }
        public long getSize() { return size; }
        public long getTimestamp() { return timestamp; }
        public String getBackupName() { return backupName; }
        
        public String getFormattedSize() {
            if (size < 1024) return size + " B";
            int exp = (int) (Math.log(size) / Math.log(1024));
            String pre = "KMGTPE".charAt(exp - 1) + "";
            return String.format("%.1f %sB", size / Math.pow(1024, exp), pre);
        }
        
        public String getFormattedDate() {
            return new java.util.Date(timestamp).toString();
        }
        
        @Override
        public String toString() {
            return String.format("Backup[%s] %s (%s) - %s", backupName, fileName, getFormattedSize(), getFormattedDate());
        }
    }
}