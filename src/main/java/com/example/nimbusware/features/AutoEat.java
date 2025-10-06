package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class AutoEat extends Module {
    public AutoEat() {
        super("AutoEat", "Automatically eats food", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("AutoEat enabled");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("AutoEat disabled");
    }
}