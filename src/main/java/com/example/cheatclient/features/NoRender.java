package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", "Disables certain rendering", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {}
    
    @Override
    protected void onDisable() {}
}