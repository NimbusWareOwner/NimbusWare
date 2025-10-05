package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoRespawn extends Module {
    public AutoRespawn() {
        super("AutoRespawn", "Automatically respawns", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoRespawn enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoRespawn disabled");
    }
}