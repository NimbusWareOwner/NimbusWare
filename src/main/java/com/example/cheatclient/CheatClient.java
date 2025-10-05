package com.example.cheatclient;

import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.gui.GuiManager;
import com.example.cheatclient.config.ConfigManager;
import com.example.cheatclient.utils.Logger;

public class CheatClient {
    public static final String NAME = "CheatClient";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "CheatClient Team";
    
    public static CheatClient INSTANCE;
    
    private EventManager eventManager;
    private ModuleManager moduleManager;
    private GuiManager guiManager;
    private ConfigManager configManager;
    private boolean initialized = false;
    
    public void initialize() {
        if (initialized) return;
        
        Logger.info("Initializing " + NAME + " v" + VERSION);
        
        INSTANCE = this;
        
        // Initialize core systems
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        configManager = new ConfigManager();
        
        // Register modules
        moduleManager.registerModules();
        
        // Load configuration
        configManager.loadConfig();
        
        // Register event listeners
        registerEventListeners();
        
        initialized = true;
        Logger.info(NAME + " initialized successfully!");
    }
    
    private void registerEventListeners() {
        // Register key bindings and event handlers
        Logger.info("Event listeners registered");
    }
    
    public EventManager getEventManager() {
        return eventManager;
    }
    
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    public GuiManager getGuiManager() {
        return guiManager;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}