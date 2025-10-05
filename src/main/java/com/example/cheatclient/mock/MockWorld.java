package com.example.cheatclient.mock;

import java.util.ArrayList;
import java.util.List;

public class MockWorld {
    private List<MockEntity> entities = new ArrayList<>();
    private MockBlockState[][][] blocks = new MockBlockState[100][100][100];
    
    public MockWorld() {
        // Initialize with some mock entities
        entities.add(new MockPlayerEntity("Player1", 10, 64, 10));
        entities.add(new MockMobEntity("Zombie", 15, 64, 15));
        entities.add(new MockItemEntity("Diamond", 20, 64, 20));
    }
    
    public List<MockEntity> getEntities() {
        return entities;
    }
    
    public MockBlockState getBlockState(int x, int y, int z) {
        if (x >= 0 && x < 100 && y >= 0 && y < 100 && z >= 0 && z < 100) {
            return blocks[x][y][z];
        }
        return new MockBlockState();
    }
    
    public void setBlockState(int x, int y, int z, MockBlockState state) {
        if (x >= 0 && x < 100 && y >= 0 && y < 100 && z >= 0 && z < 100) {
            blocks[x][y][z] = state;
        }
    }
}