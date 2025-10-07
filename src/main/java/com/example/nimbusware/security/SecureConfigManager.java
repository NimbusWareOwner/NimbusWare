package com.example.nimbusware.security;

import com.example.nimbusware.config.ConfigManager;
import com.example.nimbusware.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Secure configuration manager with encryption support
 */
public class SecureConfigManager extends ConfigManager {
    private final EncryptionManager encryptionManager;
    private final File secureConfigFile;
    private final Map<String, Boolean> encryptedKeys = new ConcurrentHashMap<>();
    
    public SecureConfigManager() {
        super();
        this.encryptionManager = EncryptionManager.getInstance();
        this.secureConfigFile = new File("nimbusware_secure_config.json");
        
        // Mark sensitive keys as encrypted
        markAsEncrypted("account.password");
        markAsEncrypted("account.token");
        markAsEncrypted("api.key");
        markAsEncrypted("database.password");
        markAsEncrypted("encryption.key");
    }
    
    /**
     * Mark a configuration key as requiring encryption
     * @param key Configuration key
     */
    public void markAsEncrypted(String key) {
        encryptedKeys.put(key, true);
    }
    
    /**
     * Check if a key should be encrypted
     * @param key Configuration key
     * @return true if key should be encrypted
     */
    public boolean isEncryptedKey(String key) {
        return encryptedKeys.getOrDefault(key, false);
    }
    
    @Override
    public void loadConfig() {
        try {
            // Load regular config first
            super.loadConfig();
            
            // Load secure config if exists
            if (secureConfigFile.exists()) {
                loadSecureConfig();
            }
            
            Logger.info("Secure configuration loaded successfully");
        } catch (Exception e) {
            Logger.error("Failed to load secure configuration", e);
            super.loadConfig(); // Fallback to regular config
        }
    }
    
    @Override
    public void saveConfig() {
        try {
            // Save regular config
            super.saveConfig();
            
            // Save secure config
            saveSecureConfig();
            
            Logger.info("Secure configuration saved successfully");
        } catch (Exception e) {
            Logger.error("Failed to save secure configuration", e);
            super.saveConfig(); // Fallback to regular config
        }
    }
    
    @Override
    public void set(String key, Object value) {
        if (isEncryptedKey(key) && value instanceof String) {
            // Encrypt sensitive values
            String encryptedValue = encryptionManager.encrypt((String) value);
            super.set(key, encryptedValue);
        } else {
            super.set(key, value);
        }
    }
    
