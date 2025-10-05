package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoTool extends Module {
    public AutoTool() {
        super("AutoTool", "Automatically selects best tool", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}