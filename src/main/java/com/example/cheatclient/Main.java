package com.example.cheatclient;

import com.example.cheatclient.core.EventManager;
import com.example.cheatclient.core.KeySimulator;
import com.example.cheatclient.utils.Logger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logger.info("Starting NimbusWare...");
        
        try {
            // Initialize the cheat client
            CheatClient client = new CheatClient();
            client.initialize();
            
            // Start the main loop
            startMainLoop(client);
            
        } catch (Exception e) {
            Logger.error("Failed to start NimbusWare: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void startMainLoop(CheatClient client) {
        Logger.info("NimbusWare started successfully!");
        Logger.info("Right Shift - Open main menu");
        Logger.info("Type 'menu' for main menu, 'h' for key help, 'quit' to exit");
        
        Scanner scanner = new Scanner(System.in);
        
        // Start key simulator
        KeySimulator keySimulator = new KeySimulator(client);
        keySimulator.start();
        
        // Start background tick simulation
        startTickSimulation(client);
        
        while (true) {
            System.out.print("NimbusWare> ");
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
                case "menu":
                case "main":
                    client.getMainMenu().open();
                    break;
                case "autobuy":
                    openAutoBuySettings(client);
                    break;
                case "help":
                    showHelp();
                    break;
                case "h":
                    Logger.info("=== NimbusWare Key Bindings ===");
                    Logger.info("Right Shift - Open Main Menu");
                    Logger.info("F1 - Toggle GUI");
                    Logger.info("F2 - List Modules");
                    Logger.info("F3 - AutoBuy Settings");
                    Logger.info("F4 - Account Manager");
                    Logger.info("F5 - Statistics");
                    Logger.info("F6 - Help");
                    Logger.info("Escape - Close Menus");
                    Logger.info("1-0 - Quick Module Toggle");
                    Logger.info("==============================");
                    break;
                case "quit":
                case "exit":
                    Logger.info("Shutting down NimbusWare...");
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
    
    private static void openAutoBuySettings(CheatClient client) {
        com.example.cheatclient.core.Module autobuyModule = client.getModuleManager().getModule("AutoBuy");
        if (autobuyModule != null && autobuyModule instanceof com.example.cheatclient.features.AutoBuy) {
            ((com.example.cheatclient.features.AutoBuy) autobuyModule).openSettingsGui();
        } else {
            System.out.println("AutoBuy module not found!");
        }
    }
    
    private static void showHelp() {
        System.out.println("\n=== NimbusWare Commands ===");
        System.out.println("menu         - Open main menu");
        System.out.println("gui          - Open/Close GUI");
        System.out.println("list         - List all modules");
        System.out.println("toggle <name> - Toggle a module");
        System.out.println("autobuy      - Open AutoBuy settings");
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