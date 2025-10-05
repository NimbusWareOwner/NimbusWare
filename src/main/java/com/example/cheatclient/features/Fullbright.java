package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class Fullbright extends Module {
    private FullbrightMode mode = FullbrightMode.POTION;
    private float customBrightness = 1.0f;
    private float originalGamma = 1.0f;
    
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
            // Store original gamma
            originalGamma = 1.0f; // Mock value
            // Apply custom brightness
            applyCustomBrightness(customBrightness);
        } else {
            // Apply potion effect
            applyPotionEffect();
        }
    }
    
    @Override
    protected void onDisable() {
        if (mode == FullbrightMode.CUSTOM) {
            // Restore original gamma
            applyCustomBrightness(originalGamma);
        } else {
            // Remove potion effect
            removePotionEffect();
        }
    }
    
    private void applyCustomBrightness(float brightness) {
        // Mock implementation - in real client would modify gamma
        System.out.println("Applied custom brightness: " + brightness);
    }
    
    private void applyPotionEffect() {
        // Mock implementation - in real client would apply night vision potion effect
        System.out.println("Applied night vision potion effect");
    }
    
    private void removePotionEffect() {
        // Mock implementation - in real client would remove night vision potion effect
        System.out.println("Removed night vision potion effect");
    }
    
    public FullbrightMode getMode() {
        return mode;
    }
    
    public void setMode(FullbrightMode mode) {
        this.mode = mode;
        if (isEnabled()) {
            onDisable();
            onEnable();
        }
    }
    
    public float getCustomBrightness() {
        return customBrightness;
    }
    
    public void setCustomBrightness(float customBrightness) {
        this.customBrightness = Math.max(0.1f, Math.min(10.0f, customBrightness));
        if (isEnabled() && mode == FullbrightMode.CUSTOM) {
            applyCustomBrightness(this.customBrightness);
        }
    }
}