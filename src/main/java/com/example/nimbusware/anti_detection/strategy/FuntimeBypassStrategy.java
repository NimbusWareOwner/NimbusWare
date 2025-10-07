package com.example.nimbusware.anti_detection.strategy;

import com.example.nimbusware.anti_detection.BypassStrategy;

import java.util.Random;

/**
 * Funtime server bypass strategy implementation
 */
public class FuntimeBypassStrategy implements BypassStrategy {
    private boolean active = false;
    
    @Override
    public float applyMovementModification(float value, Random random) {
        if (!active) return value;
        
        // Funtime bypass: add small random variations
        return value + (random.nextFloat() - 0.5f) * 0.1f;
    }
    
    @Override
    public float applyCombatModification(float value, Random random) {
        if (!active) return value;
        
        // Add timing variations for Funtime
        return value + random.nextFloat() * 0.05f;
    }
    
    @Override
    public String getName() {
        return "Funtime";
    }
    
    @Override
    public boolean isActive() {
        return active;
    }
    
    public void setActive(boolean active) {
        this.active = active;
    }
}