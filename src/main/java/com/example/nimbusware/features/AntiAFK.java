package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;

public class AntiAFK extends Module {
    private int actionInterval = 30000;
    private boolean randomizeInterval = true;
    private int minInterval = 20000;
    private int maxInterval = 40000;
    private boolean useFuntimeBypass = true;
    private long lastAction = 0;
    private int actionCounter = 0;
    
    public AntiAFK() {
        super("AntiAFK", "Prevents AFK detection with random actions", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AntiAFK");
        }
        System.out.println("AntiAFK enabled with " + actionInterval + "ms interval");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AntiAFK");
        }
        System.out.println("AntiAFK disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        long currentTime = System.currentTimeMillis();
        long nextActionInterval = actionInterval;
        
        if (randomizeInterval) {
            nextActionInterval = minInterval + (long)(Math.random() * (maxInterval - minInterval));
        }
        
        if (currentTime - lastAction >= nextActionInterval) {
            performAntiAFKAction();
            lastAction = currentTime;
        }
    }
    
    private void performAntiAFKAction() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("AntiAFK", 1.0f);
        }
        
        switch (actionCounter % 4) {
            case 0:
                performMovement();
                break;
            case 1:
                performLookAround();
                break;
            case 2:
                performJump();
                break;
            case 3:
                performSneak();
                break;
        }
        
        actionCounter++;
        System.out.println("AntiAFK: Performed action " + actionCounter);
    }
    
    private void performMovement() {
        System.out.println("AntiAFK: Movement");
    }
    
    private void performLookAround() {
        System.out.println("AntiAFK: Look around");
    }
    
    private void performJump() {
        System.out.println("AntiAFK: Jump");
    }
    
    private void performSneak() {
        System.out.println("AntiAFK: Sneak toggle");
    }
    
    // Getters and setters
    public int getActionInterval() { return actionInterval; }
    public void setActionInterval(int actionInterval) { this.actionInterval = Math.max(1000, actionInterval); }
    
    public boolean isRandomizeInterval() { return randomizeInterval; }
    public void setRandomizeInterval(boolean randomizeInterval) { this.randomizeInterval = randomizeInterval; }
    
    public int getMinInterval() { return minInterval; }
    public void setMinInterval(int minInterval) { this.minInterval = Math.max(1000, minInterval); }
    
    public int getMaxInterval() { return maxInterval; }
    public void setMaxInterval(int maxInterval) { this.maxInterval = Math.max(minInterval, maxInterval); }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
}