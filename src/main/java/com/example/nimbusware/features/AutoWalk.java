package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;

public class AutoWalk extends Module {
    private boolean useBaritone = true;
    private int targetX = 0;
    private int targetY = 64;
    private int targetZ = 0;
    private boolean autoPathfinding = true;
    private boolean avoidWater = true;
    private boolean avoidLava = true;
    private boolean useFuntimeBypass = true;
    
    public AutoWalk() {
        super("AutoWalk", "Automatically walks with Baritone pathfinding", Module.Category.WORLD, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoWalk");
        }
        
        if (useBaritone && autoPathfinding) {
            startBaritonePathfinding();
        }
        System.out.println("AutoWalk enabled with Baritone pathfinding");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoWalk");
        }
        
        if (useBaritone) {
            stopBaritonePathfinding();
        }
        System.out.println("AutoWalk disabled");
    }
    
    private void startBaritonePathfinding() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("AutoWalk", 1.0f);
        }
        System.out.println("AutoWalk: Starting Baritone pathfinding to " + targetX + ", " + targetY + ", " + targetZ);
    }
    
    private void stopBaritonePathfinding() {
        System.out.println("AutoWalk: Stopping Baritone pathfinding");
    }
    
    // Getters and setters
    public boolean isUseBaritone() { return useBaritone; }
    public void setUseBaritone(boolean useBaritone) { this.useBaritone = useBaritone; }
    
    public int getTargetX() { return targetX; }
    public void setTargetX(int targetX) { this.targetX = targetX; }
    
    public int getTargetY() { return targetY; }
    public void setTargetY(int targetY) { this.targetY = targetY; }
    
    public int getTargetZ() { return targetZ; }
    public void setTargetZ(int targetZ) { this.targetZ = targetZ; }
    
    public boolean isAutoPathfinding() { return autoPathfinding; }
    public void setAutoPathfinding(boolean autoPathfinding) { this.autoPathfinding = autoPathfinding; }
    
    public boolean isAvoidWater() { return avoidWater; }
    public void setAvoidWater(boolean avoidWater) { this.avoidWater = avoidWater; }
    
    public boolean isAvoidLava() { return avoidLava; }
    public void setAvoidLava(boolean avoidLava) { this.avoidLava = avoidLava; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
}