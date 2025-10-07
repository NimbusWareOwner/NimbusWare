package com.example.nimbusware.profiles;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Cheat profile system for saving and loading different configurations
 */
public class CheatProfile {
    private final String name;
    private final String description;
    private final Map<String, Object> moduleSettings = new ConcurrentHashMap<>();
    private final Map<String, Boolean> moduleStates = new ConcurrentHashMap<>();
    private final Map<String, Object> globalSettings = new ConcurrentHashMap<>();
    private final long creationTime;
    private long lastUsed;
    
    public CheatProfile(String name, String description) {
        this.name = name;
        this.description = description;
        this.creationTime = System.currentTimeMillis();
        this.lastUsed = creationTime;
    }
    
    /**
     * Save current module states and settings
     * @param moduleManager Module manager instance
     */
    public void saveFromCurrent(ModuleManager moduleManager) {
        moduleSettings.clear();
        moduleStates.clear();
        
        for (Module module : moduleManager.getModules()) {
            // Save module state
            moduleStates.put(module.getName(), module.isEnabled());
            
            // Save module settings
            Map<String, Object> settings = module.getSettings().getAll();
            moduleSettings.put(module.getName(), new HashMap<>(settings));
        }
        
        Logger.info("Profile '" + name + "' saved with " + moduleStates.size() + " modules");
    }
    
    /**
     * Load profile to current module states and settings
     * @param moduleManager Module manager instance
     */
    public void loadToCurrent(ModuleManager moduleManager) {
        int loadedModules = 0;
        
        for (Module module : moduleManager.getModules()) {
            String moduleName = module.getName();
            
            // Load module state
            Boolean enabled = moduleStates.get(moduleName);
            if (enabled != null) {
                if (enabled && !module.isEnabled()) {
                    module.enable();
                    loadedModules++;
                } else if (!enabled && module.isEnabled()) {
                    module.disable();
                }
            }
            
            // Load module settings
            @SuppressWarnings("unchecked")
            Map<String, Object> settings = (Map<String, Object>) moduleSettings.get(moduleName);
            if (settings != null) {
                module.getSettings().loadFromMap(settings);
            }
        }
        
        lastUsed = System.currentTimeMillis();
        Logger.info("Profile '" + name + "' loaded with " + loadedModules + " modules enabled");
    }
    
    /**
     * Add module settings to profile
     * @param moduleName Module name
     * @param settings Module settings
     */
    public void addModuleSettings(String moduleName, Map<String, Object> settings) {
        moduleSettings.put(moduleName, new HashMap<>(settings));
    }
    
    /**
     * Get module settings from profile
     * @param moduleName Module name
     * @return Module settings or null
     */
    @SuppressWarnings("unchecked")
    public Map<String, Object> getModuleSettings(String moduleName) {
        Map<String, Object> settings = (Map<String, Object>) moduleSettings.get(moduleName);
        return settings != null ? new HashMap<>(settings) : null;
    }
    
    /**
     * Set module state in profile
     * @param moduleName Module name
     * @param enabled Enabled state
     */
    public void setModuleState(String moduleName, boolean enabled) {
        moduleStates.put(moduleName, enabled);
    }
    
    /**
     * Get module state from profile
     * @param moduleName Module name
     * @return Enabled state or null
     */
    public Boolean getModuleState(String moduleName) {
        return moduleStates.get(moduleName);
    }
    
    /**
     * Set global setting
     * @param key Setting key
     * @param value Setting value
     */
    public void setGlobalSetting(String key, Object value) {
        globalSettings.put(key, value);
    }
    
    /**
     * Get global setting
     * @param key Setting key
     * @param defaultValue Default value
     * @return Setting value
     */
    @SuppressWarnings("unchecked")
    public <T> T getGlobalSetting(String key, T defaultValue) {
        Object value = globalSettings.get(key);
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
     * Get profile statistics
     * @return Profile statistics
     */
    public Map<String, Object> getStatistics() {
        Map<String, Object> stats = new HashMap<>();
        stats.put("name", name);
        stats.put("description", description);
        stats.put("creationTime", creationTime);
        stats.put("lastUsed", lastUsed);
        stats.put("moduleCount", moduleStates.size());
        stats.put("enabledModules", moduleStates.values().stream().mapToInt(b -> b ? 1 : 0).sum());
        stats.put("globalSettingsCount", globalSettings.size());
        return stats;
    }
    
    /**
     * Export profile to map
     * @return Profile data as map
     */
    public Map<String, Object> export() {
        Map<String, Object> data = new HashMap<>();
        data.put("name", name);
        data.put("description", description);
        data.put("creationTime", creationTime);
        data.put("lastUsed", lastUsed);
        data.put("moduleStates", new HashMap<>(moduleStates));
        data.put("moduleSettings", new HashMap<>(moduleSettings));
        data.put("globalSettings", new HashMap<>(globalSettings));
        return data;
    }
    
    /**
     * Import profile from map
     * @param data Profile data
     */
    @SuppressWarnings("unchecked")
    public void importFrom(Map<String, Object> data) {
        moduleStates.clear();
        moduleSettings.clear();
        globalSettings.clear();
        
        if (data.containsKey("moduleStates")) {
            Map<String, Boolean> states = (Map<String, Boolean>) data.get("moduleStates");
            moduleStates.putAll(states);
        }
        
        if (data.containsKey("moduleSettings")) {
            Map<String, Object> settings = (Map<String, Object>) data.get("moduleSettings");
            moduleSettings.putAll(settings);
        }
        
        if (data.containsKey("globalSettings")) {
            Map<String, Object> global = (Map<String, Object>) data.get("globalSettings");
            globalSettings.putAll(global);
        }
        
        Logger.info("Profile '" + name + "' imported successfully");
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public long getCreationTime() { return creationTime; }
    public long getLastUsed() { return lastUsed; }
    public Map<String, Boolean> getModuleStates() { return new HashMap<>(moduleStates); }
    public Map<String, Object> getModuleSettings() { return new HashMap<>(moduleSettings); }
    public Map<String, Object> getGlobalSettings() { return new HashMap<>(globalSettings); }
}