package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoClicker extends Module {
    private int cps = 10;
    private boolean leftClick = true;
    private boolean rightClick = false;
    private boolean useFuntimeBypass = true;
    private long lastClick = 0;
    private boolean tapeMouse = true;
    
    public AutoClicker() {
        super("AutoClicker", "Automatically clicks with tape mouse", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoClicker");
        }
        System.out.println("AutoClicker enabled with " + cps + " CPS");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoClicker");
        }
        System.out.println("AutoClicker disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        long currentTime = System.currentTimeMillis();
        long clickInterval = 1000 / cps;
        
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
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
        }
        System.out.println("AutoClicker: Left click");
    }
    
    private void performRightClick() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
        }
        System.out.println("AutoClicker: Right click");
    }
    
    // Getters and setters
    public int getCps() { return cps; }
    public void setCps(int cps) { this.cps = Math.max(1, Math.min(20, cps)); }
    
    public boolean isLeftClick() { return leftClick; }
    public void setLeftClick(boolean leftClick) { this.leftClick = leftClick; }
    
    public boolean isRightClick() { return rightClick; }
    public void setRightClick(boolean rightClick) { this.rightClick = rightClick; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isTapeMouse() { return tapeMouse; }
    public void setTapeMouse(boolean tapeMouse) { this.tapeMouse = tapeMouse; }
}