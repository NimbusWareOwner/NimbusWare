package com.example.cheatclient.core;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.config.ConfigManager;

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
        CheatClient.INSTANCE.getEventManager().register(this);
        
        if (CheatClient.INSTANCE.mc.getPlayer() != null) {
            CheatClient.INSTANCE.mc.getPlayer().sendMessage(
                com.example.cheatclient.mock.MockText.literal("§a[CheatClient] §f" + name + " §aenabled"),
                false
            );
        }
    }
    
    public void disable() {
        if (!enabled) return;
        
        enabled = false;
        onDisable();
        CheatClient.INSTANCE.getEventManager().unregister(this);
        
        if (CheatClient.INSTANCE.mc.getPlayer() != null) {
            CheatClient.INSTANCE.mc.getPlayer().sendMessage(
                com.example.cheatclient.mock.MockText.literal("§c[CheatClient] §f" + name + " §cdisabled"),
                false
            );
        }
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