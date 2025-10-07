package com.example.nimbusware.anti_detection;

import java.util.Random;

/**
 * Strategy interface for different anti-detection bypass implementations
 */
public interface BypassStrategy {
    /**
     * Apply movement modification based on the specific bypass strategy
     * @param value Original movement value
     * @param random Random instance for consistent randomization
     * @return Modified movement value
     */
    float applyMovementModification(float value, Random random);
    
    /**
     * Apply combat modification based on the specific bypass strategy
     * @param value Original combat value
     * @param random Random instance for consistent randomization
     * @return Modified combat value
     */
    float applyCombatModification(float value, Random random);
    
    /**
     * Get the name of this bypass strategy
     * @return Strategy name
     */
    String getName();
    
    /**
     * Check if this strategy is currently active
     * @return true if active, false otherwise
     */
    boolean isActive();
    
    /**
     * Set the active state of this strategy
     * @param active true to activate, false to deactivate
     */
    void setActive(boolean active);
}