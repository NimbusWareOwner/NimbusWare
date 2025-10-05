package com.example.cheatclient.mock;

public class MockVector3d {
    public final double x, y, z;
    
    public MockVector3d(double x, double y, double z) {
        this.x = x;
        this.y = y;
        this.z = z;
    }
    
    public MockVector3d add(double x, double y, double z) {
        return new MockVector3d(this.x + x, this.y + y, this.z + z);
    }
}