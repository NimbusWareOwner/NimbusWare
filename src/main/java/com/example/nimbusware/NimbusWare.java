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
import com.example.nimbusware.security.SecureConfigManager;
import com.example.nimbusware.monitoring.MetricsCollector;
import com.example.nimbusware.monitoring.HealthChecker;
import com.example.nimbusware.analytics.StatisticsCollector;
import com.example.nimbusware.plugins.PluginManager;
import com.example.nimbusware.utils.Logger;
import com.example.nimbusware.utils.AsyncTaskManager;

/**
 * Main NimbusWare client class that manages all core systems and modules.
 * This class serves as the central hub for the cheat client, providing access
 * to all major components including module management, GUI systems, and configuration.
 * 
 * <p>The client follows a modular architecture where different systems are
 * managed independently but coordinated through this main class.</p>
 * 
 * <p>Example usage:</p>
 * <pre>{@code
 * NimbusWare client = new NimbusWare();
 * client.initialize();
 * }</pre>
 * 
 * @author NimbusWare Team
 * @version 1.0.0
 * @since 1.0.0
 */
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
    private SecureConfigManager secureConfigManager;
    private MetricsCollector metricsCollector;
    private HealthChecker healthChecker;
    private StatisticsCollector statisticsCollector;
    private PluginManager pluginManager;
    private AsyncTaskManager asyncTaskManager;
    private boolean initialized = false;
    private long startTime;
    
    /**
     * Initialize the NimbusWare client and all its subsystems.
     * This method sets up all core components including the event system,
     * module manager, GUI components, and configuration.
     * 
     * <p>This method is idempotent - calling it multiple times will have no effect
     * after the first successful initialization.</p>
     * 
     * @throws IllegalStateException if initialization fails
     * @see #isInitialized()
     */
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
        secureConfigManager = new SecureConfigManager();
        metricsCollector = MetricsCollector.getInstance();
        healthChecker = HealthChecker.getInstance();
        statisticsCollector = StatisticsCollector.getInstance();
        pluginManager = PluginManager.getInstance(this);
        asyncTaskManager = AsyncTaskManager.getInstance();
        startTime = System.currentTimeMillis();
        
        // Register modules
        moduleManager.registerModules();
        
        // Load configuration
        configManager.loadConfig();
        secureConfigManager.loadConfig();
        
        // Load plugins
        pluginManager.loadAllPlugins();
        
        // Register event listeners
        registerEventListeners();
        
        // Record startup metrics
        metricsCollector.incrementCounter("nimbusware.starts");
        statisticsCollector.recordPerformance("startup_time", System.currentTimeMillis() - startTime);
        
        initialized = true;
        Logger.info(NAME + " initialized successfully!");
    }
    
    private void registerEventListeners() {
        // Register key bindings and event handlers
        Logger.info("Event listeners registered");
    }
    
    /**
     * Get the event manager instance.
     * @return EventManager for handling events
     */
    public EventManager getEventManager() {
        return eventManager;
    }
    
    /**
     * Get the module manager instance.
     * @return ModuleManager for managing cheat modules
     */
    public ModuleManager getModuleManager() {
        return moduleManager;
    }
    
    /**
     * Get the GUI manager instance.
     * @return GuiManager for managing user interface
     */
    public GuiManager getGuiManager() {
        return guiManager;
    }
    
    /**
     * Get the main menu instance.
     * @return MainMenu for the primary user interface
     */
    public MainMenu getMainMenu() {
        return mainMenu;
    }
    
    /**
     * Get the account manager instance.
     * @return AccountManager for handling user accounts
     */
    public AccountManager getAccountManager() {
        return accountManager;
    }
    
    /**
     * Get the HUD manager instance.
     * @return HUDManager for managing heads-up display elements
     */
    public HUDManager getHUDManager() {
        return hudManager;
    }
    
    /**
     * Get the key manager instance.
     * @return KeyManager for handling keyboard input
     */
    public KeyManager getKeyManager() {
        return keyManager;
    }
    
    /**
     * Get the bind manager instance.
     * @return BindManager for managing key bindings
     */
    public BindManager getBindManager() {
        return bindManager;
    }
    
    /**
     * Get the drag and drop manager instance.
     * @return DragDropManager for handling drag and drop operations
     */
    public DragDropManager getDragDropManager() {
        return dragDropManager;
    }
    
    /**
     * Get the client start time.
     * @return Timestamp when the client was started
     */
    public long getStartTime() {
        return startTime;
    }
    
    /**
     * Get the configuration manager instance.
     * @return ConfigManager for handling configuration
     */
    public ConfigManager getConfigManager() {
        return configManager;
    }
    
    /**
     * Check if the client has been initialized.
     * @return true if initialized, false otherwise
     */
    public boolean isInitialized() {
        return initialized;
    }
    
    /**
     * Get the secure configuration manager instance.
     * @return SecureConfigManager for handling encrypted configuration
     */
    public SecureConfigManager getSecureConfigManager() {
        return secureConfigManager;
    }
    
    /**
     * Get the metrics collector instance.
     * @return MetricsCollector for performance monitoring
     */
    public MetricsCollector getMetricsCollector() {
        return metricsCollector;
    }
    
    /**
     * Get the health checker instance.
     * @return HealthChecker for system health monitoring
     */
    public HealthChecker getHealthChecker() {
        return healthChecker;
    }
    
    /**
     * Get the statistics collector instance.
     * @return StatisticsCollector for usage analytics
     */
    public StatisticsCollector getStatisticsCollector() {
        return statisticsCollector;
    }
    
    /**
     * Get the plugin manager instance.
     * @return PluginManager for plugin management
     */
    public PluginManager getPluginManager() {
        return pluginManager;
    }
    
    /**
     * Get the async task manager instance.
     * @return AsyncTaskManager for asynchronous operations
     */
    public AsyncTaskManager getAsyncTaskManager() {
        return asyncTaskManager;
    }
    
    /**
     * Shutdown all systems gracefully
     */
    public void shutdown() {
        Logger.info("Shutting down NimbusWare...");
        
        try {
            // Shutdown plugins first
            if (pluginManager != null) {
                pluginManager.shutdown();
            }
            
            // Shutdown monitoring systems
            if (metricsCollector != null) {
                metricsCollector.shutdown();
            }
            
            if (healthChecker != null) {
                healthChecker.shutdown();
            }
            
            if (statisticsCollector != null) {
                statisticsCollector.shutdown();
            }
            
            if (asyncTaskManager != null) {
                asyncTaskManager.shutdown();
            }
            
            // Save configurations
            if (configManager != null) {
                configManager.saveConfig();
            }
            
            if (secureConfigManager != null) {
                secureConfigManager.saveConfig();
            }
            
            // Shutdown logger last
            Logger.shutdown();
            
            Logger.info("NimbusWare shutdown complete");
        } catch (Exception e) {
            Logger.error("Error during shutdown", e);
        }
    }
}