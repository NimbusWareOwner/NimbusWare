package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoMine extends Module {
    public AutoMine() {
        super("AutoMine", "Mine ores at spawn coordinates", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoMine enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoMine disabled");
    }
}