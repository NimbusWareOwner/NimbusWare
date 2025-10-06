package com.example.nimbusware;

import com.example.nimbusware.core.EventManager;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.gui.GuiManager;
import com.example.nimbusware.gui.MainMenu;
import com.example.nimbusware.gui.AccountManager;
import com.example.nimbusware.gui.HUDManager;
import com.example.nimbusware.core.KeyManager;
import com.example.nimbusware.core.BindManager;
import com.example.nimbusware.core.DragDropManager;
import com.example.nimbusware.config.ConfigManager;
import com.example.nimbusware.utils.Logger;

public class NimbusWare {
    public static final String NAME = "NimbusWare";
    public static final String VERSION = "1.0.0";
    public static final String AUTHOR = "NimbusWare Team";
    
    public static NimbusWare INSTANCE;
    
    private EventManager eventManager;
    private ModuleManager moduleManager;
    private GuiManager guiManager;
    private MainMenu mainMenu;
    private AccountManager accountManager;
    private HUDManager hudManager;
    private KeyManager keyManager;
    private BindManager bindManager;
    private DragDropManager dragDropManager;
    private ConfigManager configManager;
    private boolean initialized = false;
    private long startTime;
    
    public void initialize() {
        if (initialized) return;
        
        Logger.info("Initializing " + NAME + " v" + VERSION);
        
        INSTANCE = this;
        
        // Initialize core systems
        eventManager = new EventManager();
        moduleManager = new ModuleManager();
        guiManager = new GuiManager();
        mainMenu = new MainMenu(this);
        accountManager = new AccountManager(this);
        hudManager = HUDManager.getInstance(this);
        keyManager = KeyManager.getInstance(this);
        bindManager = BindManager.getInstance(this);
        dragDropManager = DragDropManager.getInstance(this);
        configManager = new ConfigManager();
        startTime = System.currentTimeMillis();
        
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
    
    public MainMenu getMainMenu() {
        return mainMenu;
    }
    
    public AccountManager getAccountManager() {
        return accountManager;
    }
    
    public HUDManager getHUDManager() {
        return hudManager;
    }
    
    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    public BindManager getBindManager() {
        return bindManager;
    }
    
    public DragDropManager getDragDropManager() {
        return dragDropManager;
    }
    
    public long getStartTime() {
        return startTime;
    }
    
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    public boolean isInitialized() {
        return initialized;
    }
}