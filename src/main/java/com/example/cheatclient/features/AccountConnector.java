package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import com.example.cheatclient.anti_detection.AntiDetectionManager;
import com.example.cheatclient.utils.Logger;
import com.example.cheatclient.features.autobuy.AutoBuySettings;
import com.example.cheatclient.features.autobuy.AutoBuyItem;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;

public class AccountConnector extends Module {
    // Account connection settings
    private boolean connectorEnabled = true;
    private boolean autoConnect = true;
    private boolean shareResources = true;
    private boolean shareWaypoints = true;
    private boolean shareSettings = true;
    
    // Account management
    private List<ConnectedAccount> connectedAccounts = new ArrayList<>();
    private String primaryAccount = "";
    private String currentAccount = "";
    
    // Resource sharing
    private boolean shareItems = true;
    private boolean shareCoins = true;
    private boolean shareExperience = true;
    private List<String> sharedItems = new ArrayList<>();
    private int sharedCoins = 0;
    private int sharedExperience = 0;
    
    // Communication settings
    private boolean enableChat = true;
    private boolean enableCommands = true;
    private String chatPrefix = "[AccountConnector]";
    private List<String> commandQueue = new ArrayList<>();
    
    // Anti-detection settings
    private boolean useFuntimeBypass = true;
    private boolean useMatrixBypass = true;
    private boolean randomizeConnections = true;
    private boolean varyTiming = true;
    
    // Status tracking
    private boolean isConnected = false;
    private int totalConnections = 0;
    private int successfulConnections = 0;
    private int failedConnections = 0;
    private long lastConnectionTime = 0;
    
    // AutoBuy integration
    private AutoBuySettings sharedAutoBuySettings;
    
    private Random random = new Random();
    
    public AccountConnector() {
        super("AccountConnector", "Connect and manage multiple AutoBuy accounts", Module.Category.MISC, 0);
        
        // Initialize shared AutoBuy settings
        this.sharedAutoBuySettings = new AutoBuySettings();
        initializeDefaultSettings();
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AccountConnector");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("AccountConnector");
        }
        
