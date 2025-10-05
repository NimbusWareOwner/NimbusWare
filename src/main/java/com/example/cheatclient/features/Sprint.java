package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprints when moving", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        // No special setup needed
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.mc.player != null) {
            CheatClient.mc.player.setSprinting(false);
        }
    }
    
    public void onTick() {
        if (CheatClient.mc.player != null && isEnabled()) {
            // Auto sprint when moving forward
            if (CheatClient.mc.options.forwardKey.isPressed() && 
                !CheatClient.mc.options.sneakKey.isPressed()) {
                CheatClient.mc.player.setSprinting(true);
            }
        }
    }
}