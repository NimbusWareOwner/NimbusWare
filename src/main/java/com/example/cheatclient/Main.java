package com.example.cheatclient;

import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.utils.Logger;

import java.util.Scanner;

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
        Logger.info("Type 'gui' to open GUI, 'help' for commands, 'quit' to exit");
        
        Scanner scanner = new Scanner(System.in);
        
        // Start background tick simulation
        startTickSimulation(client);
        
        while (true) {
            System.out.print("CheatClient> ");
            String input = "";
            try {
                input = scanner.nextLine().trim().toLowerCase();
            } catch (Exception e) {
                Logger.error("Input error: " + e.getMessage());
                break;
            }
            
            switch (input) {
                case "gui":
                    client.getGuiManager().toggleGui();
                    break;
                case "help":
                    showHelp();
                    break;
                case "quit":
                case "exit":
                    Logger.info("Shutting down CheatClient...");
                    System.exit(0);
                    break;
                case "list":
                    listModules(client);
                    break;
                case "tick":
                    // Simulate tick event
                    EventManager.TickEvent tickEvent = new EventManager.TickEvent();
                    client.getEventManager().post(tickEvent);
                    break;
                default:
                    if (input.startsWith("toggle ")) {
                        String moduleName = input.substring(7);
                        toggleModule(client, moduleName);
                    } else if (!input.isEmpty()) {
                        System.out.println("Unknown command: " + input);
                    }
                    break;
            }
        }
    }
    
    private static void showHelp() {
        System.out.println("\n=== CheatClient Commands ===");
        System.out.println("gui          - Open/Close GUI");
        System.out.println("list         - List all modules");
        System.out.println("toggle <name> - Toggle a module");
        System.out.println("tick         - Simulate game tick");
        System.out.println("help         - Show this help");
        System.out.println("quit/exit    - Exit the program");
        System.out.println("=============================\n");
    }
    
    private static void listModules(CheatClient client) {
        System.out.println("\n=== Available Modules ===");
        for (com.example.cheatclient.core.Module module : client.getModuleManager().getModules()) {
            String status = module.isEnabled() ? "§aON" : "§cOFF";
            System.out.println(module.getName() + " - " + status + " §f(" + module.getDescription() + ")");
        }
        System.out.println("==========================\n");
    }
    
    private static void toggleModule(CheatClient client, String moduleName) {
        com.example.cheatclient.core.Module module = client.getModuleManager().getModule(moduleName);
        if (module != null) {
            module.toggle();
        } else {
            System.out.println("Module not found: " + moduleName);
        }
    }
    
    private static void startTickSimulation(CheatClient client) {
        Thread tickThread = new Thread(() -> {
            while (true) {
                try {
                    // Simulate game tick every 50ms (20 TPS)
                    Thread.sleep(50);
                    
                    // Post tick event to all registered listeners
                    EventManager.TickEvent tickEvent = new EventManager.TickEvent();
                    client.getEventManager().post(tickEvent);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                } catch (Exception e) {
                    Logger.error("Error in tick simulation: " + e.getMessage());
                }
            }
        });
        
        tickThread.setDaemon(true);
        tickThread.start();
    }
}