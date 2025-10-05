package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoBuild extends Module {
    public AutoBuild() {
        super("AutoBuild", "Build farms from schematics", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoBuild enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoBuild disabled");
    }
}