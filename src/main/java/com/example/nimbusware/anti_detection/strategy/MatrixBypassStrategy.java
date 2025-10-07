package com.example.nimbusware.anti_detection.strategy;

import com.example.nimbusware.anti_detection.BypassStrategy;

import java.util.Random;

/**
 * Matrix server bypass strategy implementation
 */
public class MatrixBypassStrategy implements BypassStrategy {
    private boolean active = false;
    
    @Override
    public float applyMovementModification(float value, Random random) {
        if (!active) return value;
        
        // Matrix bypass: more aggressive randomization
        return value + (random.nextFloat() - 0.5f) * 0.2f;
    }
    
    @Override
    public float applyCombatModification(float value, Random random) {
        if (!active) return value;
        
        // Matrix-specific combat bypass
        return value + (random.nextFloat() - 0.5f) * 0.1f;
    }
    
    @Override
    public String getName() {
        return "Matrix";
    }
    
    @Override
    public boolean isActive() {
        return active;
    }

    @Override
    public void setActive(boolean active) {
        this.active = active;
    }
}