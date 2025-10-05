package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;

public class Speed extends Module {
    private float originalWalkSpeed;
    private float speedMultiplier = 2.0f;
    
    public Speed() {
        super("Speed", "Increases your movement speed", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (CheatClient.mc.player != null) {
            ClientPlayerEntity player = CheatClient.mc.player;
            PlayerAbilities abilities = player.getAbilities();
            
            // Save original speed
            originalWalkSpeed = abilities.getWalkSpeed();
            
            // Apply speed multiplier
            abilities.setWalkSpeed(originalWalkSpeed * speedMultiplier);
        }
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.mc.player != null) {
            ClientPlayerEntity player = CheatClient.mc.player;
            PlayerAbilities abilities = player.getAbilities();
            
            // Restore original speed
            abilities.setWalkSpeed(originalWalkSpeed);
        }
    }
    
    public void onTick() {
        if (CheatClient.mc.player != null && isEnabled()) {
            ClientPlayerEntity player = CheatClient.mc.player;
            PlayerAbilities abilities = player.getAbilities();
            
            // Ensure speed is applied
            abilities.setWalkSpeed(originalWalkSpeed * speedMultiplier);
        }
    }
    
    public void setSpeedMultiplier(float multiplier) {
        this.speedMultiplier = multiplier;
        if (isEnabled() && CheatClient.mc.player != null) {
            CheatClient.mc.player.getAbilities().setWalkSpeed(originalWalkSpeed * speedMultiplier);
        }
    }
    
    public float getSpeedMultiplier() {
        return speedMultiplier;
    }
}