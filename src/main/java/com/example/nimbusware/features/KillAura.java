package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;
import com.example.nimbusware.utils.ValidationUtils;

import java.util.Random;

/**
 * Advanced KillAura module with realistic combat simulation
 */
public class KillAura extends Module {
    private final Random random = new Random();
    private int lastAttack = 0;
    private int attackCount = 0;
    private long lastTargetSwitch = 0;
    private String currentTarget = null;
    
    public KillAura() {
        super("KillAura", "Automatically attacks nearby entities with advanced anti-detection", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void initializeDefaultSettings() {
        settings.set("range", 4.0);
        settings.set("targetPlayers", true);
        settings.set("targetMobs", true);
        settings.set("targetAnimals", false);
        settings.set("attackDelay", 20);
        settings.set("useMatrixBypass", true);
        settings.set("useFuntimeBypass", true);
        settings.set("rotationSpeed", 180.0f);
        settings.set("silentRotation", true);
        settings.set("maxRange", 6.0);
        settings.set("minRange", 2.0);
        settings.set("randomizeDelay", true);
        settings.set("humanLikeRotation", true);
        settings.set("attackPattern", "normal"); // normal, burst, sustained
        settings.set("burstCount", 3);
        settings.set("burstDelay", 5);
    }
    
    @Override
    protected void onEnable() {
        boolean useMatrixBypass = settings.getBoolean("useMatrixBypass", true);
        boolean useFuntimeBypass = settings.getBoolean("useFuntimeBypass", true);
        
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("KillAura");
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("KillAura");
        }
        
        double range = settings.getDouble("range", 4.0);
        Logger.info("KillAura enabled with range: " + range);
    }
    
    @Override
    protected void onDisable() {
        boolean useMatrixBypass = settings.getBoolean("useMatrixBypass", true);
        boolean useFuntimeBypass = settings.getBoolean("useFuntimeBypass", true);
        
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("KillAura");
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("KillAura");
        }
        
        currentTarget = null;
        attackCount = 0;
        Logger.info("KillAura disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        try {
            // Simulate realistic combat behavior
            simulateCombat();
        } catch (Exception e) {
            Logger.error("Error in KillAura tick", e);
        }
    }
    
    private void simulateCombat() {
        double range = settings.getDouble("range", 4.0);
        boolean targetPlayers = settings.getBoolean("targetPlayers", true);
        boolean targetMobs = settings.getBoolean("targetMobs", true);
        boolean targetAnimals = settings.getBoolean("targetAnimals", false);
        int attackDelay = settings.getInt("attackDelay", 20);
        boolean randomizeDelay = settings.getBoolean("randomizeDelay", true);
        String attackPattern = settings.getString("attackPattern", "normal");
        
        // Simulate target detection
        String target = findTarget(range, targetPlayers, targetMobs, targetAnimals);
        
        if (target != null) {
            if (!target.equals(currentTarget)) {
                currentTarget = target;
                lastTargetSwitch = System.currentTimeMillis();
                Logger.debug("KillAura: New target acquired: " + target);
            }
            
            // Calculate attack delay with randomization
            int actualDelay = attackDelay;
            if (randomizeDelay) {
                actualDelay += random.nextInt(5) - 2; // ±2 tick variation
            }
            
            // Check if we can attack
            if (System.currentTimeMillis() - lastAttack >= actualDelay * 50) { // Convert ticks to ms
                performAttack(target, attackPattern);
                lastAttack = (int) System.currentTimeMillis();
            }
        } else {
            if (currentTarget != null) {
                Logger.debug("KillAura: Target lost");
                currentTarget = null;
            }
        }
    }
    
    private String findTarget(double range, boolean targetPlayers, boolean targetMobs, boolean targetAnimals) {
        // Simulate target detection with realistic behavior
        double detectionChance = 0.3; // 30% chance to detect target per tick
        
        if (random.nextDouble() < detectionChance) {
            // Simulate different target types
            if (targetPlayers && random.nextDouble() < 0.4) {
                return "Player_" + random.nextInt(100);
            } else if (targetMobs && random.nextDouble() < 0.5) {
                String[] mobs = {"Zombie", "Skeleton", "Spider", "Creeper", "Enderman"};
                return mobs[random.nextInt(mobs.length)] + "_" + random.nextInt(50);
            } else if (targetAnimals && random.nextDouble() < 0.3) {
                String[] animals = {"Cow", "Pig", "Sheep", "Chicken"};
                return animals[random.nextInt(animals.length)] + "_" + random.nextInt(30);
            }
        }
        
        return null;
    }
    
