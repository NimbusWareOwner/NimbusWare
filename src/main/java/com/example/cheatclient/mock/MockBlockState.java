package com.example.cheatclient.mock;

public class MockBlockState {
    private String blockName = "air";
    
    public MockBlockState() {}
    
    public MockBlockState(String blockName) {
        this.blockName = blockName;
    }
    
    public String getBlockName() {
        return blockName;
    }
    
    public void setBlockName(String blockName) {
        this.blockName = blockName;
    }
}