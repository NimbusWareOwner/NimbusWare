package com.example.cheatclient;

import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.gui.GuiManager;
import com.example.cheatclient.config.ConfigManager;
import com.example.cheatclient.utils.Logger;
import org.lwjgl.glfw.GLFW;

public class CheatClient {
    public static final String NAME = "CheatClient";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "CheatClient Team";
    
    public static CheatClient INSTANCE;
    public MockMinecraftClient mc;
    
    private EventManager eventManager;
    private ModuleManager moduleManager;
    private GuiManager guiManager;
    private ConfigManager configManager;
    private boolean initialized = false;
    
    public void initialize() {
        if (initialized) return;
        
        Logger.info("Initializing " + NAME + " v" + VERSION);
        
        INSTANCE = this;
        mc = new MockMinecraftClient();
        
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
        // Register key bindings
        GLFW.glfwSetKeyCallback(mc.getWindow().getHandle(), (window, key, scancode, action, mods) -> {
            if (action == GLFW.GLFW_PRESS) {
                if (key == GLFW.GLFW_KEY_RIGHT_SHIFT) {
                    guiManager.toggleGui();
                }
            }
        });
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