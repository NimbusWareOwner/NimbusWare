package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class AutoBuild extends Module {
    public AutoBuild() {
        super("AutoBuild", "Automatically builds", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}