package com.example.nimbusware.features.autobuy;

import com.example.nimbusware.utils.Logger;

import java.util.List;
import java.util.Scanner;

public class AutoBuyGui {
    private AutoBuySettings settings;
    private Scanner scanner;
    private boolean isOpen;
    
    public AutoBuyGui(AutoBuySettings settings) {
        this.settings = settings;
        this.scanner = new Scanner(System.in);
        this.isOpen = false;
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
            
            System.out.print("AutoBuy> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                case "items":
                    showItemsMenu();
                    break;
                case "2":
                case "history":
                    showHistoryMenu();
                    break;
                case "3":
                case "settings":
                    showSettingsMenu();
                    break;
                case "4":
                case "stats":
                    showStatsMenu();
                    break;
                case "5":
                case "demo":
                    showDemoMenu();
                    break;
                case "6":
                case "back":
                case "exit":
                    close();
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showItemsMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("📦 ITEM MANAGEMENT");
            System.out.println("==================");
            System.out.println("1. View All Items");
            System.out.println("2. View by Category");
            System.out.println("3. View by Type");
            System.out.println("4. Add Custom Item");
            System.out.println("5. Edit Item");
            System.out.println("6. Toggle Item");
            System.out.println("7. Set Priority");
            System.out.println("8. Back to Main Menu");
            
            System.out.print("Items> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showAllItems();
                    break;
                case "2":
                    showItemsByCategory();
                    break;
                case "3":
                    showItemsByType();
                    break;
                case "4":
                    addCustomItem();
                    break;
                case "5":
                    editItem();
                    break;
                case "6":
                    toggleItem();
                    break;
                case "7":
                    setItemPriority();
                    break;
                case "8":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showHistoryMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("📊 PURCHASE HISTORY");
            System.out.println("===================");
            System.out.println("1. View Recent Purchases");
            System.out.println("2. View All Purchases");
            System.out.println("3. View by Item");
            System.out.println("4. View Statistics");
            System.out.println("5. Clear History");
            System.out.println("6. Back to Main Menu");
            
            System.out.print("History> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showRecentPurchases();
                    break;
                case "2":
                    showAllPurchases();
                    break;
                case "3":
                    showPurchasesByItem();
                    break;
                case "4":
                    showPurchaseStatistics();
                    break;
                case "5":
                    clearHistory();
                    break;
                case "6":
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
            System.out.println("⚙️ AUTOBUY SETTINGS");
            System.out.println("===================");
            System.out.println("1. General Settings");
            System.out.println("2. Price Settings");
            System.out.println("3. Notification Settings");
            System.out.println("4. Account Settings");
            System.out.println("5. Back to Main Menu");
            
            System.out.print("Settings> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    showGeneralSettings();
                    break;
                case "2":
                    showPriceSettings();
                    break;
                case "3":
                    showNotificationSettings();
                    break;
                case "4":
                    showAccountSettings();
                    break;
                case "5":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void showStatsMenu() {
        clearScreen();
        printHeader();
        System.out.println("📈 AUTOBUY STATISTICS");
        System.out.println("=====================");
        
        PurchaseHistory history = settings.getPurchaseHistory();
        System.out.println("Total Purchases: " + history.getTotalPurchases());
        System.out.println("Total Spent: " + history.getFormattedTotalSpent());
        System.out.println("Items Configured: " + settings.getTotalItems());
        System.out.println("Enabled Items: " + settings.getEnabledItemsCount());
        System.out.println("Max Price: " + settings.getFormattedMaxPrice());
        System.out.println("AutoBuy Enabled: " + (settings.isAutoBuyEnabled() ? "Yes" : "No"));
        System.out.println("Priority Mode: " + (settings.isPriorityMode() ? "Yes" : "No"));
        
        pause();
    }
    
    private void showAllItems() {
        clearScreen();
        printHeader();
        System.out.println("📦 ALL ITEMS");
        System.out.println("=============");
        
        List<AutoBuyItem> items = settings.getAllItems();
        if (items.isEmpty()) {
            System.out.println("No items configured.");
        } else {
            System.out.printf("%-20s %-15s %-10s %-12s %-8s %s%n", 
                "Name", "Category", "Type", "Price", "Enabled", "Enchantments");
            System.out.println("─".repeat(80));
            
            for (AutoBuyItem item : items) {
                System.out.printf("%-20s %-15s %-10s %-12s %-8s %s%n",
                    item.getDisplayName(),
                    item.getCategory(),
                    item.getItemType().getDisplayName(),
                    item.getFormattedPrice(),
                    item.isEnabled() ? "Yes" : "No",
                    item.getEnchantmentString()
                );
            }
        }
        
        pause();
    }
    
    private void showItemsByCategory() {
        System.out.print("Enter category (Weapon/Armor/Tool/Talisman/Consumable/Block/Misc): ");
        String category = scanner.nextLine().trim();
        
        clearScreen();
        printHeader();
        System.out.println("📦 ITEMS BY CATEGORY: " + category.toUpperCase());
        System.out.println("=".repeat(30 + category.length()));
        
        List<AutoBuyItem> items = settings.getItemsByCategory(category);
        if (items.isEmpty()) {
            System.out.println("No items found in category: " + category);
        } else {
            System.out.printf("%-20s %-10s %-12s %-8s %s%n", 
                "Name", "Type", "Price", "Enabled", "Enchantments");
            System.out.println("─".repeat(70));
            
            for (AutoBuyItem item : items) {
                System.out.printf("%-20s %-10s %-12s %-8s %s%n",
                    item.getDisplayName(),
                    item.getItemType().getDisplayName(),
                    item.getFormattedPrice(),
                    item.isEnabled() ? "Yes" : "No",
                    item.getEnchantmentString()
                );
            }
        }
        
        pause();
    }
    
    private void showItemsByType() {
        System.out.println("Available types:");
        for (AutoBuyItem.ItemType type : AutoBuyItem.ItemType.values()) {
            System.out.println("- " + type.getDisplayName() + " " + type.getEmoji());
        }
        
        System.out.print("Enter type: ");
        String typeInput = scanner.nextLine().trim();
        
        AutoBuyItem.ItemType type = null;
        for (AutoBuyItem.ItemType t : AutoBuyItem.ItemType.values()) {
            if (t.getDisplayName().equalsIgnoreCase(typeInput)) {
                type = t;
                break;
            }
        }
        
        if (type == null) {
            System.out.println("Invalid type.");
            pause();
            return;
        }
        
        clearScreen();
        printHeader();
        System.out.println("📦 ITEMS BY TYPE: " + type.getDisplayName() + " " + type.getEmoji());
        System.out.println("=".repeat(25 + type.getDisplayName().length()));
        
        List<AutoBuyItem> items = settings.getItemsByType(type);
        if (items.isEmpty()) {
            System.out.println("No items found of type: " + type.getDisplayName());
        } else {
            System.out.printf("%-20s %-15s %-12s %-8s %s%n", 
                "Name", "Category", "Price", "Enabled", "Enchantments");
            System.out.println("─".repeat(75));
            
            for (AutoBuyItem item : items) {
                System.out.printf("%-20s %-15s %-12s %-8s %s%n",
                    item.getDisplayName(),
                    item.getCategory(),
                    item.getFormattedPrice(),
                    item.isEnabled() ? "Yes" : "No",
                    item.getEnchantmentString()
                );
            }
        }
        
        pause();
    }
    
    private void addCustomItem() {
        System.out.println("Adding Custom Item");
        System.out.println("==================");
        
        System.out.print("Item name (ID): ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Display name: ");
        String displayName = scanner.nextLine().trim();
        
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Price: ");
        int price = Integer.parseInt(scanner.nextLine().trim());
        
        System.out.println("Item type:");
        for (int i = 0; i < AutoBuyItem.ItemType.values().length; i++) {
            AutoBuyItem.ItemType type = AutoBuyItem.ItemType.values()[i];
            System.out.println((i + 1) + ". " + type.getDisplayName() + " " + type.getEmoji());
        }
        
        System.out.print("Select type (1-" + AutoBuyItem.ItemType.values().length + "): ");
        int typeIndex = Integer.parseInt(scanner.nextLine().trim()) - 1;
        AutoBuyItem.ItemType type = AutoBuyItem.ItemType.values()[typeIndex];
        
        AutoBuyItem item = new AutoBuyItem(name, displayName, category, price, type);
        settings.addItem(item);
        
        System.out.println("Item added successfully!");
        pause();
    }
    
    private void editItem() {
        System.out.print("Enter item name to edit: ");
        String name = scanner.nextLine().trim();
        
        AutoBuyItem item = settings.getItem(name);
        if (item == null) {
            System.out.println("Item not found.");
            pause();
            return;
        }
        
        System.out.println("Editing: " + item.getDisplayName());
        System.out.println("1. Change price");
        System.out.println("2. Toggle enabled");
        System.out.println("3. Set priority");
        System.out.println("4. Add enchantment");
        System.out.println("5. Remove enchantment");
        
        System.out.print("Select option: ");
        String option = scanner.nextLine().trim();
        
        switch (option) {
            case "1":
                System.out.print("New price: ");
                int newPrice = Integer.parseInt(scanner.nextLine().trim());
                item.setPrice(newPrice);
                System.out.println("Price updated!");
                break;
            case "2":
                item.setEnabled(!item.isEnabled());
                System.out.println("Item " + (item.isEnabled() ? "enabled" : "disabled") + "!");
                break;
            case "3":
                System.out.print("Priority (higher = more important): ");
                int priority = Integer.parseInt(scanner.nextLine().trim());
                item.setPriority(priority);
                System.out.println("Priority updated!");
                break;
            case "4":
                System.out.print("Enchantment name: ");
                String enchantName = scanner.nextLine().trim();
                System.out.print("Level: ");
                int level = Integer.parseInt(scanner.nextLine().trim());
                System.out.print("Description: ");
                String description = scanner.nextLine().trim();
                
                AutoBuyItem.Enchantment enchant = new AutoBuyItem.Enchantment(enchantName, level, description);
                item.addEnchantment(enchant);
                System.out.println("Enchantment added!");
                break;
            case "5":
                System.out.println("Current enchantments:");
                for (int i = 0; i < item.getEnchantments().size(); i++) {
                    System.out.println((i + 1) + ". " + item.getEnchantments().get(i));
                }
                System.out.print("Select enchantment to remove (1-" + item.getEnchantments().size() + "): ");
                int index = Integer.parseInt(scanner.nextLine().trim()) - 1;
                if (index >= 0 && index < item.getEnchantments().size()) {
                    item.removeEnchantment(item.getEnchantments().get(index));
                    System.out.println("Enchantment removed!");
                }
                break;
        }
        
        pause();
    }
    
    private void toggleItem() {
        System.out.print("Enter item name to toggle: ");
        String name = scanner.nextLine().trim();
        
        AutoBuyItem item = settings.getItem(name);
        if (item == null) {
            System.out.println("Item not found.");
        } else {
            item.setEnabled(!item.isEnabled());
            System.out.println("Item " + (item.isEnabled() ? "enabled" : "disabled") + "!");
        }
        
        pause();
    }
    
    private void setItemPriority() {
        System.out.print("Enter item name: ");
        String name = scanner.nextLine().trim();
        
        AutoBuyItem item = settings.getItem(name);
        if (item == null) {
            System.out.println("Item not found.");
        } else {
            System.out.print("Priority (higher = more important): ");
            int priority = Integer.parseInt(scanner.nextLine().trim());
            item.setPriority(priority);
            System.out.println("Priority updated!");
        }
        
        pause();
    }
    
    private void showRecentPurchases() {
        clearScreen();
        printHeader();
        System.out.println("📊 RECENT PURCHASES");
        System.out.println("===================");
        
        List<PurchaseHistory.PurchaseRecord> recent = settings.getPurchaseHistory().getRecentPurchases(10);
        if (recent.isEmpty()) {
            System.out.println("No purchases yet.");
        } else {
            System.out.printf("%-20s %-10s %-12s %-15s %-20s%n", 
                "Item", "Quantity", "Price", "Account", "Time");
            System.out.println("─".repeat(85));
            
            for (PurchaseHistory.PurchaseRecord record : recent) {
                System.out.printf("%-20s %-10d %-12s %-15s %-20s%n",
                    record.getItemDisplay(),
                    record.getQuantity(),
                    record.getFormattedPrice(),
                    record.getAccount(),
                    record.getFormattedTimestamp()
                );
            }
        }
        
        pause();
    }
    
    private void showAllPurchases() {
        clearScreen();
        printHeader();
        System.out.println("📊 ALL PURCHASES");
        System.out.println("================");
        
        List<PurchaseHistory.PurchaseRecord> all = settings.getPurchaseHistory().getAllPurchases();
        if (all.isEmpty()) {
            System.out.println("No purchases yet.");
        } else {
            System.out.printf("%-20s %-10s %-12s %-15s %-20s%n", 
                "Item", "Quantity", "Price", "Account", "Time");
            System.out.println("─".repeat(85));
            
            for (PurchaseHistory.PurchaseRecord record : all) {
                System.out.printf("%-20s %-10d %-12s %-15s %-20s%n",
                    record.getItemDisplay(),
                    record.getQuantity(),
                    record.getFormattedPrice(),
                    record.getAccount(),
                    record.getFormattedTimestamp()
                );
            }
        }
        
        pause();
    }
    
    private void showPurchasesByItem() {
        System.out.print("Enter item name: ");
        String itemName = scanner.nextLine().trim();
        
        clearScreen();
        printHeader();
        System.out.println("📊 PURCHASES FOR: " + itemName.toUpperCase());
        System.out.println("=".repeat(25 + itemName.length()));
        
        List<PurchaseHistory.PurchaseRecord> all = settings.getPurchaseHistory().getAllPurchases();
        List<PurchaseHistory.PurchaseRecord> filtered = all.stream()
            .filter(record -> record.getItem().getName().equalsIgnoreCase(itemName))
            .collect(java.util.stream.Collectors.toList());
        
        if (filtered.isEmpty()) {
            System.out.println("No purchases found for item: " + itemName);
        } else {
            System.out.printf("%-10s %-12s %-15s %-20s%n", 
                "Quantity", "Price", "Account", "Time");
            System.out.println("─".repeat(65));
            
            for (PurchaseHistory.PurchaseRecord record : filtered) {
                System.out.printf("%-10d %-12s %-15s %-20s%n",
                    record.getQuantity(),
                    record.getFormattedPrice(),
                    record.getAccount(),
                    record.getFormattedTimestamp()
                );
            }
        }
        
        pause();
    }
    
    private void showPurchaseStatistics() {
        clearScreen();
        printHeader();
        System.out.println("📈 PURCHASE STATISTICS");
        System.out.println("======================");
        
        PurchaseHistory history = settings.getPurchaseHistory();
        System.out.println("Total Purchases: " + history.getTotalPurchases());
        System.out.println("Total Spent: " + history.getFormattedTotalSpent());
        
        // Calculate average purchase price
        if (history.getTotalPurchases() > 0) {
            double avgPrice = (double) history.getTotalSpent() / history.getTotalPurchases();
            System.out.println("Average Price: " + String.format("%,.0f", avgPrice) + " coins");
        }
        
        pause();
    }
    
    private void clearHistory() {
        System.out.print("Are you sure you want to clear all purchase history? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            settings.getPurchaseHistory().clearHistory();
            System.out.println("Purchase history cleared!");
        } else {
            System.out.println("Operation cancelled.");
        }
        
        pause();
    }
    
    private void showGeneralSettings() {
        clearScreen();
        printHeader();
        System.out.println("⚙️ GENERAL SETTINGS");
        System.out.println("===================");
        
        System.out.println("1. AutoBuy Enabled: " + (settings.isAutoBuyEnabled() ? "Yes" : "No"));
        System.out.println("2. Priority Mode: " + (settings.isPriorityMode() ? "Yes" : "No"));
        System.out.println("3. Check Interval: " + settings.getCheckInterval() + "ms");
        System.out.println("4. Max Price: " + settings.getFormattedMaxPrice());
        
        System.out.print("Select setting to change (1-4): ");
        String option = scanner.nextLine().trim();
        
        switch (option) {
            case "1":
                settings.setAutoBuyEnabled(!settings.isAutoBuyEnabled());
                System.out.println("AutoBuy " + (settings.isAutoBuyEnabled() ? "enabled" : "disabled") + "!");
                break;
            case "2":
                settings.setPriorityMode(!settings.isPriorityMode());
                System.out.println("Priority mode " + (settings.isPriorityMode() ? "enabled" : "disabled") + "!");
                break;
            case "3":
                System.out.print("New check interval (ms): ");
                int interval = Integer.parseInt(scanner.nextLine().trim());
                settings.setCheckInterval(interval);
                System.out.println("Check interval updated!");
                break;
            case "4":
                System.out.print("New max price: ");
                int maxPrice = Integer.parseInt(scanner.nextLine().trim());
                settings.setMaxPrice(maxPrice);
                System.out.println("Max price updated!");
                break;
        }
        
        pause();
    }
    
    private void showPriceSettings() {
        clearScreen();
        printHeader();
        System.out.println("💰 PRICE SETTINGS");
        System.out.println("=================");
        
        System.out.println("Current max price: " + settings.getFormattedMaxPrice());
        System.out.println("Affordable items: " + settings.getAffordableItems(settings.getMaxPrice()).size());
        
        System.out.print("Set new max price: ");
        int newPrice = Integer.parseInt(scanner.nextLine().trim());
        settings.setMaxPrice(newPrice);
        
        System.out.println("Max price updated to: " + settings.getFormattedMaxPrice());
        pause();
    }
    
    private void showNotificationSettings() {
        clearScreen();
        printHeader();
        System.out.println("🔔 NOTIFICATION SETTINGS");
        System.out.println("========================");
        
        System.out.println("1. Notifications Enabled: " + (settings.isNotificationsEnabled() ? "Yes" : "No"));
        System.out.println("2. Notification Sound: " + settings.getNotificationSound());
        
        System.out.print("Select setting to change (1-2): ");
        String option = scanner.nextLine().trim();
        
        switch (option) {
            case "1":
                settings.setNotificationsEnabled(!settings.isNotificationsEnabled());
                System.out.println("Notifications " + (settings.isNotificationsEnabled() ? "enabled" : "disabled") + "!");
                break;
            case "2":
                System.out.print("New notification sound: ");
                String sound = scanner.nextLine().trim();
                settings.setNotificationSound(sound);
                System.out.println("Notification sound updated!");
                break;
        }
        
        pause();
    }
    
    private void showAccountSettings() {
        clearScreen();
        printHeader();
        System.out.println("👤 ACCOUNT SETTINGS");
        System.out.println("===================");
        
        System.out.println("Account management for AutoBuy system");
        System.out.println("(This would integrate with the main AutoBuy account system)");
        
        pause();
    }
    
    private void showDemoMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("🎮 DEMO & SIMULATION");
            System.out.println("====================");
            System.out.println("1. Run Market Simulation");
            System.out.println("2. Show Market Status");
            System.out.println("3. Simulate Purchase");
            System.out.println("4. Simulate Price Changes");
            System.out.println("5. Add Random Items");
            System.out.println("6. Back to Main Menu");
            
            System.out.print("Demo> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    runMarketSimulation();
                    break;
                case "2":
                    showMarketStatus();
                    break;
                case "3":
                    simulatePurchase();
                    break;
                case "4":
                    simulatePriceChanges();
                    break;
                case "5":
                    addRandomItems();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void runMarketSimulation() {
        clearScreen();
        printHeader();
        System.out.println("🎮 RUNNING MARKET SIMULATION");
        System.out.println("=============================");
        
        // This would call the demo simulation
        System.out.println("Simulating market activity...");
        System.out.println("(In a real implementation, this would run the AutoBuyDemo)");
        
        pause();
    }
    
    private void showMarketStatus() {
        clearScreen();
        printHeader();
        System.out.println("📊 MARKET STATUS");
        System.out.println("================");
        
        System.out.println("Total Items: " + settings.getTotalItems());
        System.out.println("Enabled Items: " + settings.getEnabledItemsCount());
        System.out.println("Total Purchases: " + settings.getPurchaseHistory().getTotalPurchases());
        System.out.println("Total Spent: " + settings.getPurchaseHistory().getFormattedTotalSpent());
        System.out.println("Max Price: " + settings.getFormattedMaxPrice());
        
        pause();
    }
    
    private void simulatePurchase() {
        System.out.print("Enter item name to purchase: ");
        String itemName = scanner.nextLine().trim();
        
        AutoBuyItem item = settings.getItem(itemName);
        if (item == null) {
            System.out.println("Item not found.");
        } else {
            System.out.print("Quantity: ");
            int quantity = Integer.parseInt(scanner.nextLine().trim());
            
            settings.getPurchaseHistory().addPurchase(item, quantity, "DemoUser");
            System.out.println("Simulated purchase: " + quantity + "x " + item.getDisplayName() + 
                             " for " + (item.getPrice() * quantity) + " coins");
        }
        
        pause();
    }
    
    private void simulatePriceChanges() {
        System.out.println("Simulating price changes...");
        
        // Randomly change prices of some items
        List<AutoBuyItem> items = settings.getAllItems();
        int changeCount = Math.min(3, items.size());
        
        for (int i = 0; i < changeCount; i++) {
            AutoBuyItem item = items.get(i);
            int oldPrice = item.getPrice();
            int newPrice = (int) (oldPrice * (0.8 + Math.random() * 0.4)); // ±20% change
            item.setPrice(newPrice);
            
            String change = newPrice > oldPrice ? "increased" : "decreased";
            System.out.println("Price " + change + " for " + item.getDisplayName() + 
                             ": " + oldPrice + " → " + newPrice + " coins");
        }
        
        pause();
    }
    
    private void addRandomItems() {
        System.out.println("Adding random items to market...");
        
        String[] names = {"mystic_sword", "enchanted_bow", "legendary_armor", "magic_talisman"};
        String[] displays = {"Mystic Sword", "Enchanted Bow", "Legendary Armor", "Magic Talisman"};
        String[] categories = {"Weapon", "Weapon", "Armor", "Talisman"};
        
        for (int i = 0; i < names.length; i++) {
            AutoBuyItem item = new AutoBuyItem(
                names[i],
                displays[i],
                categories[i],
                (int) (Math.random() * 50000) + 10000,
                AutoBuyItem.ItemType.WEAPON
            );
            
            settings.addItem(item);
            System.out.println("Added: " + item.getDisplayName() + " for " + item.getFormattedPrice());
        }
        
        pause();
    }
    
    private void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    🛒 AUTOBUY SYSTEM                        ║");
        System.out.println("║                   Funtime Server Edition                    ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printMainMenu() {
        System.out.println("📋 MAIN MENU");
        System.out.println("============");
        System.out.println("1. Item Management    📦");
        System.out.println("2. Purchase History   📊");
        System.out.println("3. Settings          ⚙️");
        System.out.println("4. Statistics        📈");
        System.out.println("5. Demo              🎮");
        System.out.println("6. Back to Main      ↩️");
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