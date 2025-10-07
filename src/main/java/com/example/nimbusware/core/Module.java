package com.example.nimbusware.core;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

/**
 * Abstract base class for all cheat modules in NimbusWare.
 * This class provides the common functionality that all modules share,
 * including enable/disable logic, event registration, and basic properties.
 * 
 * <p>All cheat modules must extend this class and implement the abstract methods
 * {@link #onEnable()} and {@link #onDisable()}.</p>
 * 
 * <p>Example implementation:</p>
 * <pre>{@code
 * public class MyModule extends Module {
 *     public MyModule() {
 *         super("MyModule", "Description", Category.MISC, KeyEvent.VK_M);
 *     }
 *     
 *     @Override
 *     protected void onEnable() {
 *         // Module-specific enable logic
 *     }
 *     
 *     @Override
 *     protected void onDisable() {
 *         // Module-specific disable logic
 *     }
 * }</pre>
 * 
 * @author NimbusWare Team
 * @version 1.0.0
 * @since 1.0.0
 */
public abstract class Module {
    protected final String name;
    protected final String description;
    protected final Category category;
    protected final int keyBind;
    
    protected boolean enabled = false;
    protected boolean visible = true;
    protected ModuleSettings settings;
    protected long lastToggleTime = 0;
    protected long totalUptime = 0;
    protected long startTime = 0;
    
    public Module(String name, String description, Category category, int keyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBind = keyBind;
        this.settings = new ModuleSettings(name);
        initializeDefaultSettings();
    }
    
    /**
     * Initialize default settings for the module
     * Override this method to set default values
     */
    protected void initializeDefaultSettings() {
        // Override in subclasses
    }
    
    public void toggle() {
        if (enabled) {
            disable();
        } else {
            enable();
        }
    }
    
    public void enable() {
        if (enabled) return;
        
        enabled = true;
        startTime = System.currentTimeMillis();
        lastToggleTime = startTime;
        
        onEnable();
        NimbusWare.INSTANCE.getEventManager().register(this);
        
        // Record metrics
        if (NimbusWare.INSTANCE != null) {
            NimbusWare.INSTANCE.getStatisticsCollector().incrementCounter("modules." + name + ".activations");
        }
        
        Logger.info("§a[CheatClient] §f" + name + " §aenabled");
    }
    
    public void disable() {
        if (!enabled) return;
        
        enabled = false;
        
        // Calculate uptime
        if (startTime > 0) {
            totalUptime += System.currentTimeMillis() - startTime;
        }
        
        onDisable();
        NimbusWare.INSTANCE.getEventManager().unregister(this);
        
        Logger.info("§c[CheatClient] §f" + name + " §cdisabled");
    }
    
    protected abstract void onEnable();
    protected abstract void onDisable();
    
    public String getName() {
        return name;
    }
    
    public String getDescription() {
        return description;
    }
    
    public Category getCategory() {
        return category;
    }
    
    public int getKeyBind() {
        return keyBind;
    }
    
    public boolean isEnabled() {
        return enabled;
    }
    
    public boolean isVisible() {
        return visible;
    }
    
    public void setVisible(boolean visible) {
        this.visible = visible;
    }
    
    /**
     * Get module settings
     * @return ModuleSettings instance
     */
    public ModuleSettings getSettings() {
        return settings;
    }
    
    /**
     * Get total uptime in milliseconds
     * @return Total uptime
     */
    public long getTotalUptime() {
        if (enabled && startTime > 0) {
            return totalUptime + (System.currentTimeMillis() - startTime);
        }
        return totalUptime;
    }
    
    /**
     * Get current uptime in milliseconds
     * @return Current uptime
     */
    public long getCurrentUptime() {
        if (enabled && startTime > 0) {
            return System.currentTimeMillis() - startTime;
        }
        return 0;
    }
    
    /**
     * Get uptime percentage (0.0 to 1.0)
     * @return Uptime percentage
     */
    public double getUptimePercentage() {
        long totalTime = System.currentTimeMillis() - lastToggleTime;
        if (totalTime <= 0) return 0.0;
        return (double) getTotalUptime() / totalTime;
    }
    
    /**
     * Reset module statistics
     */
    public void resetStatistics() {
        totalUptime = 0;
        startTime = 0;
        lastToggleTime = 0;
    }
    
    /**
     * Get module statistics
     * @return Map of statistics
     */
    public java.util.Map<String, Object> getStatistics() {
        java.util.Map<String, Object> stats = new java.util.HashMap<>();
        stats.put("enabled", enabled);
        stats.put("totalUptime", getTotalUptime());
        stats.put("currentUptime", getCurrentUptime());
        stats.put("uptimePercentage", getUptimePercentage());
        stats.put("lastToggleTime", lastToggleTime);
        return stats;
    }
    
    public enum Category {
        MOVEMENT("Movement"),
        RENDER("Render"),
        COMBAT("Combat"),
        PLAYER("Player"),
        WORLD("World"),
        MISC("Misc");
        
        private final String displayName;
        
        Category(String displayName) {
            this.displayName = displayName;
        }
        
        public String getDisplayName() {
            return displayName;
        }
    }
}