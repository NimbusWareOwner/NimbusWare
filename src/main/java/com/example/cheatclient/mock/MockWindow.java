package com.example.cheatclient.mock;

import org.lwjgl.glfw.GLFW;

public class MockWindow {
    private long handle;
    
    public MockWindow() {
        this.handle = GLFW.glfwCreateWindow(800, 600, "CheatClient Demo", 0, 0);
    }
    
    public long getHandle() {
        return handle;
    }
}