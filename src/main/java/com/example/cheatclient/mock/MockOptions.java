package com.example.cheatclient.mock;

public class MockOptions {
    private MockKeyBinding forwardKey = new MockKeyBinding("Forward");
    private MockKeyBinding backKey = new MockKeyBinding("Back");
    private MockKeyBinding leftKey = new MockKeyBinding("Left");
    private MockKeyBinding rightKey = new MockKeyBinding("Right");
    private MockKeyBinding jumpKey = new MockKeyBinding("Jump");
    private MockKeyBinding sneakKey = new MockKeyBinding("Sneak");
    
    public MockKeyBinding getForwardKey() { return forwardKey; }
    public MockKeyBinding getBackKey() { return backKey; }
    public MockKeyBinding getLeftKey() { return leftKey; }
    public MockKeyBinding getRightKey() { return rightKey; }
    public MockKeyBinding getJumpKey() { return jumpKey; }
    public MockKeyBinding getSneakKey() { return sneakKey; }
    
    public static class MockKeyBinding {
        private String name;
        private boolean pressed = false;
        
        public MockKeyBinding(String name) {
            this.name = name;
        }
        
        public boolean isPressed() {
            return pressed;
        }
        
        public void setPressed(boolean pressed) {
            this.pressed = pressed;
        }
    }
}