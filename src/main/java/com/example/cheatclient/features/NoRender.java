package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class NoRender extends Module {
    public NoRender() {
        super("NoRender", "Disables certain rendering", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("NoRender enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("NoRender disabled");
    }
}