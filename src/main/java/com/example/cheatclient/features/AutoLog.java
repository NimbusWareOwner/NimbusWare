package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoLog extends Module {
    private String regCommand = "/reg pass pass";
    private boolean autoReg = true;
    private int regDelay = 2000; // milliseconds
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
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoLog");
        }
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getPlayer() == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        
        // Auto register if enabled and not already registered
        if (autoReg && !hasRegged && currentTime - lastReg >= regDelay) {
            performRegistration();
            lastReg = currentTime;
            hasRegged = true;
        }
    }
    
    private void performRegistration() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoLog", 1.0f);
        }
        
        // Send registration command
        CheatClient.INSTANCE.mc.getPlayer().sendMessage(
            com.example.cheatclient.mock.MockText.literal(regCommand), 
            false
        );
        
        System.out.println("AutoLog: Sent registration command: " + regCommand);
    }
    
    public String getRegCommand() {
        return regCommand;
    }
    
    public void setRegCommand(String regCommand) {
        this.regCommand = regCommand;
    }
    
    public boolean isAutoReg() {
        return autoReg;
    }
    
    public void setAutoReg(boolean autoReg) {
        this.autoReg = autoReg;
    }
    
    public int getRegDelay() {
        return regDelay;
    }
    
    public void setRegDelay(int regDelay) {
        this.regDelay = Math.max(0, regDelay);
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
    
    public boolean isHasRegged() {
        return hasRegged;
    }
    
    public void setHasRegged(boolean hasRegged) {
        this.hasRegged = hasRegged;
    }
}