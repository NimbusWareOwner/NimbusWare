package com.example.nimbusware.core;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

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