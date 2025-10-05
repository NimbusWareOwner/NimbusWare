package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AntiAFK extends Module {
    private int actionInterval = 30000; // 30 seconds
    private boolean randomizeInterval = true;
    private int minInterval = 20000; // 20 seconds
    private int maxInterval = 40000; // 40 seconds
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
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AntiAFK");
        }
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getPlayer() == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        long nextActionInterval = actionInterval;
        
        // Randomize interval if enabled
        if (randomizeInterval) {
            nextActionInterval = minInterval + (long)(Math.random() * (maxInterval - minInterval));
        }
        
        if (currentTime - lastAction >= nextActionInterval) {
            performAntiAFKAction();
            lastAction = currentTime;
        }
    }
    
    private void performAntiAFKAction() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyMovementModification("AntiAFK", 1.0f);
        }
        
        // Cycle through different actions to avoid detection
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
        // Small random movement
        double randomX = (Math.random() - 0.5) * 0.1;
        double randomZ = (Math.random() - 0.5) * 0.1;
        
        // Mock movement
        System.out.println("AntiAFK: Movement - X:" + randomX + ", Z:" + randomZ);
    }
    
    private void performLookAround() {
        // Random look around
        float randomYaw = (float)(Math.random() * 360);
        float randomPitch = (float)(Math.random() * 180 - 90);
        
        // Mock look around
        System.out.println("AntiAFK: Look around - Yaw:" + randomYaw + ", Pitch:" + randomPitch);
    }
    
    private void performJump() {
        // Mock jump
        System.out.println("AntiAFK: Jump");
    }
    
    private void performSneak() {
        // Mock sneak toggle
        System.out.println("AntiAFK: Sneak toggle");
    }
    
    public int getActionInterval() {
        return actionInterval;
    }
    
    public void setActionInterval(int actionInterval) {
        this.actionInterval = Math.max(1000, actionInterval);
    }
    
    public boolean isRandomizeInterval() {
        return randomizeInterval;
    }
    
    public void setRandomizeInterval(boolean randomizeInterval) {
        this.randomizeInterval = randomizeInterval;
    }
    
    public int getMinInterval() {
        return minInterval;
    }
    
    public void setMinInterval(int minInterval) {
        this.minInterval = Math.max(1000, minInterval);
    }
    
    public int getMaxInterval() {
        return maxInterval;
    }
    
    public void setMaxInterval(int maxInterval) {
        this.maxInterval = Math.max(minInterval, maxInterval);
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
}