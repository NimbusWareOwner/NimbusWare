package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoMine extends Module {
    public AutoMine() {
        super("AutoMine", "Automatically mines blocks", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}