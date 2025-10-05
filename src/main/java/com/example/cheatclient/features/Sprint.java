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
        if (CheatClient.INSTANCE.mc.getPlayer() != null) {
            CheatClient.INSTANCE.mc.getPlayer().setSprinting(false);
        }
    }
    
    public void onTick() {
        if (CheatClient.INSTANCE.mc.getPlayer() != null && isEnabled()) {
            // Auto sprint when moving forward
            if (CheatClient.INSTANCE.mc.getOptions().forwardKey.isPressed() && 
                !CheatClient.INSTANCE.mc.getOptions().sneakKey.isPressed()) {
                CheatClient.INSTANCE.mc.getPlayer().setSprinting(true);
            }
        }
    }
}