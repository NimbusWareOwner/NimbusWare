package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoEat extends Module {
    public AutoEat() {
        super("AutoEat", "Automatically eats food", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}