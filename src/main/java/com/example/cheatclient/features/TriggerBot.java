package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;
import com.example.cheatclient.mock.MockEntity;
import com.example.cheatclient.mock.MockHand;

public class TriggerBot extends Module {
    private int delay = 100; // milliseconds
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
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("TriggerBot");
        }
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getPlayer() == null || CheatClient.INSTANCE.mc.getWorld() == null) {
            return;
        }
        
        // Check if player is holding a weapon
        if (!isHoldingWeapon) {
            return;
        }
        
        // Check delay
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastAttack < delay) {
            return;
        }
        
        // Check if crosshair is on a valid target
        MockEntity target = getCrosshairTarget();
        if (target != null && shouldTarget(target)) {
            attack(target);
            lastAttack = currentTime;
        }
    }
    
    private MockEntity getCrosshairTarget() {
        // Mock implementation - in real client would get crosshair target
        if (CheatClient.INSTANCE.mc.getCrosshairTarget() != null && 
            CheatClient.INSTANCE.mc.getCrosshairTarget().getType() == com.example.cheatclient.mock.MockCrosshairTarget.Type.ENTITY) {
            return CheatClient.INSTANCE.mc.getCrosshairTarget().getTargetEntity();
        }
        return null;
    }
    
    private boolean shouldTarget(MockEntity entity) {
        if (entity instanceof com.example.cheatclient.mock.MockPlayerEntity) {
            return targetPlayers;
        } else if (entity instanceof com.example.cheatclient.mock.MockMobEntity) {
            return targetMobs;
        }
        return false;
    }
    
    private void attack(MockEntity target) {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("TriggerBot", 1.0f);
        }
        
        // Attack the target
        CheatClient.INSTANCE.mc.getInteractionManager().attackEntity(CheatClient.INSTANCE.mc.getPlayer(), target);
        CheatClient.INSTANCE.mc.getPlayer().swingHand(MockHand.MAIN_HAND);
        
        System.out.println("TriggerBot attacked: " + target.getName());
    }
    
    public int getDelay() {
        return delay;
    }
    
    public void setDelay(int delay) {
        this.delay = Math.max(0, delay);
    }
    
    public boolean isTargetPlayers() {
        return targetPlayers;
    }
    
    public void setTargetPlayers(boolean targetPlayers) {
        this.targetPlayers = targetPlayers;
    }
    
    public boolean isTargetMobs() {
        return targetMobs;
    }
    
    public void setTargetMobs(boolean targetMobs) {
        this.targetMobs = targetMobs;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
    
    public boolean isHoldingWeapon() {
        return isHoldingWeapon;
    }
    
    public void setHoldingWeapon(boolean holdingWeapon) {
        isHoldingWeapon = holdingWeapon;
    }
}