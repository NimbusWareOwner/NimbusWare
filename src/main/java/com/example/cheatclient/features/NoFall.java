package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class NoFall extends Module {
    public NoFall() {
        super("NoFall", "Prevents fall damage", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        // No special setup needed
    }
    
    @Override
    protected void onDisable() {
        // No cleanup needed
    }
    
    public void onTick() {
        if (CheatClient.mc.player != null && isEnabled()) {
            // Reset fall distance to prevent damage
            CheatClient.mc.player.fallDistance = 0.0f;
        }
    }
}