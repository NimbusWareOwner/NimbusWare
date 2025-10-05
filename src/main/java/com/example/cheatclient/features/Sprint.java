package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class Sprint extends Module {
    public Sprint() {
        super("Sprint", "Automatically sprints when moving", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        // Mock implementation
        System.out.println("Sprint enabled - player will auto-sprint");
    }
    
    @Override
    protected void onDisable() {
        // Mock implementation
        System.out.println("Sprint disabled");
    }
}