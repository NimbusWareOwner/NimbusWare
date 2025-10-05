package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;

public class Jesus extends Module {
    public Jesus() {
        super("Jesus", "Walk on water", Module.Category.MOVEMENT, 0);
    }
    
    @Override
    protected void onEnable() {
        // No special setup needed
    }
    
    @Override
    protected void onDisable() {
        // No cleanup needed
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.mc.player == null) {
            return;
        }
        
        // Check if player is above water
        BlockPos pos = CheatClient.mc.player.getBlockPos();
        BlockState blockState = CheatClient.mc.world.getBlockState(pos);
        
        if (blockState.getBlock() == Blocks.WATER) {
            // Keep player above water surface
            CheatClient.mc.player.setVelocity(CheatClient.mc.player.getVelocity().x, 0.1, CheatClient.mc.player.getVelocity().z);
        }
    }
}