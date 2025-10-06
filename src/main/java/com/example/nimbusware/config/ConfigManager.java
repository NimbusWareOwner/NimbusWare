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

public class ConfigManager {
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    private final File configFile = new File("cheatclient_config.json");
    private Map<String, Object> config = new HashMap<>();
    
    public void loadConfig() {
        if (configFile.exists()) {
            try (FileReader reader = new FileReader(configFile)) {
                @SuppressWarnings("unchecked")
                Map<String, Object> loadedConfig = gson.fromJson(reader, Map.class);
                config = loadedConfig;
                Logger.info("Configuration loaded from " + configFile.getName());
            } catch (IOException e) {
                Logger.error("Failed to load config: " + e.getMessage());
                config = new HashMap<>();
            }
        } else {
            Logger.info("No config file found, using defaults");
            config = new HashMap<>();
        }
        
        // Load module states
        loadModuleStates();
    }
    
    public void saveConfig() {
        try (FileWriter writer = new FileWriter(configFile)) {
            gson.toJson(config, writer);
            Logger.info("Configuration saved to " + configFile.getName());
        } catch (IOException e) {
            Logger.error("Failed to save config: " + e.getMessage());
        }
    }
    
    private void loadModuleStates() {
        ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
        for (Module module : moduleManager.getModules()) {
            String key = "module." + module.getName().toLowerCase();
            if (config.containsKey(key)) {
                boolean enabled = (Boolean) config.get(key);
                if (enabled) {
                    module.enable();
                }
            }
        }
    }
    
    public void saveModuleStates() {
        ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
        for (Module module : moduleManager.getModules()) {
            String key = "module." + module.getName().toLowerCase();
            config.put(key, module.isEnabled());
        }
    }
    
    public void set(String key, Object value) {
        config.put(key, value);
    }
    
    public Object get(String key) {
        return config.get(key);
    }
    
    public Object get(String key, Object defaultValue) {
        return config.getOrDefault(key, defaultValue);
    }
    
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
}