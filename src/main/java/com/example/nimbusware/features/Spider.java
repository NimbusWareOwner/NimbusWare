package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;

public class Spider extends Module {
    private boolean useFuntimeBypass = true;
    private float climbSpeed = 0.2f;
    private int tickCounter = 0;
    
    public Spider() {
        super("Spider", "Climb walls like a spider with Funtime bypass", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("Spider");
        }
        System.out.println("Spider enabled - player can climb walls");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("Spider");
        }
        System.out.println("Spider disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) {
            return;
        }
        
        // Check if player is against a wall and trying to move forward
        if (isAgainstWall() && isMovingForward()) {
            tickCounter++;
            
            // Apply climbing with randomization
            if (tickCounter % 2 == 0) { // Every 2 ticks
                float randomSpeed = climbSpeed + (float)(Math.random() * 0.05 - 0.025); // ±0.025 variation
                applyClimbing(randomSpeed);
            }
        } else {
            tickCounter = 0;
        }
    }
    
    private boolean isAgainstWall() {
        // Mock implementation - in real client would check for blocks in front
        return Math.random() < 0.4; // 40% chance to simulate wall detection
    }
    
    private boolean isMovingForward() {
        // Mock implementation - check if player is pressing forward key
        return Math.random() < 0.7; // 70% chance to simulate forward movement
    }
    
    private void applyClimbing(float speed) {
        // Apply vertical movement with anti-detection
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("Spider", speed);
        }
    }
    
    public float getClimbSpeed() {
        return climbSpeed;
    }
    
    public void setClimbSpeed(float climbSpeed) {
        this.climbSpeed = climbSpeed;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
}