    @Override
    public String getString(String key, String defaultValue) {
        Object value = get(key);
        if (value instanceof String) {
            String stringValue = (String) value;
            
            if (isEncryptedKey(key) && encryptionManager.isEncrypted(stringValue)) {
                // Decrypt sensitive values
                try {
                    return encryptionManager.decrypt(stringValue);
                } catch (Exception e) {
                    Logger.warn("Failed to decrypt value for key: " + key, e);
                    return defaultValue;
                }
            } else {
                return stringValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Set a secure string value (will be encrypted)
     * @param key Configuration key
     * @param value Value to encrypt and store
     */
    public void setSecureString(String key, String value) {
        if (value != null) {
            String encryptedValue = encryptionManager.encrypt(value);
            super.set(key, encryptedValue);
            markAsEncrypted(key);
        }
    }
    
    /**
     * Get a secure string value (will be decrypted)
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Decrypted value
     */
    public String getSecureString(String key, String defaultValue) {
        Object value = get(key);
        if (value instanceof String) {
            String stringValue = (String) value;
            
            if (encryptionManager.isEncrypted(stringValue)) {
                try {
                    return encryptionManager.decrypt(stringValue);
                } catch (Exception e) {
                    Logger.warn("Failed to decrypt secure value for key: " + key, e);
                    return defaultValue;
                }
            } else {
                return stringValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Check if a secure value exists and is encrypted
     * @param key Configuration key
     * @return true if secure value exists
     */
    public boolean hasSecureValue(String key) {
        Object value = get(key);
        return value instanceof String && encryptionManager.isEncrypted((String) value);
    }
    
    private void loadSecureConfig() {
        try (FileReader reader = new FileReader(secureConfigFile)) {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().create();
            @SuppressWarnings("unchecked")
            Map<String, Object> secureConfig = gson.fromJson(reader, Map.class);
            
            // Decrypt and load secure values
            for (Map.Entry<String, Object> entry : secureConfig.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                
                if (value instanceof String && encryptionManager.isEncrypted((String) value)) {
                    try {
                        String decryptedValue = encryptionManager.decrypt((String) value);
                        super.set(key, decryptedValue);
                        markAsEncrypted(key);
                    } catch (Exception e) {
                        Logger.warn("Failed to decrypt secure config value: " + key, e);
                    }
                }
            }
            
            Logger.info("Secure configuration loaded from " + secureConfigFile.getName());
        } catch (IOException e) {
            Logger.error("Failed to load secure config file", e);
        }
    }
    
    private void saveSecureConfig() {
        try (FileWriter writer = new FileWriter(secureConfigFile)) {
            Map<String, Object> secureConfig = new HashMap<>();
            
            // Collect and encrypt sensitive values
            for (String key : encryptedKeys.keySet()) {
                Object value = get(key);
                if (value instanceof String) {
                    String stringValue = (String) value;
                    if (!encryptionManager.isEncrypted(stringValue)) {
                        // Encrypt if not already encrypted
                        stringValue = encryptionManager.encrypt(stringValue);
                    }
                    secureConfig.put(key, stringValue);
                }
            }
            
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            gson.toJson(secureConfig, writer);
            
            Logger.info("Secure configuration saved to " + secureConfigFile.getName());
        } catch (IOException e) {
            Logger.error("Failed to save secure config file", e);
        }
    }
    
    /**
     * Migrate regular config values to secure storage
     */
    public void migrateToSecureStorage() {
        Logger.info("Starting migration to secure storage...");
        
        int migratedCount = 0;
        for (String key : encryptedKeys.keySet()) {
            Object value = get(key);
            if (value instanceof String) {
                String stringValue = (String) value;
                if (!encryptionManager.isEncrypted(stringValue)) {
                    setSecureString(key, stringValue);
                    migratedCount++;
                }
            }
        }
        
        Logger.info("Migrated " + migratedCount + " values to secure storage");
    }
    
    /**
     * Get encryption status for all keys
     * @return Map of key names to encryption status
     */
    public Map<String, Boolean> getEncryptionStatus() {
        Map<String, Boolean> status = new HashMap<>();
        
        for (String key : encryptedKeys.keySet()) {
            Object value = get(key);
            boolean isEncrypted = value instanceof String && 
                                encryptionManager.isEncrypted((String) value);
            status.put(key, isEncrypted);
        }
        
        return status;
    }
    
    /**
     * Re-encrypt all secure values with a new key
     * @param newKeyName New key name to use
     */
    public void reEncryptAll(String newKeyName) {
        Logger.info("Re-encrypting all secure values with key: " + newKeyName);
        
        int reEncryptedCount = 0;
        for (String key : encryptedKeys.keySet()) {
            Object value = get(key);
            if (value instanceof String) {
                String stringValue = (String) value;
                
                try {
                    // Decrypt with current key
                    String decryptedValue = encryptionManager.decrypt(stringValue);
                    // Encrypt with new key
                    String newEncryptedValue = encryptionManager.encrypt(decryptedValue, newKeyName);
                    super.set(key, newEncryptedValue);
                    reEncryptedCount++;
                } catch (Exception e) {
                    Logger.warn("Failed to re-encrypt key: " + key, e);
                }
            }
        }
        
        Logger.info("Re-encrypted " + reEncryptedCount + " values");
    }
}