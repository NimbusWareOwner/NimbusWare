package com.example.cheatclient.mock;

public abstract class MockEntity {
    protected String name;
    protected double x, y, z;
    
    public MockEntity(String name, double x, double y, double z) {
        this.name = name;
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public String getName() {
        return name;
    }
    
    public double getX() { return x; }
    public double getY() { return y; }
    public double getZ() { return z; }
    
    public void setPosition(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
}