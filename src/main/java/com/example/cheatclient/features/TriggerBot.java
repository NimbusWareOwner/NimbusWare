package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class TriggerBot extends Module {
    private int delay = 100;
    private boolean targetPlayers = true;
    private boolean targetMobs = true;
    private boolean useFuntimeBypass = true;
    private long lastAttack = 0;
    private boolean isHoldingWeapon = false;
    
    public TriggerBot() {
        super("TriggerBot", "Automatically attacks when crosshair is on target", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("TriggerBot");
        }
        System.out.println("TriggerBot enabled with " + delay + "ms delay");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("TriggerBot");
        }
        System.out.println("TriggerBot disabled");
    }
    
    public void onTick() {
        if (!isEnabled() || !isHoldingWeapon) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttack < delay) return;
        
        // Mock triggerbot logic
        if (Math.random() < 0.1) { // 10% chance to attack
            attack();
            lastAttack = currentTime;
        }
    }
    
    private void attack() {
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("TriggerBot", 1.0f);
        }
        System.out.println("TriggerBot: Attacked target");
    }
    
    // Getters and setters
    public int getDelay() { return delay; }
    public void setDelay(int delay) { this.delay = Math.max(0, delay); }
    
    public boolean isTargetPlayers() { return targetPlayers; }
    public void setTargetPlayers(boolean targetPlayers) { this.targetPlayers = targetPlayers; }
    
    public boolean isTargetMobs() { return targetMobs; }
    public void setTargetMobs(boolean targetMobs) { this.targetMobs = targetMobs; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isHoldingWeapon() { return isHoldingWeapon; }
    public void setHoldingWeapon(boolean holdingWeapon) { isHoldingWeapon = holdingWeapon; }
}