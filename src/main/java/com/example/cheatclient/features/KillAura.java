package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

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
        System.out.println("KillAura enabled with range: " + range);
    }
    
    @Override
    protected void onDisable() {
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("KillAura");
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("KillAura");
        }
        System.out.println("KillAura disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) {
            return;
        }
        
        // Mock killaura logic
        if (Math.random() < 0.1) { // 10% chance to simulate attack
            System.out.println("KillAura: Attacking nearby entity");
        }
    }
    
    // Getters and setters
    public double getRange() { return range; }
    public void setRange(double range) { this.range = range; }
    
    public boolean isTargetPlayers() { return targetPlayers; }
    public void setTargetPlayers(boolean targetPlayers) { this.targetPlayers = targetPlayers; }
    
    public boolean isTargetMobs() { return targetMobs; }
    public void setTargetMobs(boolean targetMobs) { this.targetMobs = targetMobs; }
    
    public boolean isTargetAnimals() { return targetAnimals; }
    public void setTargetAnimals(boolean targetAnimals) { this.targetAnimals = targetAnimals; }
    
    public int getAttackDelay() { return attackDelay; }
    public void setAttackDelay(int attackDelay) { this.attackDelay = attackDelay; }
    
    public boolean isUseMatrixBypass() { return useMatrixBypass; }
    public void setUseMatrixBypass(boolean useMatrixBypass) { this.useMatrixBypass = useMatrixBypass; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public float getRotationSpeed() { return rotationSpeed; }
    public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }
    
    public boolean isSilentRotation() { return silentRotation; }
    public void setSilentRotation(boolean silentRotation) { this.silentRotation = silentRotation; }
}