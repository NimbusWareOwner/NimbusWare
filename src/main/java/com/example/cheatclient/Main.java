package com.example.cheatclient;

import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.utils.Logger;

public class Main {
    public static void main(String[] args) {
        Logger.info("Starting CheatClient...");
        
        try {
            // Initialize the cheat client
            CheatClient client = new CheatClient();
            client.initialize();
            
            // Start the main loop
            startMainLoop(client);
            
        } catch (Exception e) {
            Logger.error("Failed to start CheatClient: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void startMainLoop(CheatClient client) {
        Logger.info("CheatClient started successfully!");
        Logger.info("Press RIGHT SHIFT to open the GUI");
        
        // This is a simplified main loop
        // In a real implementation, you'd integrate with Minecraft's event system
        while (true) {
            try {
                // Process events
                if (client.isInitialized()) {
                    EventManager.TickEvent tickEvent = new EventManager.TickEvent();
                    client.getEventManager().post(tickEvent);
                }
                
                // Sleep to prevent excessive CPU usage
                Thread.sleep(50); // 20 TPS
                
            } catch (InterruptedException e) {
                Logger.warn("Main loop interrupted");
                break;
            } catch (Exception e) {
                Logger.error("Error in main loop: " + e.getMessage());
            }
        }
    }
}