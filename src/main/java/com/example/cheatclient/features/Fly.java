package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.mock.MockPlayer;

public class Fly extends Module {
    private float originalFlySpeed;
    private boolean originalAllowFlying;
    private boolean originalFlying;
    
    public Fly() {
        super("Fly", "Allows you to fly like in creative mode", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (CheatClient.mc.player != null) {
            MockPlayer player = CheatClient.mc.player;
            MockPlayer.MockPlayerAbilities abilities = player.getAbilities();
            
            // Save original values
            originalFlySpeed = abilities.getFlySpeed();
            originalAllowFlying = abilities.allowFlying;
            originalFlying = abilities.flying;
            
            // Enable flying
            abilities.allowFlying = true;
            abilities.flying = true;
            abilities.setFlySpeed(0.05f);
        }
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.mc.player != null) {
            MockPlayer player = CheatClient.mc.player;
            MockPlayer.MockPlayerAbilities abilities = player.getAbilities();
            
            // Restore original values
            abilities.allowFlying = originalAllowFlying;
            abilities.flying = originalFlying;
            abilities.setFlySpeed(originalFlySpeed);
        }
    }
    
    public void onTick() {
        if (CheatClient.mc.player != null && isEnabled()) {
            ClientPlayerEntity player = CheatClient.mc.player;
            PlayerAbilities abilities = player.getAbilities();
            
            // Ensure flying is always enabled while module is active
            abilities.allowFlying = true;
            abilities.flying = true;
            
            // Handle movement
            if (CheatClient.mc.options.jumpKey.isPressed()) {
                player.setVelocity(player.getVelocity().add(0, 0.1, 0));
            }
            if (CheatClient.mc.options.sneakKey.isPressed()) {
                player.setVelocity(player.getVelocity().add(0, -0.1, 0));
            }
        }
    }
}