package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class Fullbright extends Module {
    public Fullbright() {
        super("Fullbright", "Makes everything bright", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}