    private void performAttack(String target, String attackPattern) {
        attackCount++;
        
        // Apply anti-detection modifications
        boolean useMatrixBypass = settings.getBoolean("useMatrixBypass", true);
        boolean useFuntimeBypass = settings.getBoolean("useFuntimeBypass", true);
        
        if (useMatrixBypass) {
            AntiDetectionManager.applyCombatModification("KillAura", 1.0f);
        }
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("KillAura", 1.0f);
        }
        
        // Simulate different attack patterns
        switch (attackPattern) {
            case "burst":
                performBurstAttack(target);
                break;
            case "sustained":
                performSustainedAttack(target);
                break;
            default:
                performNormalAttack(target);
                break;
        }
        
        // Record statistics
        if (com.example.nimbusware.NimbusWare.INSTANCE != null) {
            com.example.nimbusware.NimbusWare.INSTANCE.getStatisticsCollector().recordModuleUsage("KillAura", 50);
        }
        
        Logger.debug("KillAura: Attacked " + target + " (Attack #" + attackCount + ")");
    }
    
    private void performNormalAttack(String target) {
        // Simulate normal attack with human-like timing
        float rotationSpeed = (float) settings.getDouble("rotationSpeed", 180.0f);
        boolean humanLikeRotation = settings.getBoolean("humanLikeRotation", true);
        
        if (humanLikeRotation) {
            // Add slight rotation delay for realism
            try {
                Thread.sleep(random.nextInt(10) + 5); // 5-15ms delay
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        // Simulate attack action
        System.out.println("⚔️ KillAura: Attacking " + target);
    }
    
    private void performBurstAttack(String target) {
        int burstCount = settings.getInt("burstCount", 3);
        int burstDelay = settings.getInt("burstDelay", 5);
        
        for (int i = 0; i < burstCount; i++) {
            System.out.println("⚔️ KillAura: Burst attack " + (i + 1) + "/" + burstCount + " on " + target);
            
            if (i < burstCount - 1) {
                try {
                    Thread.sleep(burstDelay);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }
    }
    
    private void performSustainedAttack(String target) {
        // Simulate sustained attack with consistent timing
        System.out.println("⚔️ KillAura: Sustained attack on " + target);
    }
    
    // Convenience methods for common settings
    public double getRange() { 
        return settings.getDouble("range", 4.0); 
    }
    
    public void setRange(double range) { 
        settings.set("range", range); 
    }
    
    public boolean isTargetPlayers() { 
        return settings.getBoolean("targetPlayers", true); 
    }
    
    public void setTargetPlayers(boolean targetPlayers) { 
        settings.set("targetPlayers", targetPlayers); 
    }
    
    public boolean isTargetMobs() { 
        return settings.getBoolean("targetMobs", true); 
    }
    
    public void setTargetMobs(boolean targetMobs) { 
        settings.set("targetMobs", targetMobs); 
    }
    
    public boolean isTargetAnimals() { 
        return settings.getBoolean("targetAnimals", false); 
    }
    
    public void setTargetAnimals(boolean targetAnimals) { 
        settings.set("targetAnimals", targetAnimals); 
    }
    
    public int getAttackDelay() { 
        return settings.getInt("attackDelay", 20); 
    }
    
    public void setAttackDelay(int attackDelay) { 
        settings.set("attackDelay", attackDelay); 
    }
    
    public boolean isUseMatrixBypass() { 
        return settings.getBoolean("useMatrixBypass", true); 
    }
    
    public void setUseMatrixBypass(boolean useMatrixBypass) { 
        settings.set("useMatrixBypass", useMatrixBypass); 
    }
    
    public boolean isUseFuntimeBypass() { 
        return settings.getBoolean("useFuntimeBypass", true); 
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { 
        settings.set("useFuntimeBypass", useFuntimeBypass); 
    }
    
    public float getRotationSpeed() { 
        return (float) settings.getDouble("rotationSpeed", 180.0f); 
    }
    
    public void setRotationSpeed(float rotationSpeed) { 
        settings.set("rotationSpeed", rotationSpeed); 
    }
    
    public boolean isSilentRotation() { 
        return settings.getBoolean("silentRotation", true); 
    }
    
    public void setSilentRotation(boolean silentRotation) { 
        settings.set("silentRotation", silentRotation); 
    }
    
    /**
     * Get attack statistics
     * @return Attack statistics
     */
    public int getAttackCount() {
        return attackCount;
    }
    
    /**
     * Get current target
     * @return Current target name or null
     */
    public String getCurrentTarget() {
        return currentTarget;
    }
    
    /**
     * Reset attack statistics
     */
    public void resetAttackStats() {
        attackCount = 0;
        currentTarget = null;
        lastTargetSwitch = 0;
    }
}