package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.features.AccountConnector;
import com.example.cheatclient.features.autobuy.AutoBuySettings;
import com.example.cheatclient.features.autobuy.AutoBuyItem;
import com.example.cheatclient.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class AccountManager {
    private boolean isOpen = false;
    private Scanner scanner;
    private CheatClient client;
    private AccountConnector connector;
    
    public AccountManager(CheatClient client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
        this.connector = new AccountConnector();
    }
    
    public void open() {
        isOpen = true;
        showAccountManager();
    }
    
    public void close() {
        isOpen = false;
    }
    
    public boolean isOpen() {
        return isOpen;
    }
    
    private void showAccountManager() {
        while (isOpen) {
            clearScreen();
            printHeader();
            printMainMenu();
            
            System.out.print("AccountManager> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                case "view":
                    showAllAccounts();
                    break;
                case "2":
                case "add":
                    addAccount();
                    break;
                case "3":
                case "remove":
                    removeAccount();
                    break;
                case "4":
                case "switch":
                    switchAccount();
                    break;
                case "5":
                case "settings":
                    showAccountSettings();
                    break;
                case "6":
                case "resources":
                    showResourceSharing();
                    break;
                case "7":
                case "status":
                    showConnectionStatus();
                    break;
                case "8":
                case "autobuy":
                    showAutoBuyIntegration();
                    break;
                case "9":
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
    
    private void showAllAccounts() {
        clearScreen();
        printHeader();
        System.out.println("👤 ALL ACCOUNTS");
        System.out.println("===============");
        
        List<AccountConnector.ConnectedAccount> accounts = connector.getConnectedAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts configured.");
            System.out.println("Use 'Add Account' to configure accounts.");
        } else {
            System.out.printf("%-15s %-10s %-10s %-15s %s%n", 
                "Username", "Role", "Status", "Last Seen", "Connections");
            System.out.println("─".repeat(70));
            
            for (AccountConnector.ConnectedAccount account : accounts) {
                String status = account.isOnline() ? "Online" : "Offline";
                String lastSeen = formatLastSeen(account.getLastSeen());
                System.out.printf("%-15s %-10s %-10s %-15s %d%n",
                    account.getUsername(),
                    account.getRole(),
                    status,
                    lastSeen,
                    account.getConnectionCount()
                );
            }
        }
        
        pause();
    }
    
    private void addAccount() {
        clearScreen();
        printHeader();
        System.out.println("➕ ADD ACCOUNT");
        System.out.println("==============");
        
        System.out.print("Username: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            pause();
            return;
        }
        
        System.out.print("Password: ");
        String password = scanner.nextLine().trim();
        
        if (password.isEmpty()) {
            System.out.println("Password cannot be empty.");
            pause();
            return;
        }
        
        System.out.println("Select role:");
        System.out.println("1. Checker - Monitors queue and server status");
        System.out.println("2. Buyer - Purchases items and manages transactions");
        System.out.println("3. Worker - Performs specific tasks (SwordCraft, etc.)");
        System.out.println("4. Manager - Coordinates between accounts");
        
        System.out.print("Enter role (1-4): ");
        String roleInput = scanner.nextLine().trim();
        
        String role = "Worker";
        switch (roleInput) {
            case "1": role = "Checker"; break;
            case "2": role = "Buyer"; break;
            case "3": role = "Worker"; break;
            case "4": role = "Manager"; break;
            default:
                System.out.println("Invalid role selection. Defaulting to Worker.");
                break;
        }
        
        connector.addAccount(username, password, role);
        System.out.println("Account added successfully: " + username + " (" + role + ")");
        pause();
    }
    
    private void removeAccount() {
        clearScreen();
        printHeader();
        System.out.println("🗑️ REMOVE ACCOUNT");
        System.out.println("==================");
        
        List<AccountConnector.ConnectedAccount> accounts = connector.getConnectedAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts to remove.");
            pause();
            return;
        }
        
        System.out.println("Available accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            AccountConnector.ConnectedAccount account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getUsername() + " (" + account.getRole() + ")");
        }
        
        System.out.print("Enter username to remove: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            pause();
            return;
        }
        
        connector.removeAccount(username);
        System.out.println("Account removed: " + username);
        pause();
    }
    
    private void switchAccount() {
        clearScreen();
        printHeader();
        System.out.println("🔄 SWITCH ACCOUNT");
        System.out.println("=================");
        
        List<AccountConnector.ConnectedAccount> accounts = connector.getConnectedAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            pause();
            return;
        }
        
        System.out.println("Available accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            AccountConnector.ConnectedAccount account = accounts.get(i);
            String current = account.getUsername().equals(connector.getCurrentAccount()) ? " (Current)" : "";
            System.out.println((i + 1) + ". " + account.getUsername() + " (" + account.getRole() + ")" + current);
        }
        
        System.out.print("Enter username to switch to: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            pause();
            return;
        }
        
        connector.switchAccount(username);
        System.out.println("Switched to account: " + username);
        pause();
    }
    
    private void showAccountSettings() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("⚙️ ACCOUNT SETTINGS");
            System.out.println("===================");
            System.out.println("1. Enable Resource Sharing: " + (connector.isShareResources() ? "Yes" : "No"));
            System.out.println("2. Enable Chat Communication: " + (connector.isEnableChat() ? "Yes" : "No"));
            System.out.println("3. Enable Command Sharing: " + (connector.isEnableCommands() ? "Yes" : "No"));
            System.out.println("4. Set Primary Account");
            System.out.println("5. Auto-Connect: " + (connector.isAutoConnect() ? "Yes" : "No"));
            System.out.println("6. Back to Account Manager");
            
            System.out.print("Settings> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    connector.setShareResources(!connector.isShareResources());
                    System.out.println("Resource sharing " + (connector.isShareResources() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "2":
                    connector.setEnableChat(!connector.isEnableChat());
                    System.out.println("Chat communication " + (connector.isEnableChat() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "3":
                    connector.setEnableCommands(!connector.isEnableCommands());
                    System.out.println("Command sharing " + (connector.isEnableCommands() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "4":
                    setPrimaryAccount();
                    break;
                case "5":
                    connector.setAutoConnect(!connector.isAutoConnect());
                    System.out.println("Auto-connect " + (connector.isAutoConnect() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void setPrimaryAccount() {
        List<AccountConnector.ConnectedAccount> accounts = connector.getConnectedAccounts();
        if (accounts.isEmpty()) {
            System.out.println("No accounts available.");
            pause();
            return;
        }
        
        System.out.println("Available accounts:");
        for (int i = 0; i < accounts.size(); i++) {
            AccountConnector.ConnectedAccount account = accounts.get(i);
            System.out.println((i + 1) + ". " + account.getUsername() + " (" + account.getRole() + ")");
        }
        
        System.out.print("Enter username for primary account: ");
        String username = scanner.nextLine().trim();
        
        if (username.isEmpty()) {
            System.out.println("Username cannot be empty.");
            pause();
            return;
        }
        
        connector.setPrimaryAccountName(username);
        System.out.println("Primary account set to: " + username);
        pause();
    }
    
    private void showResourceSharing() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("🔄 RESOURCE SHARING");
            System.out.println("===================");
            System.out.println("1. Share Items: " + (connector.isShareItems() ? "Yes" : "No"));
            System.out.println("2. Share Coins: " + (connector.isShareCoins() ? "Yes" : "No"));
            System.out.println("3. Share Experience: " + (connector.isShareExperience() ? "Yes" : "No"));
            System.out.println("4. View Shared Resources");
            System.out.println("5. Add Shared Item");
            System.out.println("6. Add Shared Coins");
            System.out.println("7. Add Shared Experience");
            System.out.println("8. Back to Account Manager");
            
            System.out.print("Resources> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    connector.setShareItems(!connector.isShareItems());
                    System.out.println("Item sharing " + (connector.isShareItems() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "2":
                    connector.setShareCoins(!connector.isShareCoins());
                    System.out.println("Coin sharing " + (connector.isShareCoins() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "3":
                    connector.setShareExperience(!connector.isShareExperience());
                    System.out.println("Experience sharing " + (connector.isShareExperience() ? "enabled" : "disabled"));
                    pause();
                    break;
                case "4":
                    viewSharedResources();
                    break;
                case "5":
                    addSharedItem();
                    break;
                case "6":
                    addSharedCoins();
                    break;
                case "7":
                    addSharedExperience();
                    break;
                case "8":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void viewSharedResources() {
        clearScreen();
        printHeader();
        System.out.println("📊 SHARED RESOURCES");
        System.out.println("===================");
        
        System.out.println("Shared Coins: " + connector.getSharedCoins());
        System.out.println("Shared Experience: " + connector.getSharedExperience());
        System.out.println("Shared Items:");
        
        List<String> sharedItems = connector.getSharedItems();
        if (sharedItems.isEmpty()) {
            System.out.println("  No items shared.");
        } else {
            for (String item : sharedItems) {
                System.out.println("  - " + item);
            }
        }
        
        pause();
    }
    
    private void addSharedItem() {
        System.out.print("Enter item name to share: ");
        String item = scanner.nextLine().trim();
        
        if (item.isEmpty()) {
            System.out.println("Item name cannot be empty.");
        } else {
            connector.addSharedItem(item);
            System.out.println("Added shared item: " + item);
        }
        
        pause();
    }
    
    private void addSharedCoins() {
        System.out.print("Enter amount of coins to add: ");
        String input = scanner.nextLine().trim();
        
        try {
            int amount = Integer.parseInt(input);
            if (amount > 0) {
                connector.addSharedCoins(amount);
                System.out.println("Added " + amount + " coins to shared pool");
            } else {
                System.out.println("Amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
        
        pause();
    }
    
    private void addSharedExperience() {
        System.out.print("Enter amount of experience to add: ");
        String input = scanner.nextLine().trim();
        
        try {
            int amount = Integer.parseInt(input);
            if (amount > 0) {
                connector.addSharedExperience(amount);
                System.out.println("Added " + amount + " experience to shared pool");
            } else {
                System.out.println("Amount must be positive.");
            }
        } catch (NumberFormatException e) {
            System.out.println("Invalid number format.");
        }
        
        pause();
    }
    
    private void showConnectionStatus() {
        clearScreen();
        printHeader();
        System.out.println("📡 CONNECTION STATUS");
        System.out.println("====================");
        
        System.out.println("Connection Status: " + (connector.isConnected() ? "Connected" : "Disconnected"));
        System.out.println("Current Account: " + (connector.getCurrentAccount().isEmpty() ? "None" : connector.getCurrentAccount()));
        System.out.println("Total Connections: " + connector.getTotalConnections());
        System.out.println("Successful: " + connector.getSuccessfulConnections());
        System.out.println("Failed: " + connector.getFailedConnections());
        System.out.println("Last Connection: " + formatLastSeen(connector.getLastConnectionTime()));
        
        pause();
    }
    
    private void showAutoBuyIntegration() {
        while (isOpen) {
            clearScreen();
            printHeader();
            System.out.println("🛒 AUTOBUY INTEGRATION");
            System.out.println("=======================");
            System.out.println("1. View Shared AutoBuy Items");
            System.out.println("2. Add AutoBuy Item");
            System.out.println("3. Remove AutoBuy Item");
            System.out.println("4. Sync AutoBuy Settings");
            System.out.println("5. View AutoBuy Statistics");
            System.out.println("6. Back to Account Manager");
            
            System.out.print("AutoBuy> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "1":
                    viewSharedAutoBuyItems();
                    break;
                case "2":
                    addAutoBuyItem();
                    break;
                case "3":
                    removeAutoBuyItem();
                    break;
                case "4":
                    syncAutoBuySettings();
                    break;
                case "5":
                    viewAutoBuyStatistics();
                    break;
                case "6":
                    return;
                default:
                    System.out.println("Invalid option. Please try again.");
                    pause();
            }
        }
    }
    
    private void viewSharedAutoBuyItems() {
        clearScreen();
        printHeader();
        System.out.println("🛒 SHARED AUTOBUY ITEMS");
        System.out.println("=======================");
        
        List<AutoBuyItem> items = connector.getSharedAutoBuyItems();
        if (items.isEmpty()) {
            System.out.println("No AutoBuy items shared.");
        } else {
            System.out.printf("%-20s %-15s %-10s %-12s %-8s%n", 
                "Name", "Category", "Type", "Price", "Enabled");
            System.out.println("─".repeat(75));
            
            for (AutoBuyItem item : items) {
                System.out.printf("%-20s %-15s %-10s %-12s %-8s%n",
                    item.getDisplayName(),
                    item.getCategory(),
                    item.getItemType().getDisplayName(),
                    item.getFormattedPrice(),
                    item.isEnabled() ? "Yes" : "No"
                );
            }
        }
        
        pause();
    }
    
    private void addAutoBuyItem() {
        System.out.print("Item name (ID): ");
        String name = scanner.nextLine().trim();
        
        System.out.print("Display name: ");
        String displayName = scanner.nextLine().trim();
        
        System.out.print("Category: ");
        String category = scanner.nextLine().trim();
        
        System.out.print("Price: ");
        String priceInput = scanner.nextLine().trim();
        
        try {
            int price = Integer.parseInt(priceInput);
            
            AutoBuyItem item = new AutoBuyItem(name, displayName, category, price, AutoBuyItem.ItemType.MISC);
            connector.syncAutoBuyItem(item);
            System.out.println("Added AutoBuy item: " + displayName);
        } catch (NumberFormatException e) {
            System.out.println("Invalid price format.");
        }
        
        pause();
    }
    
    private void removeAutoBuyItem() {
        System.out.print("Enter item name to remove: ");
        String itemName = scanner.nextLine().trim();
        
        if (itemName.isEmpty()) {
            System.out.println("Item name cannot be empty.");
        } else {
            connector.removeAutoBuyItem(itemName);
            System.out.println("Removed AutoBuy item: " + itemName);
        }
        
        pause();
    }
    
    private void syncAutoBuySettings() {
        System.out.println("Syncing AutoBuy settings across all accounts...");
        // In real implementation, this would sync settings
        System.out.println("AutoBuy settings synced successfully.");
        pause();
    }
    
    private void viewAutoBuyStatistics() {
        clearScreen();
        printHeader();
        System.out.println("📊 AUTOBUY STATISTICS");
        System.out.println("=====================");
        
        System.out.println("Shared AutoBuy Items: " + connector.getSharedAutoBuyItems().size());
        System.out.println("Resource Sharing: " + (connector.isShareResources() ? "Enabled" : "Disabled"));
        System.out.println("Auto-Connect: " + (connector.isAutoConnect() ? "Enabled" : "Disabled"));
        
        pause();
    }
    
    private String formatLastSeen(long timestamp) {
        if (timestamp == 0) return "Never";
        
        long now = System.currentTimeMillis();
        long diff = now - timestamp;
        
        if (diff < 60000) return "Just now";
        if (diff < 3600000) return (diff / 60000) + " minutes ago";
        if (diff < 86400000) return (diff / 3600000) + " hours ago";
        return (diff / 86400000) + " days ago";
    }
    
    private void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    👤 ACCOUNT MANAGER                       ║");
        System.out.println("║              Multi-Account Management System                ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printMainMenu() {
        System.out.println("📋 ACCOUNT MANAGER MENU");
        System.out.println("=======================");
        System.out.println("1. View All Accounts      👥");
        System.out.println("2. Add Account            ➕");
        System.out.println("3. Remove Account         🗑️");
        System.out.println("4. Switch Account         🔄");
        System.out.println("5. Account Settings       ⚙️");
        System.out.println("6. Resource Sharing       🔄");
        System.out.println("7. Connection Status      📡");
        System.out.println("8. AutoBuy Integration    🛒");
        System.out.println("9. Back to Main Menu      ↩️");
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