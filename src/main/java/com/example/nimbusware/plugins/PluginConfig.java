package com.example.nimbusware.plugins;

import java.util.HashMap;
import java.util.Map;

/**
 * Configuration class for plugins
 */
public class PluginConfig {
    private final Map<String, Object> config = new HashMap<>();
    
    /**
     * Set a configuration value
     * @param key Configuration key
     * @param value Configuration value
     */
    public void set(String key, Object value) {
        config.put(key, value);
    }
    
    /**
     * Get a configuration value
     * @param key Configuration key
     * @param defaultValue Default value if not found
     * @return Configuration value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = config.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a string configuration value
     * @param key Configuration key
     * @param defaultValue Default value
     * @return String value
     */
    public String getString(String key, String defaultValue) {
        Object value = config.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Get a boolean configuration value
     * @param key Configuration key
     * @param defaultValue Default value
     * @return Boolean value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return defaultValue;
    }
    
    /**
     * Get an integer configuration value
     * @param key Configuration key
     * @param defaultValue Default value
     * @return Integer value
     */
    public int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        } else if (value instanceof String) {
            try {
                return Integer.parseInt((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a double configuration value
     * @param key Configuration key
     * @param defaultValue Default value
     * @return Double value
     */
    public double getDouble(String key, double defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        } else if (value instanceof String) {
            try {
                return Double.parseDouble((String) value);
            } catch (NumberFormatException e) {
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Check if a key exists
     * @param key Configuration key
     * @return true if key exists
     */
    public boolean hasKey(String key) {
        return config.containsKey(key);
    }
    
    /**
     * Get all configuration keys
     * @return Set of configuration keys
     */
    public java.util.Set<String> getKeys() {
        return config.keySet();
    }
    
    /**
     * Get all configuration values
     * @return Map of all configuration
     */
    public Map<String, Object> getAll() {
        return new HashMap<>(config);
    }
    
    /**
     * Load configuration from map
     * @param configMap Configuration map
     */
    public void loadFromMap(Map<String, Object> configMap) {
        config.putAll(configMap);
    }
    
    /**
     * Clear all configuration
     */
    public void clear() {
        config.clear();
    }
}