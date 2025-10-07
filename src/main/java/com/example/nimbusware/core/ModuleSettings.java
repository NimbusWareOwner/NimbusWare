package com.example.nimbusware.core;

import com.example.nimbusware.utils.Logger;
import com.example.nimbusware.utils.ValidationUtils;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Advanced module settings system with validation and persistence
 */
public class ModuleSettings {
    private final Map<String, Object> settings = new ConcurrentHashMap<>();
    private final Map<String, SettingValidator> validators = new ConcurrentHashMap<>();
    private final String moduleName;
    
    public ModuleSettings(String moduleName) {
        this.moduleName = moduleName;
        registerDefaultValidators();
    }
    
    /**
     * Set a setting value with validation
     * @param key Setting key
     * @param value Setting value
     * @param validator Validator for the value
     */
    public void set(String key, Object value, SettingValidator validator) {
        if (validator != null) {
            try {
                validator.validate(value);
            } catch (IllegalArgumentException e) {
                Logger.warn("Invalid setting value for " + moduleName + "." + key + ": " + e.getMessage());
                return;
            }
        }
        
        settings.put(key, value);
        Logger.debug("Setting " + moduleName + "." + key + " = " + value);
    }
    
    /**
     * Set a setting value with default validator
     * @param key Setting key
     * @param value Setting value
     */
    public void set(String key, Object value) {
        SettingValidator validator = validators.get(key);
        set(key, value, validator);
    }
    
    /**
     * Get a setting value
     * @param key Setting key
     * @param defaultValue Default value if not found
     * @return Setting value
     */
    @SuppressWarnings("unchecked")
    public <T> T get(String key, T defaultValue) {
        Object value = settings.get(key);
        if (value != null) {
            try {
                return (T) value;
            } catch (ClassCastException e) {
                Logger.warn("Type mismatch for setting " + moduleName + "." + key);
                return defaultValue;
            }
        }
        return defaultValue;
    }
    
    /**
     * Get a boolean setting
     * @param key Setting key
     * @param defaultValue Default value
     * @return Boolean value
     */
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = settings.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        } else if (value instanceof String) {
            return Boolean.parseBoolean((String) value);
        }
        return defaultValue;
    }
    
    /**
     * Get an integer setting
     * @param key Setting key
     * @param defaultValue Default value
     * @return Integer value
     */
    public int getInt(String key, int defaultValue) {
        Object value = settings.get(key);
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
     * Get a double setting
     * @param key Setting key
     * @param defaultValue Default value
     * @return Double value
     */
    public double getDouble(String key, double defaultValue) {
        Object value = settings.get(key);
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
     * Get a string setting
     * @param key Setting key
     * @param defaultValue Default value
     * @return String value
     */
    public String getString(String key, String defaultValue) {
        Object value = settings.get(key);
        return value != null ? value.toString() : defaultValue;
    }
    
    /**
     * Check if a setting exists
     * @param key Setting key
     * @return true if setting exists
     */
    public boolean hasKey(String key) {
        return settings.containsKey(key);
    }
    
    /**
     * Get all settings
     * @return Map of all settings
     */
    public Map<String, Object> getAll() {
        return new HashMap<>(settings);
    }
    
    /**
     * Load settings from map
     * @param settingsMap Settings map
     */
    public void loadFromMap(Map<String, Object> settingsMap) {
        for (Map.Entry<String, Object> entry : settingsMap.entrySet()) {
            String key = entry.getKey();
            Object value = entry.getValue();
            
            SettingValidator validator = validators.get(key);
            if (validator != null) {
                try {
                    validator.validate(value);
                    settings.put(key, value);
                } catch (IllegalArgumentException e) {
                    Logger.warn("Invalid setting value for " + moduleName + "." + key + ": " + e.getMessage());
                }
            } else {
                settings.put(key, value);
            }
        }
    }
    
    /**
     * Reset all settings to defaults
     */
    public void reset() {
        settings.clear();
        Logger.info("Settings reset for module: " + moduleName);
    }
    
    /**
     * Register a validator for a setting
     * @param key Setting key
     * @param validator Validator
     */
    public void registerValidator(String key, SettingValidator validator) {
        validators.put(key, validator);
    }
    
    private void registerDefaultValidators() {
        // Range validators
        registerValidator("range", value -> {
            if (value instanceof Number) {
                double range = ((Number) value).doubleValue();
                ValidationUtils.validateRange(range, 0.1, 10.0, "Range");
            }
        });
        
        registerValidator("cps", value -> {
            if (value instanceof Number) {
                int cps = ((Number) value).intValue();
                ValidationUtils.validateRange(cps, 1, 20, "CPS");
            }
        });
        
        registerValidator("delay", value -> {
            if (value instanceof Number) {
                int delay = ((Number) value).intValue();
                ValidationUtils.validateRange(delay, 1, 1000, "Delay");
            }
        });
        
        registerValidator("speed", value -> {
            if (value instanceof Number) {
                double speed = ((Number) value).doubleValue();
                ValidationUtils.validateRange(speed, 0.1, 5.0, "Speed");
            }
        });
    }
    
    /**
     * Functional interface for setting validation
     */
    @FunctionalInterface
    public interface SettingValidator {
        void validate(Object value) throws IllegalArgumentException;
    }
}