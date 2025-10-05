package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoLog extends Module {
    private String regCommand = "/reg pass pass";
    private boolean autoReg = true;
    private int regDelay = 2000;
    private boolean useFuntimeBypass = true;
    private long lastReg = 0;
    private boolean hasRegged = false;
    
    public AutoLog() {
        super("AutoLog", "Automatically logs out and registers", Module.Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoLog");
        }
        System.out.println("AutoLog enabled with command: " + regCommand);
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoLog");
        }
        System.out.println("AutoLog disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        long currentTime = System.currentTimeMillis();
        
        if (autoReg && !hasRegged && currentTime - lastReg >= regDelay) {
            performRegistration();
            lastReg = currentTime;
            hasRegged = true;
        }
    }
    
    private void performRegistration() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoLog", 1.0f);
        }
        System.out.println("AutoLog: Sent registration command: " + regCommand);
    }
    
    // Getters and setters
    public String getRegCommand() { return regCommand; }
    public void setRegCommand(String regCommand) { this.regCommand = regCommand; }
    
    public boolean isAutoReg() { return autoReg; }
    public void setAutoReg(boolean autoReg) { this.autoReg = autoReg; }
    
    public int getRegDelay() { return regDelay; }
    public void setRegDelay(int regDelay) { this.regDelay = Math.max(0, regDelay); }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isHasRegged() { return hasRegged; }
    public void setHasRegged(boolean hasRegged) { this.hasRegged = hasRegged; }
}