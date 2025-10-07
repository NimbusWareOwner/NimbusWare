package com.example.nimbusware;

import com.example.nimbusware.core.EventManager;
import com.example.nimbusware.core.KeySimulator;
import com.example.nimbusware.utils.Logger;

import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        Logger.info("🚀 Starting NimbusWare...");
        
        try {
            // Initialize all systems
            com.example.nimbusware.init.InitializationManager initManager = com.example.nimbusware.init.InitializationManager.getInstance();
            if (!initManager.initializeAll()) {
                Logger.error("❌ Failed to initialize systems");
                return;
            }
            
            // Get the client instance
            NimbusWare client = NimbusWare.INSTANCE;
            
            // Run pre-release checks
            runPreReleaseChecks();
            
            // Start the main loop
            startMainLoop(client);
            
        } catch (Exception e) {
            Logger.error("❌ Failed to start NimbusWare: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private static void runPreReleaseChecks() {
        Logger.info("🔍 Running pre-release checks...");
        
        try {
            com.example.nimbusware.release.ReleaseChecker releaseChecker = com.example.nimbusware.release.ReleaseChecker.getInstance();
            boolean ready = releaseChecker.checkReleaseReadiness();
            
            if (ready) {
                Logger.info("✅ All systems ready for release!");
            } else {
                Logger.warn("⚠️ Some issues found, but continuing...");
                java.util.List<String> issues = releaseChecker.getIssues();
                for (String issue : issues) {
                    Logger.warn("  - " + issue);
                }
            }
        } catch (Exception e) {
            Logger.error("Pre-release check failed", e);
        }
    }
    
    private static void startMainLoop(NimbusWare client) {
        Logger.info("NimbusWare started successfully!");
        Logger.info("Right Shift - Open main menu");
        Logger.info("Type 'menu' for main menu, 'h' for key help, 'quit' to exit");
        
        Scanner scanner = null;
        KeySimulator keySimulator = null;
        Thread tickThread = null;
        
        try {
            scanner = new Scanner(System.in);
            
            // Start key simulator
            keySimulator = new KeySimulator(client);
            keySimulator.start();
            
            // Start background tick simulation
            tickThread = startTickSimulation(client);
            
            while (true) {
                try {
                    System.out.print("NimbusWare> ");
                    String input = "";
                    
                    try {
                        input = scanner.nextLine().trim().toLowerCase();
                    } catch (Exception e) {
                        Logger.error("Input error: " + e.getMessage(), e);
                        break;
                    }
                    
                    if (!processCommand(input, client)) {
                        break;
                    }
                    
                } catch (Exception e) {
                    Logger.error("Error processing command", e);
                    System.out.println("An error occurred. Please try again.");
                }
            }
        } catch (Exception e) {
            Logger.error("Critical error in main loop", e);
        } finally {
            // Cleanup resources
            cleanupResources(scanner, keySimulator, tickThread);
        }
    }
    
    private static boolean processCommand(String input, NimbusWare client) {
        try {
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
                    Logger.info("Escape - Close Menus");
                    Logger.info("1-0 - Quick Module Toggle");
                    Logger.info("==============================");
                    break;
                case "quit":
                case "exit":
                    Logger.info("Shutting down NimbusWare...");
                    return false; // Exit the loop
                case "list":
                    listModules(client);
                    break;
                case "tick":
                    // Simulate tick event
                    EventManager.TickEvent tickEvent = new EventManager.TickEvent();
                    client.getEventManager().post(tickEvent);
                    break;
                case "debug":
                    Logger.setLevel(Logger.Level.DEBUG);
                    Logger.info("Debug mode enabled");
                    break;
                case "info":
                    Logger.setLevel(Logger.Level.INFO);
                    Logger.info("Info level logging enabled");
                    break;
                case "metrics":
                    showMetrics(client);
                    break;
                case "health":
                    showHealthStatus(client);
                    break;
                case "stats":
                    showStatistics(client);
                    break;
                case "plugins":
                    showPlugins(client);
                    break;
                case "secure":
                    showSecureConfig(client);
                    break;
                default:
                    if (input.startsWith("toggle ")) {
                        String moduleName = input.substring(7);
                        toggleModule(client, moduleName);
                    } else if (!input.isEmpty()) {
                        System.out.println("Unknown command: " + input + ". Type 'help' for available commands.");
                    }
                    break;
            }
        } catch (Exception e) {
            Logger.error("Error processing command: " + input, e);
            System.out.println("Command failed: " + e.getMessage());
        }
        return true; // Continue the loop
    }
    
    private static void cleanupResources(Scanner scanner, KeySimulator keySimulator, Thread tickThread) {
        try {
            if (scanner != null) {
                scanner.close();
            }
        } catch (Exception e) {
            Logger.warn("Error closing scanner", e);
        }
        
        try {
            if (keySimulator != null) {
                keySimulator.stop();
            }
        } catch (Exception e) {
            Logger.warn("Error stopping key simulator", e);
        }
        
        try {
            if (tickThread != null) {
                tickThread.interrupt();
            }
        } catch (Exception e) {
            Logger.warn("Error stopping tick thread", e);
        }
        
        // Shutdown logger
        Logger.shutdown();
    }
    
    private static void openAutoBuySettings(NimbusWare client) {
        com.example.nimbusware.core.Module autobuyModule = client.getModuleManager().getModule("AutoBuy");
        if (autobuyModule != null && autobuyModule instanceof com.example.nimbusware.features.AutoBuy) {
            ((com.example.nimbusware.features.AutoBuy) autobuyModule).openSettingsGui();
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
        System.out.println("debug        - Enable debug logging");
        System.out.println("info         - Enable info logging");
        System.out.println("metrics      - Show performance metrics");
        System.out.println("health       - Show system health status");
        System.out.println("stats        - Show usage statistics");
        System.out.println("plugins      - Show loaded plugins");
        System.out.println("secure       - Show secure configuration");
        System.out.println("help         - Show this help");
        System.out.println("quit/exit    - Exit the program");
        System.out.println("=============================\n");
    }
    
    private static void listModules(NimbusWare client) {
        System.out.println("\n=== Available Modules ===");
        for (com.example.nimbusware.core.Module module : client.getModuleManager().getModules()) {
            String status = module.isEnabled() ? "§aON" : "§cOFF";
            System.out.println(module.getName() + " - " + status + " §f(" + module.getDescription() + ")");
        }
        System.out.println("==========================\n");
    }
    
    private static void toggleModule(NimbusWare client, String moduleName) {
        com.example.nimbusware.core.Module module = client.getModuleManager().getModule(moduleName);
        if (module != null) {
            module.toggle();
        } else {
            System.out.println("Module not found: " + moduleName);
        }
    }
    
    private static Thread startTickSimulation(NimbusWare client) {
        Thread tickThread = new Thread(() -> {
            Logger.debug("Tick simulation thread started");
            while (!Thread.currentThread().isInterrupted()) {
                try {
                    // Simulate game tick every 50ms (20 TPS)
                    Thread.sleep(50);
                    
                    // Post tick event to all registered listeners
                    EventManager.TickEvent tickEvent = new EventManager.TickEvent();
                    client.getEventManager().post(tickEvent);
                    
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    Logger.debug("Tick simulation thread interrupted");
                    break;
                } catch (Exception e) {
                    Logger.error("Error in tick simulation", e);
                    // Continue running even if there's an error
                }
            }
            Logger.debug("Tick simulation thread stopped");
        }, "TickSimulation-Thread");
        
        tickThread.setDaemon(true);
        tickThread.start();
        return tickThread;
    }
    
    private static void showMetrics(NimbusWare client) {
        System.out.println("\n=== Performance Metrics ===");
        java.util.Map<String, Object> metrics = client.getMetricsCollector().getAllMetrics();
        metrics.forEach((name, value) -> {
            System.out.println(name + ": " + value);
        });
        System.out.println("===========================\n");
    }
    
    private static void showHealthStatus(NimbusWare client) {
        System.out.println("\n=== System Health Status ===");
        com.example.nimbusware.monitoring.HealthChecker.HealthStatus healthStatus = client.getHealthChecker().checkAllHealth();
        System.out.println("Overall Status: " + (healthStatus.isHealthy() ? "HEALTHY" : "UNHEALTHY"));
        System.out.println("Message: " + healthStatus.getMessage());
        
        java.util.Map<String, com.example.nimbusware.monitoring.HealthChecker.HealthStatus> healthResults = client.getHealthChecker().getHealthResults();
        healthResults.forEach((name, status) -> {
            System.out.println(name + ": " + status);
        });
        System.out.println("=============================\n");
    }
    
    private static void showStatistics(NimbusWare client) {
        System.out.println("\n=== Usage Statistics ===");
        com.example.nimbusware.analytics.StatisticsCollector.StatisticsReport report = client.getStatisticsCollector().getStatisticsReport();
        
        System.out.println("Counters:");
        report.getCounters().forEach((name, value) -> {
            if (value > 0) {
                System.out.println("  " + name + ": " + value);
            }
        });
        
        System.out.println("\nGauges:");
        report.getGauges().forEach((name, value) -> {
            System.out.println("  " + name + ": " + value);
        });
        
        System.out.println("\nModule Usage:");
        java.util.Map<String, com.example.nimbusware.analytics.StatisticsCollector.ModuleUsageStatistics> moduleStats = client.getStatisticsCollector().getAllModuleUsageStatistics();
        moduleStats.forEach((name, stats) -> {
            System.out.println("  " + name + ": " + stats.getActivations() + " activations");
        });
        
        System.out.println("========================\n");
    }
    
    private static void showPlugins(NimbusWare client) {
        System.out.println("\n=== Loaded Plugins ===");
        java.util.Map<String, com.example.nimbusware.plugins.Plugin> plugins = client.getPluginManager().getLoadedPlugins();
        if (plugins.isEmpty()) {
            System.out.println("No plugins loaded");
        } else {
            plugins.forEach((name, plugin) -> {
                System.out.println(name + " v" + plugin.getVersion() + " by " + plugin.getAuthor());
                System.out.println("  Description: " + plugin.getDescription());
                System.out.println("  Status: " + (plugin.isEnabled() ? "Enabled" : "Disabled"));
                System.out.println();
            });
        }
        System.out.println("=====================\n");
    }
    
    private static void showSecureConfig(NimbusWare client) {
        System.out.println("\n=== Secure Configuration ===");
        java.util.Map<String, Boolean> encryptionStatus = client.getSecureConfigManager().getEncryptionStatus();
        System.out.println("Encrypted Keys:");
        encryptionStatus.forEach((key, isEncrypted) -> {
            System.out.println("  " + key + ": " + (isEncrypted ? "Encrypted" : "Not Encrypted"));
        });
        System.out.println("============================\n");
    }
}