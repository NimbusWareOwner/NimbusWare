package com.example.cheatclient.mock;

public class MockCrosshairTarget {
    private MockEntity targetEntity;
    private Type type = Type.MISS;
    
    public enum Type {
        MISS, BLOCK, ENTITY
    }
    
    public MockEntity getTargetEntity() {
        return targetEntity;
    }
    
    public void setTargetEntity(MockEntity targetEntity) {
        this.targetEntity = targetEntity;
        this.type = Type.ENTITY;
    }
    
    public Type getType() {
        return type;
    }
    
    public void setType(Type type) {
        this.type = type;
    }
}