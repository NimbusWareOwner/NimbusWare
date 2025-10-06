package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class AutoReconnect extends Module {
    public AutoReconnect() {
        super("AutoReconnect", "Automatically reconnects", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoReconnect enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoReconnect disabled");
    }
}