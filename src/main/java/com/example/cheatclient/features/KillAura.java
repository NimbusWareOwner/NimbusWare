package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;
import com.example.cheatclient.mock.MockEntity;
import com.example.cheatclient.mock.MockPlayerEntity;
import com.example.cheatclient.mock.MockMobEntity;
import com.example.cheatclient.mock.MockHand;

public class KillAura extends Module {
    private double range = 4.0;
    private boolean targetPlayers = true;
    private boolean targetMobs = true;
    private boolean targetAnimals = false;
    private int attackDelay = 20; // ticks
    private int lastAttack = 0;
    private boolean useMatrixBypass = true;
    private boolean useFuntimeBypass = true;
    private float rotationSpeed = 180.0f;
    private boolean silentRotation = true;
    
    public KillAura() {
        super("KillAura", "Automatically attacks nearby entities with Matrix & Funtime bypasses", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("KillAura");
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("KillAura");
        }
    }
    
    @Override
    protected void onDisable() {
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("KillAura");
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("KillAura");
        }
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getPlayer() == null || CheatClient.INSTANCE.mc.getWorld() == null) {
            return;
        }
        
        // Check attack delay
        if (CheatClient.INSTANCE.mc.getPlayer().age - lastAttack < attackDelay) {
            return;
        }
        
        // Find target
        Entity target = findTarget();
        if (target != null) {
            attack(target);
        }
    }
    
    private MockEntity findTarget() {
        MockEntity closest = null;
        double closestDistance = range;
        
        for (MockEntity entity : CheatClient.INSTANCE.mc.getWorld().getEntities()) {
            if (entity == CheatClient.INSTANCE.mc.getPlayer()) continue;
            if (!shouldTarget(entity)) continue;
            
            double distance = CheatClient.INSTANCE.mc.getPlayer().distanceTo(entity);
            if (distance <= range && distance < closestDistance) {
                closest = entity;
                closestDistance = distance;
            }
        }
        
        return closest;
    }
    
    private boolean shouldTarget(MockEntity entity) {
        if (entity instanceof MockPlayerEntity) {
            return targetPlayers;
        } else if (entity instanceof MockMobEntity) {
            return targetMobs;
        } else {
            return targetAnimals;
        }
    }
    
    private void attack(MockEntity target) {
        // Apply anti-detection modifications
        if (useMatrixBypass || useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("KillAura", 1.0f);
        }
        
        // Rotate to target if silent rotation is enabled
        if (silentRotation) {
            rotateToTarget(target);
        }
        
        // Attack the target
        CheatClient.INSTANCE.mc.getInteractionManager().attackEntity(CheatClient.INSTANCE.mc.getPlayer(), target);
        CheatClient.INSTANCE.mc.getPlayer().swingHand(MockHand.MAIN_HAND);
        lastAttack = CheatClient.INSTANCE.mc.getPlayer().getAge();
    }
    
    private void rotateToTarget(MockEntity target) {
        // Calculate rotation angles to target
        double deltaX = target.getX() - CheatClient.INSTANCE.mc.getPlayer().getX();
        double deltaY = target.getY() - CheatClient.INSTANCE.mc.getPlayer().getY();
        double deltaZ = target.getZ() - CheatClient.INSTANCE.mc.getPlayer().getZ();
        
        // Calculate yaw and pitch
        double distance = Math.sqrt(deltaX * deltaX + deltaZ * deltaZ);
        float yaw = (float) (Math.atan2(deltaZ, deltaX) * 180.0 / Math.PI) - 90.0f;
        float pitch = (float) (-Math.atan2(deltaY, distance) * 180.0 / Math.PI);
        
        // Apply rotation with speed limit
        // In real client, this would modify player rotation
        System.out.println("Rotating to target: yaw=" + yaw + ", pitch=" + pitch);
    }
    
    public double getRange() {
        return range;
    }
    
    public void setRange(double range) {
        this.range = range;
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
    
    public boolean isTargetAnimals() {
        return targetAnimals;
    }
    
    public void setTargetAnimals(boolean targetAnimals) {
        this.targetAnimals = targetAnimals;
    }
    
    public int getAttackDelay() {
        return attackDelay;
    }
    
    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }
    
    public boolean isUseMatrixBypass() {
        return useMatrixBypass;
    }
    
    public void setUseMatrixBypass(boolean useMatrixBypass) {
        this.useMatrixBypass = useMatrixBypass;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
    
    public float getRotationSpeed() {
        return rotationSpeed;
    }
    
    public void setRotationSpeed(float rotationSpeed) {
        this.rotationSpeed = rotationSpeed;
    }
    
    public boolean isSilentRotation() {
        return silentRotation;
    }
    
    public void setSilentRotation(boolean silentRotation) {
        this.silentRotation = silentRotation;
    }
}