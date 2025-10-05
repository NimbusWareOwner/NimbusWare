package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;

public class AutoFish extends Module {
    private boolean useCustomEnchants = true;
    private float fishingSpeed = 1.5f;
    private boolean autoReel = true;
    private boolean autoCast = true;
    private int castDelay = 1000;
    private int reelDelay = 500;
    private long lastAction = 0;
    private boolean isFishing = false;
    
    public AutoFish() {
        super("AutoFish", "Automatically fishes with custom enchant support", Module.Category.PLAYER, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useCustomEnchants) {
            AntiDetectionManager.enableFuntimeBypass("AutoFish");
        }
        System.out.println("AutoFish enabled with " + fishingSpeed + "x speed");
    }
    
    @Override
    protected void onDisable() {
        if (useCustomEnchants) {
            AntiDetectionManager.disableFuntimeBypass("AutoFish");
        }
        System.out.println("AutoFish disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        long currentTime = System.currentTimeMillis();
        
        if (!isFishing && autoCast) {
            if (currentTime - lastAction >= castDelay) {
                castFishingRod();
                lastAction = currentTime;
                isFishing = true;
            }
        } else if (isFishing && autoReel) {
            if (currentTime - lastAction >= reelDelay) {
                reelFishingRod();
                lastAction = currentTime;
                isFishing = false;
            }
        }
    }
    
    private void castFishingRod() {
        if (useCustomEnchants) {
            AntiDetectionManager.applyCombatModification("AutoFish", fishingSpeed);
        }
        System.out.println("AutoFish: Casting fishing rod");
    }
    
    private void reelFishingRod() {
        if (useCustomEnchants) {
            AntiDetectionManager.applyCombatModification("AutoFish", fishingSpeed);
        }
        System.out.println("AutoFish: Reeling fishing rod");
    }
    
    // Getters and setters
    public boolean isUseCustomEnchants() { return useCustomEnchants; }
    public void setUseCustomEnchants(boolean useCustomEnchants) { this.useCustomEnchants = useCustomEnchants; }
    
    public float getFishingSpeed() { return fishingSpeed; }
    public void setFishingSpeed(float fishingSpeed) { this.fishingSpeed = Math.max(0.1f, Math.min(5.0f, fishingSpeed)); }
    
    public boolean isAutoReel() { return autoReel; }
    public void setAutoReel(boolean autoReel) { this.autoReel = autoReel; }
    
    public boolean isAutoCast() { return autoCast; }
    public void setAutoCast(boolean autoCast) { this.autoCast = autoCast; }
    
    public int getCastDelay() { return castDelay; }
    public void setCastDelay(int castDelay) { this.castDelay = Math.max(100, castDelay); }
    
    public int getReelDelay() { return reelDelay; }
    public void setReelDelay(int reelDelay) { this.reelDelay = Math.max(100, reelDelay); }
}