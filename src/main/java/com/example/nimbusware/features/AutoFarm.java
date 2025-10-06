package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class AutoFarm extends Module {
    public AutoFarm() {
        super("AutoFarm", "Auto-farm with instant replanting", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoFarm enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoFarm disabled");
    }
}