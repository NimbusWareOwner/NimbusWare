package com.example.nimbusware.config;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.core.Module;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Enhanced configuration manager with typed properties and validation
 */
public class ConfigManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile = new File("nimbusware_config.json");
    private final Map<String, Object> config = new ConcurrentHashMap<>();
    private final Map<String, ConfigProperty<?>> properties = new ConcurrentHashMap<>();
    
    // Default configuration properties
    private final ConfigProperty<Boolean> debugMode = new ConfigProperty<>("debug.mode", false);
    private final ConfigProperty<String> logLevel = new ConfigProperty<>("log.level", "INFO", 
        value -> {
            try {
                Logger.Level.valueOf(value.toUpperCase());
            } catch (IllegalArgumentException e) {
                throw new IllegalArgumentException("Invalid log level: " + value);
            }
        });
    private final ConfigProperty<Integer> maxModules = new ConfigProperty<>("modules.max", 50, 
        ConfigProperty.Validators.range(1, 100));
    private final ConfigProperty<Boolean> autoSave = new ConfigProperty<>("config.auto_save", true);
    private final ConfigProperty<Integer> autoSaveInterval = new ConfigProperty<>("config.auto_save_interval", 300, 
        ConfigProperty.Validators.positive());
    
    public ConfigManager() {
        registerDefaultProperties();
    }
    
    private void registerDefaultProperties() {
        properties.put("debug.mode", debugMode);
        properties.put("log.level", logLevel);
        properties.put("modules.max", maxModules);
        properties.put("config.auto_save", autoSave);
        properties.put("config.auto_save_interval", autoSaveInterval);
    }
    
    /**
     * Load configuration from file
     */
    public void loadConfig() {
        try {
            if (configFile.exists()) {
                loadFromFile();
            } else {
                Logger.info("No config file found, using defaults");
                loadDefaults();
            }
            
            // Apply loaded configuration
            applyConfiguration();
            
            // Load module states
            loadModuleStates();
            
            Logger.info("Configuration loaded successfully");
        } catch (Exception e) {
            Logger.error("Failed to load configuration", e);
            loadDefaults();
        }
    }
    
    private void loadFromFile() throws IOException {
        try (FileReader reader = new FileReader(configFile)) {
            @SuppressWarnings("unchecked")
            Map<String, Object> loadedConfig = gson.fromJson(reader, Map.class);
            config.putAll(loadedConfig);
            Logger.info("Configuration loaded from " + configFile.getName());
        }
    }
    
    private void loadDefaults() {
        config.clear();
        for (ConfigProperty<?> property : properties.values()) {
            config.put(property.getKey(), property.getDefaultValue());
        }
    }
    
    private void applyConfiguration() {
        // Apply log level
        try {
            String levelStr = getString("log.level", "INFO");
            Logger.Level level = Logger.Level.valueOf(levelStr.toUpperCase());
            Logger.setLevel(level);
        } catch (Exception e) {
            Logger.warn("Invalid log level in config, using INFO", e);
        }
    }
    
    /**
     * Save configuration to file
     */
    public void saveConfig() {
        try {
            // Update config with current property values
            updateConfigFromProperties();
            
            // Save module states
            saveModuleStates();
            
            // Write to file
            try (FileWriter writer = new FileWriter(configFile)) {
                gson.toJson(config, writer);
                Logger.info("Configuration saved to " + configFile.getName());
            }
        } catch (Exception e) {
            Logger.error("Failed to save configuration", e);
        }
    }
    
    private void updateConfigFromProperties() {
        for (ConfigProperty<?> property : properties.values()) {
            config.put(property.getKey(), property.getValue());
        }
    }
    
    private void loadModuleStates() {
        try {
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            if (moduleManager != null) {
                for (Module module : moduleManager.getModules()) {
                    String key = "module." + module.getName().toLowerCase();
                    if (config.containsKey(key)) {
                        boolean enabled = getBoolean(key, false);
                        if (enabled && !module.isEnabled()) {
                            module.enable();
                        } else if (!enabled && module.isEnabled()) {
                            module.disable();
                        }
                    }
                }
            }
        } catch (Exception e) {
            Logger.error("Error loading module states", e);
        }
    }
    
    private void saveModuleStates() {
        try {
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            if (moduleManager != null) {
                for (Module module : moduleManager.getModules()) {
                    String key = "module." + module.getName().toLowerCase();
                    config.put(key, module.isEnabled());
                }
            }
        } catch (Exception e) {
            Logger.error("Error saving module states", e);
        }
    }
    
    // Generic property access
    public void set(String key, Object value) {
        config.put(key, value);
    }
    
    public Object get(String key) {
        return config.get(key);
    }
    
    public Object get(String key, Object defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }
    
    // Typed property access
    public boolean getBoolean(String key, boolean defaultValue) {
        Object value = config.get(key);
        if (value instanceof Boolean) {
            return (Boolean) value;
        }
        return defaultValue;
    }
    
    public int getInt(String key, int defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).intValue();
        }
        return defaultValue;
    }
    
    public double getDouble(String key, double defaultValue) {
        Object value = config.get(key);
        if (value instanceof Number) {
            return ((Number) value).doubleValue();
        }
        return defaultValue;
    }
    
    public String getString(String key, String defaultValue) {
        Object value = config.get(key);
        if (value instanceof String) {
            return (String) value;
        }
        return defaultValue;
    }
    
    // Property management
    public <T> void registerProperty(ConfigProperty<T> property) {
        properties.put(property.getKey(), property);
        if (config.containsKey(property.getKey())) {
            try {
                @SuppressWarnings("unchecked")
                T value = (T) config.get(property.getKey());
                property.setValue(value);
            } catch (Exception e) {
                Logger.warn("Failed to load property " + property.getKey() + ", using default", e);
            }
        }
    }
    
    @SuppressWarnings("unchecked")
    public <T> ConfigProperty<T> getProperty(String key) {
        return (ConfigProperty<T>) properties.get(key);
    }
    
    // Convenience methods for default properties
    public boolean isDebugMode() {
        return debugMode.getValue();
    }
    
    public void setDebugMode(boolean debugMode) {
        this.debugMode.setValue(debugMode);
    }
    
    public String getLogLevel() {
        return logLevel.getValue();
    }
    
    public void setLogLevel(String logLevel) {
        this.logLevel.setValue(logLevel);
    }
    
    public int getMaxModules() {
        return maxModules.getValue();
    }
    
    public void setMaxModules(int maxModules) {
        this.maxModules.setValue(maxModules);
    }
    
    public boolean isAutoSave() {
        return autoSave.getValue();
    }
    
    public void setAutoSave(boolean autoSave) {
        this.autoSave.setValue(autoSave);
    }
    
    public int getAutoSaveInterval() {
        return autoSaveInterval.getValue();
    }
    
    public void setAutoSaveInterval(int autoSaveInterval) {
        this.autoSaveInterval.setValue(autoSaveInterval);
    }
    
    /**
     * Reset all properties to defaults
     */
    public void resetToDefaults() {
        for (ConfigProperty<?> property : properties.values()) {
            property.reset();
        }
        Logger.info("Configuration reset to defaults");
    }
    
    /**
     * Get all registered properties
     * @return Map of property keys to properties
     */
    public Map<String, ConfigProperty<?>> getAllProperties() {
        return new HashMap<>(properties);
    }
}