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
    
    public Module(String name, String description, Category category, int keyBind) {
        this.name = name;
        this.description = description;
        this.category = category;
        this.keyBind = keyBind;
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
        onEnable();
        NimbusWare.INSTANCE.getEventManager().register(this);
        
        Logger.info("§a[CheatClient] §f" + name + " §aenabled");
    }
    
    public void disable() {
        if (!enabled) return;
        
        enabled = false;
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