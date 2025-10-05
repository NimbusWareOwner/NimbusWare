package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;

public class ESP extends Module {
    private boolean showPlayers = true;
    private boolean showMobs = true;
    private boolean showItems = false;
    private boolean showAnimals = false;
    private boolean showBlocks = false;
    private boolean showAncientDebris = true;
    private boolean showOres = true;
    private ESPMode mode = ESPMode.ENTITY;
    
    public enum ESPMode {
        ENTITY,
        BLOCK,
        ITEM,
        ANCIENT
    }
    
    public ESP() {
        super("ESP", "Highlights entities, blocks and items through walls", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        System.out.println("ESP enabled in " + mode + " mode");
    }
    
    @Override
    protected void onDisable() {
        System.out.println("ESP disabled");
    }
    
    public void onRender(float partialTicks) {
        if (!isEnabled()) {
            return;
        }
        
        switch (mode) {
            case ENTITY:
                renderEntityESP();
                break;
            case BLOCK:
                renderBlockESP();
                break;
            case ITEM:
                renderItemESP();
                break;
            case ANCIENT:
                renderAncientESP();
                break;
        }
    }
    
    private void renderEntityESP() {
        System.out.println("Rendering entity ESP");
    }
    
    private void renderBlockESP() {
        System.out.println("Rendering block ESP");
    }
    
    private void renderItemESP() {
        System.out.println("Rendering item ESP");
    }
    
    private void renderAncientESP() {
        System.out.println("Rendering ancient debris ESP");
    }
    
    // Getters and setters
    public boolean isShowPlayers() { return showPlayers; }
    public void setShowPlayers(boolean showPlayers) { this.showPlayers = showPlayers; }
    
    public boolean isShowMobs() { return showMobs; }
    public void setShowMobs(boolean showMobs) { this.showMobs = showMobs; }
    
    public boolean isShowItems() { return showItems; }
    public void setShowItems(boolean showItems) { this.showItems = showItems; }
    
    public boolean isShowAnimals() { return showAnimals; }
    public void setShowAnimals(boolean showAnimals) { this.showAnimals = showAnimals; }
    
    public boolean isShowBlocks() { return showBlocks; }
    public void setShowBlocks(boolean showBlocks) { this.showBlocks = showBlocks; }
    
    public boolean isShowAncientDebris() { return showAncientDebris; }
    public void setShowAncientDebris(boolean showAncientDebris) { this.showAncientDebris = showAncientDebris; }
    
    public boolean isShowOres() { return showOres; }
    public void setShowOres(boolean showOres) { this.showOres = showOres; }
    
    public ESPMode getMode() { return mode; }
    public void setMode(ESPMode mode) { this.mode = mode; }
}