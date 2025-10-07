package com.example.nimbusware.anti_detection.strategy;

import com.example.nimbusware.anti_detection.BypassStrategy;

import java.util.Random;

/**
 * Hypixel server bypass strategy implementation
 */
public class HypixelBypassStrategy implements BypassStrategy {
    private boolean active = false;
    
    @Override
    public float applyMovementModification(float value, Random random) {
        if (!active) return value;
        
        // Hypixel bypass: very subtle variations
        return value + (random.nextFloat() - 0.5f) * 0.05f;
    }
    
    @Override
    public float applyCombatModification(float value, Random random) {
        if (!active) return value;
        
        // Hypixel-specific combat bypass with timing variations
        return value + random.nextFloat() * 0.02f;
    }
    
    @Override
    public String getName() {
        return "Hypixel";
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}