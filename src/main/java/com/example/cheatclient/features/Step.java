package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class Step extends Module {
    private float stepHeight = 1.0f;
    private float originalStepHeight;
    
    public Step() {
        super("Step", "Allows you to step up higher blocks", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        if (CheatClient.mc.player != null) {
            originalStepHeight = CheatClient.mc.player.stepHeight;
            CheatClient.mc.player.stepHeight = stepHeight;
        }
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.mc.player != null) {
            CheatClient.mc.player.stepHeight = originalStepHeight;
        }
    }
    
    public float getStepHeight() {
        return stepHeight;
    }
    
    public void setStepHeight(float stepHeight) {
        this.stepHeight = stepHeight;
        if (isEnabled() && CheatClient.mc.player != null) {
            CheatClient.mc.player.stepHeight = stepHeight;
        }
    }
}