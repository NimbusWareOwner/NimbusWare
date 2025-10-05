package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoClicker extends Module {
    private int cps = 10; // clicks per second
    private boolean leftClick = true;
    private boolean rightClick = false;
    private boolean useFuntimeBypass = true;
    private long lastClick = 0;
    private boolean tapeMouse = true; // Tape mouse to prevent detection
    
    public AutoClicker() {
        super("AutoClicker", "Automatically clicks with tape mouse", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoClicker");
        }
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoClicker");
        }
    }
    
    public void onTick() {
        if (!isEnabled()) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        long clickInterval = 1000 / cps; // Convert CPS to milliseconds
        
        if (currentTime - lastClick >= clickInterval) {
            if (leftClick) {
                performLeftClick();
            }
            if (rightClick) {
                performRightClick();
            }
            lastClick = currentTime;
        }
    }
    
    private void performLeftClick() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
        }
        
        // Perform left click with tape mouse
        if (tapeMouse) {
            // Tape mouse implementation - add small random delays
            long randomDelay = (long) (Math.random() * 10); // 0-10ms random delay
            try {
                Thread.sleep(randomDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Mock click implementation
        System.out.println("AutoClicker: Left click performed");
    }
    
    private void performRightClick() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
        }
        
        // Perform right click with tape mouse
        if (tapeMouse) {
            // Tape mouse implementation - add small random delays
            long randomDelay = (long) (Math.random() * 10); // 0-10ms random delay
            try {
                Thread.sleep(randomDelay);
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Mock click implementation
        System.out.println("AutoClicker: Right click performed");
    }
    
    public int getCps() {
        return cps;
    }
    
    public void setCps(int cps) {
        this.cps = Math.max(1, Math.min(20, cps)); // Limit between 1-20 CPS
    }
    
    public boolean isLeftClick() {
        return leftClick;
    }
    
    public void setLeftClick(boolean leftClick) {
        this.leftClick = leftClick;
    }
    
    public boolean isRightClick() {
        return rightClick;
    }
    
    public void setRightClick(boolean rightClick) {
        this.rightClick = rightClick;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
    
    public boolean isTapeMouse() {
        return tapeMouse;
    }
    
    public void setTapeMouse(boolean tapeMouse) {
        this.tapeMouse = tapeMouse;
    }
}