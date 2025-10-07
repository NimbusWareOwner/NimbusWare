package com.example.nimbusware.security;

import com.example.nimbusware.utils.Logger;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.util.Base64;
import java.util.concurrent.ConcurrentHashMap;
import java.util.Map;

/**
 * Advanced encryption manager for securing sensitive data
 */
public class EncryptionManager {
    private static volatile EncryptionManager instance;
    private static final String ALGORITHM = "AES";
    private static final String TRANSFORMATION = "AES/GCM/NoPadding";
    private static final int GCM_IV_LENGTH = 12;
    private static final int GCM_TAG_LENGTH = 16;
    private static final int KEY_LENGTH = 256;
    
    private final Map<String, SecretKey> keyCache = new ConcurrentHashMap<>();
    private final SecureRandom secureRandom = new SecureRandom();
    
    private EncryptionManager() {
        // Initialize with default key
        generateDefaultKey();
    }
    
    public static EncryptionManager getInstance() {
        if (instance == null) {
            synchronized (EncryptionManager.class) {
                if (instance == null) {
                    instance = new EncryptionManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Generate a new encryption key
     * @param keyName Name for the key
     * @return Generated key
     */
    public SecretKey generateKey(String keyName) {
        try {
            KeyGenerator keyGenerator = KeyGenerator.getInstance(ALGORITHM);
            keyGenerator.init(KEY_LENGTH);
            SecretKey key = keyGenerator.generateKey();
            keyCache.put(keyName, key);
            Logger.debug("Generated new encryption key: " + keyName);
            return key;
        } catch (Exception e) {
            Logger.error("Failed to generate encryption key: " + keyName, e);
            throw new SecurityException("Key generation failed", e);
        }
    }
    
    /**
     * Get or create a key
     * @param keyName Key name
     * @return Secret key
     */
    public SecretKey getKey(String keyName) {
        return keyCache.computeIfAbsent(keyName, this::generateKey);
    }
    
    /**
     * Encrypt data using the default key
     * @param data Data to encrypt
     * @return Encrypted data as Base64 string
     */
    public String encrypt(String data) {
        return encrypt(data, "default");
    }
    
    /**
     * Encrypt data using specified key
     * @param data Data to encrypt
     * @param keyName Key name
     * @return Encrypted data as Base64 string
     */
    public String encrypt(String data, String keyName) {
        if (data == null || data.isEmpty()) {
            return data;
        }
        
        try {
            SecretKey key = getKey(keyName);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            
            // Generate random IV
            byte[] iv = new byte[GCM_IV_LENGTH];
            secureRandom.nextBytes(iv);
            
            // Initialize cipher
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.ENCRYPT_MODE, key, parameterSpec);
            
            // Encrypt data
            byte[] encryptedData = cipher.doFinal(data.getBytes(StandardCharsets.UTF_8));
            
            // Combine IV and encrypted data
            byte[] encryptedWithIv = new byte[GCM_IV_LENGTH + encryptedData.length];
            System.arraycopy(iv, 0, encryptedWithIv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedData, 0, encryptedWithIv, GCM_IV_LENGTH, encryptedData.length);
            
            // Return Base64 encoded result
            return Base64.getEncoder().encodeToString(encryptedWithIv);
            
        } catch (Exception e) {
            Logger.error("Encryption failed for key: " + keyName, e);
            throw new SecurityException("Encryption failed", e);
        }
    }
    
    /**
     * Decrypt data using the default key
     * @param encryptedData Encrypted data as Base64 string
     * @return Decrypted data
     */
    public String decrypt(String encryptedData) {
        return decrypt(encryptedData, "default");
    }
    
    /**
     * Decrypt data using specified key
     * @param encryptedData Encrypted data as Base64 string
     * @param keyName Key name
     * @return Decrypted data
     */
    public String decrypt(String encryptedData, String keyName) {
        if (encryptedData == null || encryptedData.isEmpty()) {
            return encryptedData;
        }
        
        try {
            SecretKey key = getKey(keyName);
            Cipher cipher = Cipher.getInstance(TRANSFORMATION);
            
            // Decode Base64
            byte[] encryptedWithIv = Base64.getDecoder().decode(encryptedData);
            
            // Extract IV and encrypted data
            byte[] iv = new byte[GCM_IV_LENGTH];
            byte[] encrypted = new byte[encryptedWithIv.length - GCM_IV_LENGTH];
            System.arraycopy(encryptedWithIv, 0, iv, 0, GCM_IV_LENGTH);
            System.arraycopy(encryptedWithIv, GCM_IV_LENGTH, encrypted, 0, encrypted.length);
            
            // Initialize cipher
            GCMParameterSpec parameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, iv);
            cipher.init(Cipher.DECRYPT_MODE, key, parameterSpec);
            
            // Decrypt data
            byte[] decryptedData = cipher.doFinal(encrypted);
            return new String(decryptedData, StandardCharsets.UTF_8);
            
        } catch (Exception e) {
            Logger.error("Decryption failed for key: " + keyName, e);
            throw new SecurityException("Decryption failed", e);
        }
    }
    
    /**
     * Encrypt a map of key-value pairs
     * @param data Map to encrypt
     * @param keyName Key name
     * @return Encrypted map
     */
    public Map<String, String> encryptMap(Map<String, String> data, String keyName) {
        Map<String, String> encryptedMap = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, String> entry : data.entrySet()) {
            String encryptedKey = encrypt(entry.getKey(), keyName);
            String encryptedValue = encrypt(entry.getValue(), keyName);
            encryptedMap.put(encryptedKey, encryptedValue);
        }
        
        return encryptedMap;
    }
    
    /**
     * Decrypt a map of key-value pairs
     * @param encryptedData Encrypted map
     * @param keyName Key name
     * @return Decrypted map
     */
    public Map<String, String> decryptMap(Map<String, String> encryptedData, String keyName) {
        Map<String, String> decryptedMap = new ConcurrentHashMap<>();
        
        for (Map.Entry<String, String> entry : encryptedData.entrySet()) {
            String decryptedKey = decrypt(entry.getKey(), keyName);
            String decryptedValue = decrypt(entry.getValue(), keyName);
            decryptedMap.put(decryptedKey, decryptedValue);
        }
        
        return decryptedMap;
    }
    
    /**
     * Check if a string is encrypted (Base64 and contains IV)
     * @param data String to check
     * @return true if appears to be encrypted
     */
    public boolean isEncrypted(String data) {
        if (data == null || data.isEmpty()) {
            return false;
        }
        
        try {
            byte[] decoded = Base64.getDecoder().decode(data);
            return decoded.length > GCM_IV_LENGTH;
        } catch (Exception e) {
            return false;
        }
    }
    
    /**
     * Generate a secure random string
     * @param length String length
     * @return Random string
     */
    public String generateSecureRandomString(int length) {
        byte[] bytes = new byte[length];
        secureRandom.nextBytes(bytes);
        return Base64.getEncoder().encodeToString(bytes);
    }
    
    /**
     * Hash a password using PBKDF2
     * @param password Password to hash
     * @param salt Salt for hashing
     * @return Hashed password
     */
    public String hashPassword(String password, String salt) {
        try {
            javax.crypto.spec.PBEKeySpec spec = new javax.crypto.spec.PBEKeySpec(
                password.toCharArray(), 
                salt.getBytes(StandardCharsets.UTF_8), 
                65536, 
                256
            );
            javax.crypto.SecretKeyFactory factory = javax.crypto.SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
            byte[] hash = factory.generateSecret(spec).getEncoded();
            return Base64.getEncoder().encodeToString(hash);
        } catch (Exception e) {
            Logger.error("Password hashing failed", e);
            throw new SecurityException("Password hashing failed", e);
        }
    }
    
    /**
     * Verify a password against its hash
     * @param password Password to verify
     * @param salt Salt used for hashing
     * @param hash Expected hash
     * @return true if password matches
     */
    public boolean verifyPassword(String password, String salt, String hash) {
        try {
            String computedHash = hashPassword(password, salt);
            return computedHash.equals(hash);
        } catch (Exception e) {
            Logger.error("Password verification failed", e);
            return false;
        }
    }
    
    private void generateDefaultKey() {
        try {
            generateKey("default");
        } catch (Exception e) {
            Logger.error("Failed to generate default encryption key", e);
        }
    }
    
    /**
     * Clear all cached keys (for security)
     */
    public void clearKeyCache() {
        keyCache.clear();
        Logger.info("Encryption key cache cleared");
    }
    
    /**
     * Get key information
     * @return Map of key names to their status
     */
    public Map<String, String> getKeyInfo() {
        Map<String, String> info = new ConcurrentHashMap<>();
        keyCache.forEach((name, key) -> {
            info.put(name, "Key length: " + key.getEncoded().length * 8 + " bits");
        });
        return info;
    }
}