package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.utils.Logger;

import java.util.Scanner;

public class MainMenu {
    private boolean isOpen = false;
    private Scanner scanner;
    private CheatClient client;
    
    public MainMenu(CheatClient client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
    }
    
    public void open() {
        isOpen = true;
        showMainMenu();
    }
    
    public void close() {
        isOpen = false;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    private void showMainMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            printMainMenu();
            
            System.out.print("CheatClient> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                case "modules":
                    showModulesMenu();
                    break;
                case "2":
                case "autobuy":
                    showAutoBuyMenu();
                    break;
                case "3":
                case "account":
                case "accounts":
                    showAccountManager();
                    break;
                case "4":
                case "money":
                    showMoneyMakingMenu();
                    break;
                case "5":
                case "settings":
                    showSettingsMenu();
                    break;
                case "6":
                case "stats":
                    showStatisticsMenu();
                    break;
                case "7":
                case "help":
                    showHelpMenu();
                    break;
                case "8":
                case "quit":
                case "exit":
                    close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showModulesMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("🔧 MODULE MANAGEMENT");
            System.out.println("====================");
            System.out.println("1. View All Modules");
            System.out.println("2. Toggle Module");
            System.out.println("3. Module Settings");
            System.out.println("4. Enable All Modules");
            System.out.println("5. Disable All Modules");
            System.out.println("6. Back to Main Menu");
            
            System.out.print("Modules> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showAllModules();
                    break;
                case "2":
                    toggleModule();
                    break;
                case "3":
                    showModuleSettings();
                    break;
                case "4":
                    enableAllModules();
                    break;
                case "5":
                    disableAllModules();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showAutoBuyMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("🛒 AUTOBUY SYSTEM");
            System.out.println("=================");
            System.out.println("1. AutoBuy Settings");
            System.out.println("2. Item Management");
            System.out.println("3. Purchase History");
            System.out.println("4. Statistics");
            System.out.println("5. Demo & Simulation");
            System.out.println("6. Back to Main Menu");
            
            System.out.print("AutoBuy> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showAutoBuySettings();
                    break;
                case "2":
                    showItemManagement();
                    break;
                case "3":
                    showPurchaseHistory();
                    break;
                case "4":
                    showAutoBuyStatistics();
                    break;
                case "5":
                    showAutoBuyDemo();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showAccountManager() {
        client.getAccountManager().open();
    }
    
    private void showMoneyMakingMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("💰 MONEY MAKING");
            System.out.println("===============");
            System.out.println("1. SwordCraft");
            System.out.println("2. ChestStealer");
            System.out.println("3. AutoFarm");
            System.out.println("4. AutoMine");
            System.out.println("5. AutoFish");
            System.out.println("6. Profit Calculator");
            System.out.println("7. Back to Main Menu");
            
            System.out.print("Money> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showSwordCraftMenu();
                    break;
                case "2":
                    showChestStealerMenu();
                    break;
                case "3":
                    showAutoFarmMenu();
                    break;
                case "4":
                    showAutoMineMenu();
                    break;
                case "5":
                    showAutoFishMenu();
                    break;
                case "6":
                    showProfitCalculator();
                    break;
                case "7":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showSettingsMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("⚙️ SETTINGS");
            System.out.println("============");
            System.out.println("1. General Settings");
            System.out.println("2. Anti-Detection Settings");
            System.out.println("3. Display Settings");
            System.out.println("4. Performance Settings");
            System.out.println("5. Security Settings");
            System.out.println("6. Back to Main Menu");
            
            System.out.print("Settings> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showGeneralSettings();
                    break;
                case "2":
                    showAntiDetectionSettings();
                    break;
                case "3":
                    showDisplaySettings();
                    break;
                case "4":
                    showPerformanceSettings();
                    break;
                case "5":
                    showSecuritySettings();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showStatisticsMenu() {
        clearScreen();
        printHeader();
        System.out.println("📊 STATISTICS");
        System.out.println("=============");
        
        // Show module statistics
        int totalModules = client.getModuleManager().getModules().size();
        int enabledModules = client.getModuleManager().getEnabledModules().size();
        
        System.out.println("Modules: " + enabledModules + "/" + totalModules + " enabled");
        
        // Show specific module stats
        showModuleSpecificStats();
        
        pause();
    }
    
    private void showHelpMenu() {
        clearScreen();
        printHeader();
        System.out.println("❓ HELP & COMMANDS");
        System.out.println("==================");
        System.out.println("Main Menu Commands:");
        System.out.println("  1 - Module Management");
        System.out.println("  2 - AutoBuy System");
        System.out.println("  3 - Account Manager");
        System.out.println("  4 - Money Making");
        System.out.println("  5 - Settings");
        System.out.println("  6 - Statistics");
        System.out.println("  7 - Help");
        System.out.println("  8 - Quit");
        System.out.println();
        System.out.println("Quick Commands:");
        System.out.println("  gui - Open GUI");
        System.out.println("  list - List modules");
        System.out.println("  toggle <module> - Toggle module");
        System.out.println("  help - Show this help");
        System.out.println("  quit - Exit client");
        
        pause();
    }
    
    // Module management methods
    private void showAllModules() {
        clearScreen();
        printHeader();
        System.out.println("📋 ALL MODULES");
        System.out.println("==============");
        
        for (Module module : client.getModuleManager().getModules()) {
            String status = module.isEnabled() ? "§aON" : "§cOFF";
            String category = module.getCategory().getDisplayName();
            System.out.println(module.getName() + " - " + status + " §7(" + category + ")");
        }
        
        pause();
    }
    
    private void toggleModule() {
        System.out.print("Enter module name to toggle: ");
        String moduleName = scanner.nextLine().trim();
        
        Module module = client.getModuleManager().getModule(moduleName);
        if (module != null) {
            module.toggle();
            System.out.println("Module " + moduleName + " " + (module.isEnabled() ? "enabled" : "disabled"));
        } else {
            System.out.println("Module not found: " + moduleName);
        }
        
        pause();
    }
    
    private void showModuleSettings() {
        System.out.print("Enter module name for settings: ");
        String moduleName = scanner.nextLine().trim();
        
        Module module = client.getModuleManager().getModule(moduleName);
        if (module != null) {
            System.out.println("Settings for " + moduleName + ":");
            System.out.println("Description: " + module.getDescription());
            System.out.println("Category: " + module.getCategory().getDisplayName());
            System.out.println("Enabled: " + (module.isEnabled() ? "Yes" : "No"));
            System.out.println("Keybind: " + module.getKeyBind());
        } else {
            System.out.println("Module not found: " + moduleName);
        }
        
        pause();
    }
    
    private void enableAllModules() {
        for (Module module : client.getModuleManager().getModules()) {
            if (!module.isEnabled()) {
                module.enable();
            }
        }
        System.out.println("All modules enabled");
        pause();
    }
    
    private void disableAllModules() {
        for (Module module : client.getModuleManager().getModules()) {
            if (module.isEnabled()) {
                module.disable();
            }
        }
        System.out.println("All modules disabled");
        pause();
    }
    
    // AutoBuy methods
    private void showAutoBuySettings() {
        System.out.println("AutoBuy Settings - Opening AutoBuy GUI...");
        // This would open the AutoBuy settings GUI
        pause();
    }
    
    private void showItemManagement() {
        System.out.println("Item Management - Opening item management...");
        // This would open item management
        pause();
    }
    
    private void showPurchaseHistory() {
        System.out.println("Purchase History - Opening purchase history...");
        // This would open purchase history
        pause();
    }
    
    private void showAutoBuyStatistics() {
        System.out.println("AutoBuy Statistics - Opening statistics...");
        // This would show AutoBuy statistics
        pause();
    }
    
    private void showAutoBuyDemo() {
        System.out.println("AutoBuy Demo - Starting demo...");
        // This would start AutoBuy demo
        pause();
    }
    
    // Account Manager methods - now handled by AccountManager class
    
    // Money making methods
    private void showSwordCraftMenu() {
        System.out.println("SwordCraft Menu:");
        System.out.println("1. Start SwordCraft");
        System.out.println("2. Configure settings");
        System.out.println("3. View statistics");
        System.out.println("4. Stop SwordCraft");
        pause();
    }
    
    private void showChestStealerMenu() {
        System.out.println("ChestStealer Menu:");
        System.out.println("1. Configure containers");
        System.out.println("2. Set item filters");
        System.out.println("3. View statistics");
        System.out.println("4. Start stealing");
        pause();
    }
    
    private void showAutoFarmMenu() {
        System.out.println("AutoFarm Menu:");
        System.out.println("1. Configure crops");
        System.out.println("2. Set coordinates");
        System.out.println("3. Start farming");
        System.out.println("4. View statistics");
        pause();
    }
    
    private void showAutoMineMenu() {
        System.out.println("AutoMine Menu:");
        System.out.println("1. Set mining coordinates");
        System.out.println("2. Configure ores");
        System.out.println("3. Start mining");
        System.out.println("4. View statistics");
        pause();
    }
    
    private void showAutoFishMenu() {
        System.out.println("AutoFish Menu:");
        System.out.println("1. Configure fishing");
        System.out.println("2. Set enchantments");
        System.out.println("3. Start fishing");
        System.out.println("4. View statistics");
        pause();
    }
    
    private void showProfitCalculator() {
        System.out.println("Profit Calculator:");
        System.out.print("Enter sword quantity: ");
        String swords = scanner.nextLine().trim();
        System.out.print("Enter emerald price: ");
        String emeraldPrice = scanner.nextLine().trim();
        System.out.print("Enter sword sell price: ");
        String swordPrice = scanner.nextLine().trim();
        
        try {
            int swordCount = Integer.parseInt(swords);
            int emeraldCost = Integer.parseInt(emeraldPrice);
            int swordRevenue = Integer.parseInt(swordPrice);
            
            int emeraldsNeeded = swordCount * 2;
            int totalEmeraldCost = emeraldsNeeded * emeraldCost;
            int totalSwordRevenue = swordCount * swordRevenue;
            int profit = totalSwordRevenue - totalEmeraldCost;
            
            System.out.println("=== Profit Calculation ===");
            System.out.println("Swords to craft: " + swordCount);
            System.out.println("Emeralds needed: " + emeraldsNeeded);
            System.out.println("Emerald cost: " + totalEmeraldCost + " coins");
            System.out.println("Sword revenue: " + totalSwordRevenue + " coins");
            System.out.println("Profit: " + profit + " coins");
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format");
        }
        
        pause();
    }
    
    // Settings methods
    private void showGeneralSettings() {
        System.out.println("General Settings:");
        System.out.println("1. Client name");
        System.out.println("2. Auto-save settings");
        System.out.println("3. Log level");
        System.out.println("4. Language");
        pause();
    }
    
    private void showAntiDetectionSettings() {
        System.out.println("Anti-Detection Settings:");
        System.out.println("1. Funtime bypass");
        System.out.println("2. Matrix bypass");
        System.out.println("3. Randomization");
        System.out.println("4. Human-like behavior");
        pause();
    }
    
    private void showDisplaySettings() {
        System.out.println("Display Settings:");
        System.out.println("1. GUI theme");
        System.out.println("2. Font size");
        System.out.println("3. Colors");
        System.out.println("4. Animations");
        pause();
    }
    
    private void showPerformanceSettings() {
        System.out.println("Performance Settings:");
        System.out.println("1. Memory usage");
        System.out.println("2. CPU usage");
        System.out.println("3. Network optimization");
        System.out.println("4. Cache settings");
        pause();
    }
    
    private void showSecuritySettings() {
        System.out.println("Security Settings:");
        System.out.println("1. Account encryption");
        System.out.println("2. Password protection");
        System.out.println("3. Session management");
        System.out.println("4. Audit logging");
        pause();
    }
    
    private void showModuleSpecificStats() {
        // Show stats for specific modules
        System.out.println("Module Statistics:");
        System.out.println("AutoBuy: 0 purchases, 0 coins spent");
        System.out.println("SwordCraft: 0 swords crafted, 0 profit");
        System.out.println("ChestStealer: 0 items stolen, 0 chests opened");
        System.out.println("GPS: 0 waypoints, 0 navigation sessions");
    }
    
    private void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🎮 CHEATCLIENT v1.0.0                    ║");
        System.out.println("║              Advanced Minecraft Cheat Client                ║");
        System.out.println("║                   [Matrix & Funtime Bypass]                 ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printMainMenu() {
        System.out.println("📋 MAIN MENU");
        System.out.println("============");
        System.out.println("1. Module Management    🔧");
        System.out.println("2. AutoBuy System       🛒");
        System.out.println("3. Account Manager      👤");
        System.out.println("4. Money Making         💰");
        System.out.println("5. Settings             ⚙️");
        System.out.println("6. Statistics           📊");
        System.out.println("7. Help & Commands      ❓");
        System.out.println("8. Quit                 🚪");
        System.out.println();
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    private void pause() {
        System.out.println("\nPress Enter to continue...");
        scanner.nextLine();
    }
}