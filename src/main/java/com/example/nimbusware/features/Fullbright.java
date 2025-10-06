package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;

public class Fullbright extends Module {
    private FullbrightMode mode = FullbrightMode.POTION;
    private float customBrightness = 1.0f;
    
    public enum FullbrightMode {
        POTION,
        CUSTOM
    }
    
    public Fullbright() {
        super("Fullbright", "Makes everything bright with 2 modes", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        if (mode == FullbrightMode.CUSTOM) {
            System.out.println("Fullbright enabled with custom brightness: " + customBrightness);
        } else {
            System.out.println("Fullbright enabled with potion effect");
        }
    }
    
    @Override
    protected void onDisable() {
        System.out.println("Fullbright disabled");
    }
    
    public FullbrightMode getMode() { return mode; }
    public void setMode(FullbrightMode mode) { this.mode = mode; }
    
    public float getCustomBrightness() { return customBrightness; }
    public void setCustomBrightness(float customBrightness) { 
        this.customBrightness = Math.max(0.1f, Math.min(10.0f, customBrightness)); 
    }
}