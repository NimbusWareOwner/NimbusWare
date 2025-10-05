package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.mock.MockBlockState;

import java.util.HashSet;
import java.util.Set;

public class XRay extends Module {
    private final Set<String> visibleBlocks = new HashSet<>();
    private XRayMode mode = XRayMode.NORMAL;
    private boolean showCoal = true;
    private boolean showIron = true;
    private boolean showGold = true;
    private boolean showDiamond = true;
    private boolean showEmerald = true;
    private boolean showRedstone = true;
    private boolean showLapis = true;
    private boolean showQuartz = true;
    private boolean showAncientDebris = true;
    
    public enum XRayMode {
        NORMAL,
        ANCIENT
    }
    
    public XRay() {
        super("XRay", "See through blocks to find ores with 2 modes", Module.Category.RENDER, 0);
        
        // Add important blocks to see through
        updateVisibleBlocks();
    }
    
    @Override
    protected void onEnable() {
        updateVisibleBlocks();
        if (CheatClient.INSTANCE.mc.getWorld() != null) {
            // Refresh world rendering
            // CheatClient.INSTANCE.mc.getWorld()Renderer.reload();
        }
    }
    
    @Override
    protected void onDisable() {
        if (CheatClient.INSTANCE.mc.getWorld() != null) {
            // Refresh world rendering
            // CheatClient.INSTANCE.mc.getWorld()Renderer.reload();
        }
    }
    
    private void updateVisibleBlocks() {
        visibleBlocks.clear();
        
        if (mode == XRayMode.ANCIENT) {
            if (showAncientDebris) {
                visibleBlocks.add("ancient_debris");
            }
        } else {
            if (showCoal) visibleBlocks.add("coal_ore");
            if (showIron) visibleBlocks.add("iron_ore");
            if (showGold) visibleBlocks.add("gold_ore");
            if (showDiamond) visibleBlocks.add("diamond_ore");
            if (showEmerald) visibleBlocks.add("emerald_ore");
            if (showRedstone) visibleBlocks.add("redstone_ore");
            if (showLapis) visibleBlocks.add("lapis_ore");
            if (showQuartz) visibleBlocks.add("nether_quartz_ore");
            if (showAncientDebris) visibleBlocks.add("ancient_debris");
        }
    }
    
    public boolean shouldRenderBlock(int x, int y, int z) {
        if (!isEnabled() || CheatClient.INSTANCE.mc.getWorld() == null) {
            return true;
        }
        
        MockBlockState blockState = CheatClient.INSTANCE.mc.getWorld().getBlockState(x, y, z);
        String blockName = blockState.getBlockName().toLowerCase();
        
        return visibleBlocks.contains(blockName);
    }
    
    public XRayMode getMode() {
        return mode;
    }
    
    public void setMode(XRayMode mode) {
        this.mode = mode;
        updateVisibleBlocks();
    }
    
    public boolean isShowCoal() { return showCoal; }
    public void setShowCoal(boolean showCoal) { this.showCoal = showCoal; updateVisibleBlocks(); }
    
    public boolean isShowIron() { return showIron; }
    public void setShowIron(boolean showIron) { this.showIron = showIron; updateVisibleBlocks(); }
    
    public boolean isShowGold() { return showGold; }
    public void setShowGold(boolean showGold) { this.showGold = showGold; updateVisibleBlocks(); }
    
    public boolean isShowDiamond() { return showDiamond; }
    public void setShowDiamond(boolean showDiamond) { this.showDiamond = showDiamond; updateVisibleBlocks(); }
    
    public boolean isShowEmerald() { return showEmerald; }
    public void setShowEmerald(boolean showEmerald) { this.showEmerald = showEmerald; updateVisibleBlocks(); }
    
    public boolean isShowRedstone() { return showRedstone; }
    public void setShowRedstone(boolean showRedstone) { this.showRedstone = showRedstone; updateVisibleBlocks(); }
    
    public boolean isShowLapis() { return showLapis; }
    public void setShowLapis(boolean showLapis) { this.showLapis = showLapis; updateVisibleBlocks(); }
    
    public boolean isShowQuartz() { return showQuartz; }
    public void setShowQuartz(boolean showQuartz) { this.showQuartz = showQuartz; updateVisibleBlocks(); }
    
    public boolean isShowAncientDebris() { return showAncientDebris; }
    public void setShowAncientDebris(boolean showAncientDebris) { this.showAncientDebris = showAncientDebris; updateVisibleBlocks(); }
}