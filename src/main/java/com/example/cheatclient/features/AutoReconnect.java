package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoReconnect extends Module {
    public AutoReconnect() {
        super("AutoReconnect", "Automatically reconnects", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}