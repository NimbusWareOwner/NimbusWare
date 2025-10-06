package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class Tracers extends Module {
    public Tracers() {
        super("Tracers", "Draw lines to entities", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("Tracers enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("Tracers disabled");
    }
}