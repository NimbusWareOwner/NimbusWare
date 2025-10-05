package com.example.cheatclient.mock;

public class MockInteractionManager {
    public void attackEntity(MockPlayer player, MockEntity target) {
        System.out.println("[Interaction] " + player.getName() + " attacks " + target.getName());
    }
}