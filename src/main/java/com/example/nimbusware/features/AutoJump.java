package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;

public class AutoJump extends Module {
    private int jumpInterval = 1000;
    private boolean randomizeInterval = true;
    private int minInterval = 800;
    private int maxInterval = 1200;
    private boolean useFuntimeBypass = true;
    private long lastJump = 0;
    
    public AutoJump() {
        super("AutoJump", "Automatically jumps with configurable interval", Module.Category.WORLD, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoJump");
        }
        System.out.println("AutoJump enabled with " + jumpInterval + "ms interval");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoJump");
        }
        System.out.println("AutoJump disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        long currentTime = System.currentTimeMillis();
        long nextJumpInterval = jumpInterval;
        
        if (randomizeInterval) {
            nextJumpInterval = minInterval + (long)(Math.random() * (maxInterval - minInterval));
        }
        
        if (currentTime - lastJump >= nextJumpInterval) {
            performJump();
            lastJump = currentTime;
        }
    }
    
    private void performJump() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("AutoJump", 1.0f);
        }
        System.out.println("AutoJump: Performing jump");
    }
    
    // Getters and setters
    public int getJumpInterval() { return jumpInterval; }
    public void setJumpInterval(int jumpInterval) { this.jumpInterval = Math.max(100, jumpInterval); }
    
    public boolean isRandomizeInterval() { return randomizeInterval; }
    public void setRandomizeInterval(boolean randomizeInterval) { this.randomizeInterval = randomizeInterval; }
    
    public int getMinInterval() { return minInterval; }
    public void setMinInterval(int minInterval) { this.minInterval = Math.max(100, minInterval); }
    
    public int getMaxInterval() { return maxInterval; }
    public void setMaxInterval(int maxInterval) { this.maxInterval = Math.max(minInterval, maxInterval); }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
}