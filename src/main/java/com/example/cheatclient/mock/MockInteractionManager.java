package com.example.cheatclient.mock;

public class MockInteractionManager {
    public void attackEntity(MockPlayer player, MockEntity target) {
        System.out.println("[Interaction] Player attacks " + target.getName());
    }
}