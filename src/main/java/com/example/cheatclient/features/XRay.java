package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import net.minecraft.block.Block;
import net.minecraft.block.Blocks;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;

import java.util.HashSet;
import java.util.Set;

public class XRay extends Module {
    private final Set<Block> visibleBlocks = new HashSet<>();
    
    public XRay() {
        super("XRay", "See through blocks to find ores", Module.Category.RENDER, 0);
        
        // Add important blocks to see through
        visibleBlocks.add(Blocks.COAL_ORE);
        visibleBlocks.add(Blocks.IRON_ORE);
        visibleBlocks.add(Blocks.GOLD_ORE);
        visibleBlocks.add(Blocks.DIAMOND_ORE);
        visibleBlocks.add(Blocks.EMERALD_ORE);
        visibleBlocks.add(Blocks.REDSTONE_ORE);
        visibleBlocks.add(Blocks.LAPIS_ORE);
        visibleBlocks.add(Blocks.NETHER_GOLD_ORE);
        visibleBlocks.add(Blocks.NETHER_QUARTZ_ORE);
        visibleBlocks.add(Blocks.ANCIENT_DEBRIS);
    }
    
    @Override
    protected void onEnable() {
        if (CheatClient.mc.world != null) {
            // Refresh world rendering
            CheatClient.mc.worldRenderer.reload();
        }
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.mc.world != null) {
            // Refresh world rendering
            CheatClient.mc.worldRenderer.reload();
        }
    }
    
    public boolean shouldRenderBlock(BlockPos pos) {
        if (!isEnabled() || CheatClient.mc.world == null) {
            return true;
        }
        
        Block block = CheatClient.mc.world.getBlockState(pos).getBlock();
        return visibleBlocks.contains(block);
    }
    
    public Set<Block> getVisibleBlocks() {
        return visibleBlocks;
    }
    
    public void addVisibleBlock(Block block) {
        visibleBlocks.add(block);
    }
    
    public void removeVisibleBlock(Block block) {
        visibleBlocks.remove(block);
    }
}