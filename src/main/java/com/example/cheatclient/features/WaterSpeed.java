package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class WaterSpeed extends Module {
    private float speedMultiplier = 2.0f;
    private boolean useFuntimeBypass = true;
    private int tickCounter = 0;
    
    public WaterSpeed() {
        super("WaterSpeed", "Increases swimming speed in water with Funtime bypass", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("WaterSpeed");
        }
        System.out.println("WaterSpeed enabled with " + speedMultiplier + "x speed");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("WaterSpeed");
        }
        System.out.println("WaterSpeed disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) {
            return;
        }
        
        // Check if player is in water
        if (isInWater()) {
            tickCounter++;
            
            // Apply speed boost with randomization to avoid detection
            if (tickCounter % 3 == 0) { // Every 3 ticks to avoid detection
                float randomMultiplier = speedMultiplier + (float)(Math.random() * 0.2 - 0.1); // ±0.1 variation
                applyWaterSpeedBoost(randomMultiplier);
            }
        } else {
            tickCounter = 0;
        }
    }
    
    private boolean isInWater() {
        // Mock implementation - in real client would check block below player
        return Math.random() < 0.3; // 30% chance to simulate water
    }
    
    private void applyWaterSpeedBoost(float multiplier) {
        // Apply swimming speed boost with anti-detection
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("WaterSpeed", multiplier);
        }
    }
    
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
    
    public void setSpeedMultiplier(float speedMultiplier) {
        this.speedMultiplier = speedMultiplier;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
}