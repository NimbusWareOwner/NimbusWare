package com.example.cheatclient.mock;

import java.util.ArrayList;
import java.util.List;

public class MockPlayer {
    private boolean flying = false;
    private boolean allowFlying = false;
    private float walkSpeed = 0.1f;
    private float flySpeed = 0.05f;
    private float fallDistance = 0.0f;
    private boolean sprinting = false;
    private int age = 0;
    private double x = 0, y = 0, z = 0;
    private double velocityX = 0, velocityY = 0, velocityZ = 0;
    private MockPlayerAbilities abilities = new MockPlayerAbilities();
    
    public MockPlayerAbilities getAbilities() {
        return abilities;
    }
    
    public void setFlying(boolean flying) {
        this.flying = flying;
    }
    
    public boolean isFlying() {
        return flying;
    }
    
    public void setSprinting(boolean sprinting) {
        this.sprinting = sprinting;
    }
    
    public boolean isSprinting() {
        return sprinting;
    }
    
    public void setFallDistance(float fallDistance) {
        this.fallDistance = fallDistance;
    }
    
    public float getFallDistance() {
        return fallDistance;
    }
    
    public void setVelocity(double x, double y, double z) {
        this.velocityX = x;
        this.velocityY = y;
        this.velocityZ = z;
    }
    
    public MockVector3d getVelocity() {
        return new MockVector3d(velocityX, velocityY, velocityZ);
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public int getAge() {
        return age++;
    }
    
    public double distanceTo(MockEntity entity) {
        double dx = x - entity.getX();
        double dy = y - entity.getY();
        double dz = z - entity.getZ();
        return Math.sqrt(dx * dx + dy * dy + dz * dz);
    }
    
    public void swingHand(MockHand hand) {
        // Mock hand swing
    }
    
    public void sendMessage(MockText text, boolean actionBar) {
        System.out.println("[Player] " + text.getString());
    }
    
    public class MockPlayerAbilities {
        public boolean allowFlying = false;
        public boolean flying = false;
        private float walkSpeed = 0.1f;
        private float flySpeed = 0.05f;
        
        public boolean isAllowFlying() { return allowFlying; }
        public void setAllowFlying(boolean allowFlying) { this.allowFlying = allowFlying; }
        public boolean isFlying() { return flying; }
        public void setFlying(boolean flying) { this.flying = flying; }
        public float getWalkSpeed() { return walkSpeed; }
        public void setWalkSpeed(float walkSpeed) { this.walkSpeed = walkSpeed; }
        public float getFlySpeed() { return flySpeed; }
        public void setFlySpeed(float flySpeed) { this.flySpeed = flySpeed; }
    }
}