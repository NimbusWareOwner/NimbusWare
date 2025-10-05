package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoBreak extends Module {
    public AutoBreak() {
        super("AutoBreak", "Automatically breaks blocks", Module.Category.WORLD, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}