        Logger.info("AccountConnector enabled - Ready to manage multiple accounts");
        Logger.info("Connected accounts: " + connectedAccounts.size());
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AccountConnector");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("AccountConnector");
        }
        
        Logger.info("AccountConnector disabled - Session stats:");
        Logger.info("Total connections: " + totalConnections);
        Logger.info("Successful: " + successfulConnections);
        Logger.info("Failed: " + failedConnections);
    }
    
    public void onTick() {
        if (!isEnabled() || !connectorEnabled) {
            return;
        }
        
        if (autoConnect && !isConnected) {
            attemptConnection();
        }
        
        if (isConnected) {
            processCommandQueue();
            syncResources();
        }
    }
    
    private void attemptConnection() {
        if (connectedAccounts.isEmpty()) {
            Logger.warn("No accounts configured for connection");
            return;
        }
        
        Logger.info("Attempting to connect to accounts...");
        
        for (ConnectedAccount account : connectedAccounts) {
            if (connectToAccount(account)) {
                successfulConnections++;
                isConnected = true;
                currentAccount = account.getUsername();
                lastConnectionTime = System.currentTimeMillis();
                Logger.info("Successfully connected to: " + account.getUsername());
                break;
            } else {
                failedConnections++;
                Logger.warn("Failed to connect to: " + account.getUsername());
            }
        }
        
        totalConnections++;
    }
    
    private boolean connectToAccount(ConnectedAccount account) {
        Logger.info("Connecting to account: " + account.getUsername());
        
        // Apply anti-detection
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AccountConnector", 1.0f);
        }
        if (useMatrixBypass) {
            AntiDetectionManager.applyMovementModification("AccountConnector", 1.0f);
        }
        
        // Simulate connection process
        try {
            Thread.sleep(1000 + random.nextInt(2000)); // 1-3 seconds
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            return false;
        }
        
        // Simulate connection success (80% chance)
        return random.nextDouble() < 0.8;
    }
    
    private void processCommandQueue() {
        if (commandQueue.isEmpty()) {
            return;
        }
        
        String command = commandQueue.remove(0);
        Logger.info("Processing command: " + command);
        
        // Simulate command execution
        try {
            Thread.sleep(500 + random.nextInt(500));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        Logger.info("Command executed: " + command);
    }
    
    private void syncResources() {
        if (!shareResources) {
            return;
        }
        
        // Sync items
        if (shareItems && !sharedItems.isEmpty()) {
            Logger.debug("Syncing " + sharedItems.size() + " items");
        }
        
        // Sync coins
        if (shareCoins && sharedCoins > 0) {
            Logger.debug("Syncing " + sharedCoins + " coins");
        }
        
        // Sync experience
        if (shareExperience && sharedExperience > 0) {
            Logger.debug("Syncing " + sharedExperience + " experience");
        }
        
        // Sync AutoBuy settings
        if (shareSettings) {
            syncAutoBuySettings();
        }
    }
    
    private void syncAutoBuySettings() {
        Logger.debug("Syncing AutoBuy settings across accounts");
        
        // In real implementation, this would sync settings between accounts
        // For now, we'll just log the sync
        Logger.debug("AutoBuy settings synced");
    }
    
    private void initializeDefaultSettings() {
        // Initialize default shared items
        sharedItems.add("Diamond");
        sharedItems.add("Emerald");
        sharedItems.add("Gold Ingot");
        sharedItems.add("Iron Ingot");
        
        // Initialize shared AutoBuy items
        AutoBuyItem emerald = new AutoBuyItem(
            "emerald",
            "Emerald",
            "Currency",
            5000,
            AutoBuyItem.ItemType.MISC
        );
        emerald.setEnabled(true);
        emerald.setPriority(10);
        sharedAutoBuySettings.addItem(emerald);
    }
    
    // Account management methods
    public void addAccount(String username, String password, String role) {
        ConnectedAccount account = new ConnectedAccount(username, password, role);
        connectedAccounts.add(account);
        Logger.info("Added account: " + username + " (" + role + ")");
    }
    
    public void removeAccount(String username) {
        connectedAccounts.removeIf(account -> account.getUsername().equals(username));
        Logger.info("Removed account: " + username);
    }
    
    public void setPrimaryAccountName(String username) {
        this.primaryAccount = username;
        Logger.info("Set primary account: " + username);
    }
    
    public void switchAccount(String username) {
        for (ConnectedAccount account : connectedAccounts) {
            if (account.getUsername().equals(username)) {
                if (connectToAccount(account)) {
                    currentAccount = username;
                    Logger.info("Switched to account: " + username);
                } else {
                    Logger.warn("Failed to switch to account: " + username);
                }
                break;
            }
        }
    }
    
    // Resource sharing methods
    public void addSharedItem(String item) {
        if (!sharedItems.contains(item)) {
            sharedItems.add(item);
            Logger.info("Added shared item: " + item);
        }
    }
    
    public void removeSharedItem(String item) {
        if (sharedItems.remove(item)) {
            Logger.info("Removed shared item: " + item);
        }
    }
    
    public void addSharedCoins(int amount) {
        sharedCoins += amount;
        Logger.info("Added " + amount + " coins to shared pool (Total: " + sharedCoins + ")");
    }
    
    public void addSharedExperience(int amount) {
        sharedExperience += amount;
        Logger.info("Added " + amount + " experience to shared pool (Total: " + sharedExperience + ")");
    }
    
    // Command methods
    public void addCommand(String command) {
        commandQueue.add(command);
        Logger.info("Added command to queue: " + command);
    }
    
    public void clearCommandQueue() {
        commandQueue.clear();
        Logger.info("Command queue cleared");
    }
    
    public void sendChatMessage(String message) {
        if (enableChat) {
            String fullMessage = chatPrefix + " " + message;
            Logger.info("Chat message: " + fullMessage);
        }
    }
    
    // AutoBuy integration methods
    public void syncAutoBuyItem(AutoBuyItem item) {
        sharedAutoBuySettings.addItem(item);
        Logger.info("Synced AutoBuy item: " + item.getDisplayName());
    }
    
    public void removeAutoBuyItem(String itemName) {
        sharedAutoBuySettings.removeItem(itemName);
        Logger.info("Removed AutoBuy item: " + itemName);
    }
    
    public List<AutoBuyItem> getSharedAutoBuyItems() {
        return sharedAutoBuySettings.getAllItems();
    }
    
    // Getters and setters
    public boolean isConnectorEnabled() { return connectorEnabled; }
    public void setConnectorEnabled(boolean connectorEnabled) { this.connectorEnabled = connectorEnabled; }
    
    public boolean isAutoConnect() { return autoConnect; }
    public void setAutoConnect(boolean autoConnect) { this.autoConnect = autoConnect; }
    
    public boolean isShareResources() { return shareResources; }
    public void setShareResources(boolean shareResources) { this.shareResources = shareResources; }
    
    public boolean isShareWaypoints() { return shareWaypoints; }
    public void setShareWaypoints(boolean shareWaypoints) { this.shareWaypoints = shareWaypoints; }
    
    public boolean isShareSettings() { return shareSettings; }
    public void setShareSettings(boolean shareSettings) { this.shareSettings = shareSettings; }
    
    public List<ConnectedAccount> getConnectedAccounts() { return connectedAccounts; }
    public void setConnectedAccounts(List<ConnectedAccount> connectedAccounts) { this.connectedAccounts = connectedAccounts; }
    
    public String getPrimaryAccount() { return primaryAccount; }
    public void setPrimaryAccount(String primaryAccount) { this.primaryAccount = primaryAccount; }
    
    public String getCurrentAccount() { return currentAccount; }
    
    public boolean isShareItems() { return shareItems; }
    public void setShareItems(boolean shareItems) { this.shareItems = shareItems; }
    
    public boolean isShareCoins() { return shareCoins; }
    public void setShareCoins(boolean shareCoins) { this.shareCoins = shareCoins; }
    
    public boolean isShareExperience() { return shareExperience; }
    public void setShareExperience(boolean shareExperience) { this.shareExperience = shareExperience; }
    
    public List<String> getSharedItems() { return sharedItems; }
    public void setSharedItems(List<String> sharedItems) { this.sharedItems = sharedItems; }
    
    public int getSharedCoins() { return sharedCoins; }
    public void setSharedCoins(int sharedCoins) { this.sharedCoins = sharedCoins; }
    
    public int getSharedExperience() { return sharedExperience; }
    public void setSharedExperience(int sharedExperience) { this.sharedExperience = sharedExperience; }
    
    public boolean isEnableChat() { return enableChat; }
    public void setEnableChat(boolean enableChat) { this.enableChat = enableChat; }
    
    public boolean isEnableCommands() { return enableCommands; }
    public void setEnableCommands(boolean enableCommands) { this.enableCommands = enableCommands; }
    
    public String getChatPrefix() { return chatPrefix; }
    public void setChatPrefix(String chatPrefix) { this.chatPrefix = chatPrefix; }
    
    public List<String> getCommandQueue() { return commandQueue; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isUseMatrixBypass() { return useMatrixBypass; }
    public void setUseMatrixBypass(boolean useMatrixBypass) { this.useMatrixBypass = useMatrixBypass; }
    
    public boolean isRandomizeConnections() { return randomizeConnections; }
    public void setRandomizeConnections(boolean randomizeConnections) { this.randomizeConnections = randomizeConnections; }
    
    public boolean isVaryTiming() { return varyTiming; }
    public void setVaryTiming(boolean varyTiming) { this.varyTiming = varyTiming; }
    
    public boolean isConnected() { return isConnected; }
    public int getTotalConnections() { return totalConnections; }
    public int getSuccessfulConnections() { return successfulConnections; }
    public int getFailedConnections() { return failedConnections; }
    public long getLastConnectionTime() { return lastConnectionTime; }
    
    public AutoBuySettings getSharedAutoBuySettings() { return sharedAutoBuySettings; }
    public void setSharedAutoBuySettings(AutoBuySettings sharedAutoBuySettings) { this.sharedAutoBuySettings = sharedAutoBuySettings; }
    
    // Inner class for connected accounts
    public static class ConnectedAccount {
        private String username;
        private String password;
        private String role;
        private boolean isOnline;
        private long lastSeen;
        private int connectionCount;
        
        public ConnectedAccount(String username, String password, String role) {
            this.username = username;
            this.password = password;
            this.role = role;
            this.isOnline = false;
            this.lastSeen = System.currentTimeMillis();
            this.connectionCount = 0;
        }
        
        public String getUsername() { return username; }
        public void setUsername(String username) { this.username = username; }
        
        public String getPassword() { return password; }
        public void setPassword(String password) { this.password = password; }
        
        public String getRole() { return role; }
        public void setRole(String role) { this.role = role; }
        
        public boolean isOnline() { return isOnline; }
        public void setOnline(boolean online) { 
            this.isOnline = online;
            if (online) {
                this.connectionCount++;
                this.lastSeen = System.currentTimeMillis();
            }
        }
        
        public long getLastSeen() { return lastSeen; }
        public void setLastSeen(long lastSeen) { this.lastSeen = lastSeen; }
        
        public int getConnectionCount() { return connectionCount; }
        public void setConnectionCount(int connectionCount) { this.connectionCount = connectionCount; }
        
        @Override
        public String toString() {
            return username + " (" + role + ") - " + (isOnline ? "Online" : "Offline");
        }
    }
}