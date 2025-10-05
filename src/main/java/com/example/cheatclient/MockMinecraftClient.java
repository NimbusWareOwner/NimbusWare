package com.example.cheatclient;

import com.example.cheatclient.mock.*;

public class MockMinecraftClient {
    private MockWindow window;
    private MockPlayer player;
    private MockWorld world;
    private MockInteractionManager interactionManager;
    private MockCrosshairTarget crosshairTarget;
    private MockBufferBuilders bufferBuilders;
    private MockOptions options;
    
    public MockMinecraftClient() {
        this.window = new MockWindow();
        this.player = new MockPlayer();
        this.world = new MockWorld();
        this.interactionManager = new MockInteractionManager();
        this.crosshairTarget = new MockCrosshairTarget();
        this.bufferBuilders = new MockBufferBuilders();
        this.options = new MockOptions();
    }
    
    public MockWindow getWindow() {
        return window;
    }
    
    public MockPlayer getPlayer() {
        return player;
    }
    
    public MockWorld getWorld() {
        return world;
    }
    
    public MockInteractionManager getInteractionManager() {
        return interactionManager;
    }
    
    public MockCrosshairTarget getCrosshairTarget() {
        return crosshairTarget;
    }
    
    public MockBufferBuilders getBufferBuilders() {
        return bufferBuilders;
    }
    
    public MockOptions getOptions() {
        return options;
    }
}