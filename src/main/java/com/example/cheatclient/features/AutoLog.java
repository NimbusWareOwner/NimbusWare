package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoLog extends Module {
    public AutoLog() {
        super("AutoLog", "Automatically logs out", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}