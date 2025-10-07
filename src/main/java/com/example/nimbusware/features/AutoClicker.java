package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;
import com.example.nimbusware.utils.ValidationUtils;

/**
 * Enhanced AutoClicker module with validation and error handling
 */
public class AutoClicker extends Module {
    private static final int MIN_CPS = 1;
    private static final int MAX_CPS = 20;
    private static final int DEFAULT_CPS = 10;
    
    private int cps = DEFAULT_CPS;
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
        try {
            if (useFuntimeBypass) {
                AntiDetectionManager.enableFuntimeBypass("AutoClicker");
            }
            Logger.info("AutoClicker enabled with " + cps + " CPS");
        } catch (Exception e) {
            Logger.error("Failed to enable AutoClicker", e);
            throw e;
        }
    }
    
    @Override
    protected void onDisable() {
        try {
            if (useFuntimeBypass) {
                AntiDetectionManager.disableFuntimeBypass("AutoClicker");
            }
            Logger.info("AutoClicker disabled");
        } catch (Exception e) {
            Logger.error("Failed to disable AutoClicker", e);
        }
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        try {
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
        } catch (Exception e) {
            Logger.error("Error in AutoClicker tick", e);
        }
    }
    
    private void performLeftClick() {
        try {
            if (useFuntimeBypass) {
                AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
            }
            Logger.debug("AutoClicker: Left click");
        } catch (Exception e) {
            Logger.error("Error performing left click", e);
        }
    }
    
    private void performRightClick() {
        try {
            if (useFuntimeBypass) {
                AntiDetectionManager.applyCombatModification("AutoClicker", 1.0f);
            }
            Logger.debug("AutoClicker: Right click");
        } catch (Exception e) {
            Logger.error("Error performing right click", e);
        }
    }
    
    // Getters and setters with validation
    public int getCps() { 
        return cps; 
    }
    
    public void setCps(int cps) {
        try {
            ValidationUtils.validateRange(cps, MIN_CPS, MAX_CPS, "CPS");
            this.cps = cps;
            Logger.debug("AutoClicker CPS set to " + cps);
        } catch (IllegalArgumentException e) {
            Logger.warn("Invalid CPS value: " + e.getMessage());
            this.cps = Math.max(MIN_CPS, Math.min(MAX_CPS, cps));
        }
    }
    
    public boolean isLeftClick() { 
        return leftClick; 
    }
    
    public void setLeftClick(boolean leftClick) { 
        this.leftClick = leftClick;
        Logger.debug("AutoClicker left click set to " + leftClick);
    }
    
    public boolean isRightClick() { 
        return rightClick; 
    }
    
    public void setRightClick(boolean rightClick) { 
        this.rightClick = rightClick;
        Logger.debug("AutoClicker right click set to " + rightClick);
    }
    
    public boolean isUseFuntimeBypass() { 
        return useFuntimeBypass; 
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { 
        this.useFuntimeBypass = useFuntimeBypass;
        Logger.debug("AutoClicker Funtime bypass set to " + useFuntimeBypass);
    }
    
    public boolean isTapeMouse() { 
        return tapeMouse; 
    }
    
    public void setTapeMouse(boolean tapeMouse) { 
        this.tapeMouse = tapeMouse;
        Logger.debug("AutoClicker tape mouse set to " + tapeMouse);
    }
    
    /**
     * Reset to default settings
     */
    public void resetToDefaults() {
        setCps(DEFAULT_CPS);
        setLeftClick(true);
        setRightClick(false);
        setUseFuntimeBypass(true);
        setTapeMouse(true);
        Logger.info("AutoClicker reset to default settings");
    }
    
    /**
     * Get current click rate in clicks per second
     * @return Current CPS value
     */
    public double getClickRate() {
        return cps;
    }
    
    /**
     * Check if any click type is enabled
     * @return true if left or right click is enabled
     */
    public boolean isAnyClickEnabled() {
        return leftClick || rightClick;
    }
}