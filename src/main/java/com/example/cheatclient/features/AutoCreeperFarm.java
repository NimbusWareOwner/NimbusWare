package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoCreeperFarm extends Module {
    private int farmX = 0;
    private int farmY = 64;
    private int farmZ = 0;
    private int farmRadius = 10;
    private boolean autoKill = true;
    private boolean autoCollect = true;
    private boolean useFuntimeBypass = true;
    private int checkInterval = 1000; // milliseconds
    private long lastCheck = 0;
    
    public AutoCreeperFarm() {
        super("AutoCreeperFarm", "Automatically manages creeper farm at coordinates", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoCreeperFarm");
        }
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoCreeperFarm");
        }
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getPlayer() == null) {
            return;
        }
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastCheck < checkInterval) {
            return;
        }
        
        // Check if player is near the farm
        if (isNearFarm()) {
            if (autoKill) {
                killCreepers();
            }
            if (autoCollect) {
                collectDrops();
            }
        }
        
        lastCheck = currentTime;
    }
    
    private boolean isNearFarm() {
        double playerX = CheatClient.INSTANCE.mc.getPlayer().getX();
        double playerZ = CheatClient.INSTANCE.mc.getPlayer().getZ();
        
        double distance = Math.sqrt(Math.pow(playerX - farmX, 2) + Math.pow(playerZ - farmZ, 2));
        return distance <= farmRadius;
    }
    
    private void killCreepers() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoCreeperFarm", 1.0f);
        }
        
        // Mock creeper killing
        System.out.println("AutoCreeperFarm: Killing creepers at farm");
    }
    
    private void collectDrops() {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoCreeperFarm", 1.0f);
        }
        
        // Mock drop collection
        System.out.println("AutoCreeperFarm: Collecting drops");
    }
    
    public int getFarmX() {
        return farmX;
    }
    
    public void setFarmX(int farmX) {
        this.farmX = farmX;
    }
    
    public int getFarmY() {
        return farmY;
    }
    
    public void setFarmY(int farmY) {
        this.farmY = farmY;
    }
    
    public int getFarmZ() {
        return farmZ;
    }
    
    public void setFarmZ(int farmZ) {
        this.farmZ = farmZ;
    }
    
    public int getFarmRadius() {
        return farmRadius;
    }
    
    public void setFarmRadius(int farmRadius) {
        this.farmRadius = Math.max(1, farmRadius);
    }
    
    public boolean isAutoKill() {
        return autoKill;
    }
    
    public void setAutoKill(boolean autoKill) {
        this.autoKill = autoKill;
    }
    
    public boolean isAutoCollect() {
        return autoCollect;
    }
    
    public void setAutoCollect(boolean autoCollect) {
        this.autoCollect = autoCollect;
    }
    
    public boolean isUseFuntimeBypass() {
        return useFuntimeBypass;
    }
    
    public void setUseFuntimeBypass(boolean useFuntimeBypass) {
        this.useFuntimeBypass = useFuntimeBypass;
    }
    
    public int getCheckInterval() {
        return checkInterval;
    }
    
    public void setCheckInterval(int checkInterval) {
        this.checkInterval = Math.max(100, checkInterval);
    }
}