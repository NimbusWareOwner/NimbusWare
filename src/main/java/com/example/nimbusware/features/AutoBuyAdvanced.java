package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class AutoBuyAdvanced extends Module {
    // Account pools
    private final Map<String, AccountPool> accountPools = new ConcurrentHashMap<>();
    private final List<Account> activeCheckers = new ArrayList<>();
    private final List<Account> activeBuyers = new ArrayList<>();
    
    // Server configuration
    private String serverUrl = "https://funtime.gg";
    private String apiKey = "";
    private String userAgent = "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36";
    
    // Timing and anti-detection
    private int checkInterval = 3000;
    private int buyDelay = 500;
    private int maxRetries = 3;
    private boolean useFuntimeBypass = true;
    private boolean randomizeTiming = true;
    private boolean rotateAccounts = true;
    
    // Queue management
    private final Queue<Account> checkerQueue = new LinkedList<>();
    private final Queue<Account> buyerQueue = new LinkedList<>();
    private int currentCheckerIndex = 0;
    private int currentBuyerIndex = 0;
    
    // Status tracking
    private boolean isInQueue = false;
    private int queuePosition = -1;
    private int maxQueueSize = 100;
    private long lastSuccessfulCheck = 0;
    private int consecutiveFailures = 0;
    
    // Threading
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4);
    private final Random random = new Random();
    
    public AutoBuyAdvanced() {
        super("AutoBuyAdvanced", "Advanced Funtime autobuy with account rotation and anti-detection", Module.Category.MISC, 0);
        
        initializeAccountPools();
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("AutoBuyAdvanced");
        }
        
        startAutoBuyProcess();
        Logger.info("AutoBuyAdvanced enabled with " + activeCheckers.size() + " checkers and " + activeBuyers.size() + " buyers");
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("AutoBuyAdvanced");
        }
        
        stopAutoBuyProcess();
        Logger.info("AutoBuyAdvanced disabled");
    }
    
    private void initializeAccountPools() {
        // Initialize checker accounts pool
        AccountPool checkerPool = new AccountPool("checkers");
        checkerPool.addAccount(new Account("checker1", "pass1", AccountType.CHECKER, 0));
        checkerPool.addAccount(new Account("checker2", "pass2", AccountType.CHECKER, 0));
        checkerPool.addAccount(new Account("checker3", "pass3", AccountType.CHECKER, 0));
        accountPools.put("checkers", checkerPool);
        
        // Initialize buyer accounts pool
        AccountPool buyerPool = new AccountPool("buyers");
        buyerPool.addAccount(new Account("buyer1", "pass1", AccountType.BUYER, 0));
        buyerPool.addAccount(new Account("buyer2", "pass2", AccountType.BUYER, 0));
        accountPools.put("buyers", buyerPool);
        
        // Populate active lists
        updateActiveAccounts();
    }
    
    private void startAutoBuyProcess() {
        // Start checker tasks
        scheduler.scheduleAtFixedRate(this::checkerTask, 0, checkInterval, TimeUnit.MILLISECONDS);
        
        // Start buyer task
        scheduler.scheduleAtFixedRate(this::buyerTask, 1000, checkInterval * 2, TimeUnit.MILLISECONDS);
        
        // Start account rotation task
        if (rotateAccounts) {
            scheduler.scheduleAtFixedRate(this::rotateAccounts, 30000, 30000, TimeUnit.MILLISECONDS);
        }
    }
    
    private void stopAutoBuyProcess() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
        }
    }
    
    private void checkerTask() {
        if (!isEnabled()) return;
        
        try {
            // Get next checker account
            Account checker = getNextChecker();
            if (checker == null) return;
            
            // Perform queue check
            QueueStatus status = performQueueCheck(checker);
            
            if (status != null) {
                updateQueueStatus(status);
                lastSuccessfulCheck = System.currentTimeMillis();
                consecutiveFailures = 0;
            } else {
                consecutiveFailures++;
                Logger.warn("Queue check failed, consecutive failures: " + consecutiveFailures);
            }
            
        } catch (Exception e) {
            Logger.error("Error in checker task: " + e.getMessage());
            consecutiveFailures++;
        }
    }
    
    private void buyerTask() {
        if (!isEnabled() || isInQueue) return;
        
        try {
            // Only attempt to buy if we're not in queue and queue is available
            if (!isInQueue && queuePosition == -1) {
                Account buyer = getNextBuyer();
                if (buyer != null) {
                    attemptPurchase(buyer);
                }
            }
        } catch (Exception e) {
            Logger.error("Error in buyer task: " + e.getMessage());
        }
    }
    
    private void rotateAccounts() {
        if (!rotateAccounts) return;
        
        updateActiveAccounts();
        Logger.info("Rotated accounts - Active checkers: " + activeCheckers.size() + ", Active buyers: " + activeBuyers.size());
    }
    
    private Account getNextChecker() {
        if (activeCheckers.isEmpty()) return null;
        
        if (currentCheckerIndex >= activeCheckers.size()) {
            currentCheckerIndex = 0;
        }
        
        return activeCheckers.get(currentCheckerIndex++);
    }
    
    private Account getNextBuyer() {
        if (activeBuyers.isEmpty()) return null;
        
        if (currentBuyerIndex >= activeBuyers.size()) {
            currentBuyerIndex = 0;
        }
        
        return activeBuyers.get(currentBuyerIndex++);
    }
    
    private QueueStatus performQueueCheck(Account account) {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoBuyAdvanced", 1.0f);
        }
        
        // Simulate API call with realistic delays
        try {
            Thread.sleep(100 + random.nextInt(200)); // 100-300ms delay
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        // Mock response
        QueueStatus status = new QueueStatus();
        status.isInQueue = random.nextDouble() < 0.3; // 30% chance of being in queue
        status.position = status.isInQueue ? random.nextInt(50) + 1 : -1;
        status.maxSize = 100;
        status.serverOnline = true;
        status.timestamp = System.currentTimeMillis();
        
        return status;
    }
    
    private void attemptPurchase(Account account) {
        Logger.info("Attempting purchase with account: " + account.username);
        
        try {
            // Apply purchase delay
            Thread.sleep(buyDelay + random.nextInt(500));
            
            // Simulate purchase
            boolean success = simulatePurchase(account);
            
            if (success) {
                Logger.info("Successfully purchased slot with " + account.username + "!");
                isInQueue = true;
                queuePosition = 1;
            } else {
                Logger.warn("Purchase failed with " + account.username);
            }
            
        } catch (Exception e) {
            Logger.error("Error during purchase: " + e.getMessage());
        }
    }
    
    private boolean simulatePurchase(Account account) {
        // Apply anti-detection modifications
        if (useFuntimeBypass) {
            AntiDetectionManager.applyCombatModification("AutoBuyAdvanced", 1.0f);
        }
        
        // Simulate success rate based on account usage
        double successRate = 0.8 - (account.usageCount * 0.1); // Decrease success rate with usage
        successRate = Math.max(0.3, successRate); // Minimum 30% success rate
        
        account.usageCount++;
        return random.nextDouble() < successRate;
    }
    
    private void updateQueueStatus(QueueStatus status) {
        isInQueue = status.isInQueue;
        queuePosition = status.position;
        maxQueueSize = status.maxSize;
        
        if (isInQueue) {
            Logger.info("Queue status - Position: " + queuePosition + "/" + maxQueueSize);
        } else {
            Logger.info("Queue status - Not in queue");
        }
    }
    
    private void updateActiveAccounts() {
        activeCheckers.clear();
        activeBuyers.clear();
        
        for (AccountPool pool : accountPools.values()) {
            for (Account account : pool.getAccounts()) {
                if (account.type == AccountType.CHECKER) {
                    activeCheckers.add(account);
                } else if (account.type == AccountType.BUYER) {
                    activeBuyers.add(account);
                }
            }
        }
    }
    
    // Configuration methods
    public void addAccount(String poolName, String username, String password, AccountType type) {
        AccountPool pool = accountPools.get(poolName);
        if (pool != null) {
            pool.addAccount(new Account(username, password, type, 0));
            updateActiveAccounts();
            Logger.info("Added " + type + " account " + username + " to pool " + poolName);
        }
    }
    
    public void removeAccount(String poolName, String username) {
        AccountPool pool = accountPools.get(poolName);
        if (pool != null) {
            pool.removeAccount(username);
            updateActiveAccounts();
            Logger.info("Removed account " + username + " from pool " + poolName);
        }
    }
    
    public void setServerUrl(String serverUrl) {
        this.serverUrl = serverUrl;
    }
    
    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
    
    // Getters and setters
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isRandomizeTiming() { return randomizeTiming; }
    public void setRandomizeTiming(boolean randomizeTiming) { this.randomizeTiming = randomizeTiming; }
    
    public boolean isRotateAccounts() { return rotateAccounts; }
    public void setRotateAccounts(boolean rotateAccounts) { this.rotateAccounts = rotateAccounts; }
    
    public int getCheckInterval() { return checkInterval; }
    public void setCheckInterval(int checkInterval) { this.checkInterval = Math.max(1000, checkInterval); }
    
    public int getBuyDelay() { return buyDelay; }
    public void setBuyDelay(int buyDelay) { this.buyDelay = Math.max(0, buyDelay); }
    
    public boolean isInQueue() { return isInQueue; }
    public int getQueuePosition() { return queuePosition; }
    public int getMaxQueueSize() { return maxQueueSize; }
    public int getConsecutiveFailures() { return consecutiveFailures; }
    
    // Inner classes
    public static class Account {
        public String username;
        public String password;
        public AccountType type;
        public int usageCount;
        
        public Account(String username, String password, AccountType type, int usageCount) {
            this.username = username;
            this.password = password;
            this.type = type;
            this.usageCount = usageCount;
        }
    }
    
    public static class AccountPool {
        private final String name;
        private final List<Account> accounts = new ArrayList<>();
        
        public AccountPool(String name) {
            this.name = name;
        }
        
        public void addAccount(Account account) {
            accounts.add(account);
        }
        
        public void removeAccount(String username) {
            accounts.removeIf(account -> account.username.equals(username));
        }
        
        public List<Account> getAccounts() {
            return new ArrayList<>(accounts);
        }
        
        public String getName() { return name; }
    }
    
    public static class QueueStatus {
        public boolean isInQueue;
        public int position;
        public int maxSize;
        public boolean serverOnline;
        public long timestamp;
    }
    
    public enum AccountType {
        CHECKER,
        BUYER
    }
}