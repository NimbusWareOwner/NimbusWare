# ⚔️ SwordCraft & AccountConnector - Money Making Guide

## 📋 Overview

The SwordCraft and AccountConnector modules provide advanced money-making capabilities through automated sword crafting and multi-account management. These modules work together to maximize profits while maintaining anti-detection features.

## ⚔️ SwordCraft Module

### Core Functionality

SwordCraft automates the entire process of making money through sword crafting:

1. **Teleportation** - Uses `/rtp` to find new locations
2. **Tree Finding** - Searches for nearby trees
3. **Wood Collection** - Collects 5 wood blocks
4. **Crafting** - Creates workbench and sticks
5. **Shopping** - Buys emeralds from `/shop`
6. **Sword Crafting** - Crafts specified number of swords
7. **Selling** - Sells swords for profit

### Step-by-Step Process

#### **Step 1: Random Teleportation**
```java
// RTP settings
setUseRTP(true);
setRtpCommand("/rtp");
setRtpDelay(2000); // 2 seconds delay after RTP
setMaxRTPAttempts(5);
```

#### **Step 2: Tree Search**
```java
// Tree finding settings
setSearchRadius(50); // Search within 50 blocks
setMaxSearchAttempts(10);
// Supported tree types: Oak, Birch, Spruce, Jungle, Acacia, Dark Oak
```

#### **Step 3: Wood Collection**
```java
// Wood collection settings
setWoodBlocksNeeded(5); // Collect 5 wood blocks
setSticksPerWood(4); // 4 sticks per wood block
```

#### **Step 4: Crafting Setup**
```java
// Crafting settings
setAutoCraftWorkbench(true); // Auto-craft workbench
setAutoCraftSticks(true); // Auto-craft sticks
setCraftDelay(1000); // 1 second between crafts
```

#### **Step 5: Emerald Purchase**
```java
// Shop settings
setUseShop(true);
setShopCommand("/shop");
setEmeraldShopItem("emerald");
setEmeraldPrice(5000); // Price per emerald
```

#### **Step 6: Sword Crafting**
```java
// Sword crafting settings
setSwordsToCraft(10); // Craft 10 swords
setAutoCraftSwords(true);
setSwordSellPrice(1000); // Price per sword
```

### Configuration Examples

#### **Basic Setup**
```java
SwordCraft swordCraft = new SwordCraft();
swordCraft.setSwordsToCraft(10);
swordCraft.setEmeraldPrice(5000);
swordCraft.setSwordSellPrice(1000);
swordCraft.setUseRTP(true);
swordCraft.setUseShop(true);
```

#### **Advanced Configuration**
```java
// Money making optimization
swordCraft.setSwordsToCraft(50); // Craft 50 swords
swordCraft.setEmeraldPrice(4500); // Lower emerald price
swordCraft.setSwordSellPrice(1200); // Higher sword price

// Anti-detection settings
swordCraft.setUseFuntimeBypass(true);
swordCraft.setUseMatrixBypass(true);
swordCraft.setRandomizeActions(true);
swordCraft.setHumanLikeBehavior(true);

// Timing optimization
swordCraft.setCraftDelay(800); // Faster crafting
swordCraft.setActionDelay(300); // Faster actions
```

### Profit Calculation

#### **Formula**
```
Profit = (Swords × Sword Price) - (Emeralds × Emerald Price)
Emeralds = Swords × 2 (each sword needs 2 emeralds)
```

#### **Example Calculation**
```java
// For 10 swords:
// Emeralds needed: 10 × 2 = 20
// Emerald cost: 20 × 5000 = 100,000 coins
// Sword revenue: 10 × 1000 = 10,000 coins
// Profit: 10,000 - 100,000 = -90,000 coins (LOSS!)

// For 50 swords:
// Emeralds needed: 50 × 2 = 100
// Emerald cost: 100 × 4500 = 450,000 coins
// Sword revenue: 50 × 1200 = 60,000 coins
// Profit: 60,000 - 450,000 = -390,000 coins (LOSS!)
```

#### **Profitable Configuration**
```java
// Profitable setup:
swordCraft.setSwordsToCraft(100);
swordCraft.setEmeraldPrice(2000); // Lower emerald price
swordCraft.setSwordSellPrice(5000); // Higher sword price

// Calculation:
// Emeralds needed: 100 × 2 = 200
// Emerald cost: 200 × 2000 = 400,000 coins
// Sword revenue: 100 × 5000 = 500,000 coins
// Profit: 500,000 - 400,000 = 100,000 coins (PROFIT!)
```

### Anti-Detection Features

#### **Funtime Bypass**
- Specialized bypasses for Funtime servers
- Combat modification application
- Movement pattern randomization

#### **Matrix Bypass**
- Advanced bypasses for Matrix anticheat
- Movement modification application
- Action timing variation

#### **Human-Like Behavior**
- Random delays between actions
- Action randomization
- Natural movement patterns

## 🔗 AccountConnector Module

### Core Functionality

AccountConnector manages multiple AutoBuy accounts and enables resource sharing:

1. **Account Management** - Connect and manage multiple accounts
2. **Resource Sharing** - Share items, coins, and experience
3. **AutoBuy Integration** - Sync AutoBuy settings across accounts
4. **Communication** - Chat and command sharing between accounts

### Account Management

#### **Adding Accounts**
```java
AccountConnector connector = new AccountConnector();

// Add checker accounts
connector.addAccount("checker1", "password1", "Checker");
connector.addAccount("checker2", "password2", "Checker");

// Add buyer accounts
connector.addAccount("buyer1", "password3", "Buyer");
connector.addAccount("buyer2", "password4", "Buyer");

// Set primary account
connector.setPrimaryAccountName("buyer1");
```

#### **Account Roles**
- **Checker** - Monitors queue and server status
- **Buyer** - Purchases items and manages transactions
- **Manager** - Coordinates between accounts
- **Worker** - Performs specific tasks (like SwordCraft)

### Resource Sharing

#### **Item Sharing**
```java
// Enable item sharing
connector.setShareItems(true);

// Add items to shared pool
connector.addSharedItem("Diamond");
connector.addSharedItem("Emerald");
connector.addSharedItem("Gold Ingot");
connector.addSharedItem("Iron Ingot");
```

#### **Coin Sharing**
```java
// Enable coin sharing
connector.setShareCoins(true);

// Add coins to shared pool
connector.addSharedCoins(100000); // Add 100k coins
```

#### **Experience Sharing**
```java
// Enable experience sharing
connector.setShareExperience(true);

// Add experience to shared pool
connector.addSharedExperience(1000); // Add 1000 XP
```

### AutoBuy Integration

#### **Shared AutoBuy Settings**
```java
// Sync AutoBuy items across accounts
AutoBuyItem emerald = new AutoBuyItem(
    "emerald",
    "Emerald",
    "Currency",
    5000,
    AutoBuyItem.ItemType.MISC
);
connector.syncAutoBuyItem(emerald);

// Get shared items
List<AutoBuyItem> sharedItems = connector.getSharedAutoBuyItems();
```

#### **Command Sharing**
```java
// Enable command sharing
connector.setEnableCommands(true);

// Add commands to queue
connector.addCommand("/shop");
connector.addCommand("/rtp");
connector.addCommand("/home");
```

### Communication System

#### **Chat Integration**
```java
// Enable chat communication
connector.setEnableChat(true);
connector.setChatPrefix("[AccountConnector]");

// Send messages between accounts
connector.sendChatMessage("Starting sword craft process");
connector.sendChatMessage("Need emeralds for crafting");
```

## 🎮 Usage Examples

### Complete SwordCraft Setup
```bash
# Enable SwordCraft
CheatClient> toggle SwordCraft

# Configure for profit
SwordCraft> setSwordsToCraft 100
SwordCraft> setEmeraldPrice 2000
SwordCraft> setSwordSellPrice 5000
SwordCraft> setUseRTP true
SwordCraft> setUseShop true

# Start the process
SwordCraft> startSwordCraftProcess
```

### AccountConnector Setup
```bash
# Enable AccountConnector
CheatClient> toggle AccountConnector

# Add accounts
AccountConnector> addAccount checker1 password1 Checker
AccountConnector> addAccount checker2 password2 Checker
AccountConnector> addAccount buyer1 password3 Buyer

# Configure sharing
AccountConnector> setShareResources true
AccountConnector> setShareItems true
AccountConnector> setShareCoins true

# Start connection
AccountConnector> setAutoConnect true
```

### Integrated Workflow
```bash
# 1. Setup accounts
AccountConnector> addAccount worker1 password1 Worker
AccountConnector> addAccount worker2 password2 Worker

# 2. Configure SwordCraft
SwordCraft> setSwordsToCraft 50
SwordCraft> setEmeraldPrice 3000
SwordCraft> setSwordSellPrice 4000

# 3. Start money making
SwordCraft> startSwordCraftProcess
AccountConnector> switchAccount worker1
SwordCraft> startSwordCraftProcess
```

## 📊 Statistics and Monitoring

### SwordCraft Statistics
```java
// View current stats
Logger.info("=== SwordCraft Statistics ===");
Logger.info("Trees found: " + swordCraft.getTreesFound());
Logger.info("Wood collected: " + swordCraft.getWoodCollected());
Logger.info("Workbenches crafted: " + swordCraft.getWorkbenchesCrafted());
Logger.info("Sticks crafted: " + swordCraft.getSticksCrafted());
Logger.info("Emeralds bought: " + swordCraft.getEmeraldsBought());
Logger.info("Swords crafted: " + swordCraft.getSwordsCrafted());
Logger.info("Total profit: " + swordCraft.getTotalProfit() + " coins");
```

### AccountConnector Statistics
```java
// View connection stats
Logger.info("=== AccountConnector Statistics ===");
Logger.info("Total connections: " + connector.getTotalConnections());
Logger.info("Successful: " + connector.getSuccessfulConnections());
Logger.info("Failed: " + connector.getFailedConnections());
Logger.info("Current account: " + connector.getCurrentAccount());
Logger.info("Shared coins: " + connector.getSharedCoins());
Logger.info("Shared experience: " + connector.getSharedExperience());
```

## 🛡️ Anti-Detection Features

### SwordCraft Anti-Detection
- **Funtime Bypass** - Specialized bypasses for Funtime servers
- **Matrix Bypass** - Advanced bypasses for Matrix anticheat
- **Human-Like Behavior** - Mimics human crafting patterns
- **Action Randomization** - Randomizes actions and timing
- **Timing Variation** - Varies delays between actions

### AccountConnector Anti-Detection
- **Funtime Bypass** - Specialized bypasses for Funtime servers
- **Matrix Bypass** - Advanced bypasses for Matrix anticheat
- **Connection Randomization** - Randomizes connection patterns
- **Timing Variation** - Varies connection timing

## ⚙️ Advanced Configuration

### Multi-Account SwordCraft
```java
// Setup multiple accounts for sword crafting
AccountConnector connector = new AccountConnector();

// Add worker accounts
connector.addAccount("worker1", "pass1", "Worker");
connector.addAccount("worker2", "pass2", "Worker");
connector.addAccount("worker3", "pass3", "Worker");

// Configure each account for sword crafting
for (ConnectedAccount account : connector.getConnectedAccounts()) {
    if (account.getRole().equals("Worker")) {
        connector.switchAccount(account.getUsername());
        // Configure SwordCraft for this account
        SwordCraft swordCraft = new SwordCraft();
        swordCraft.setSwordsToCraft(25); // 25 swords per account
        swordCraft.startSwordCraftProcess();
    }
}
```

### Profit Optimization
```java
// Optimize for maximum profit
SwordCraft swordCraft = new SwordCraft();

// High-volume, low-cost setup
swordCraft.setSwordsToCraft(200);
swordCraft.setEmeraldPrice(1500); // Very low emerald price
swordCraft.setSwordSellPrice(3000); // High sword price

// Expected profit: 200 × 3000 - 400 × 1500 = 600,000 - 600,000 = 0
// Need to find better prices for profit
```

## 🔧 Troubleshooting

### Common SwordCraft Issues
1. **Not finding trees** - Increase search radius
2. **Crafting too slow** - Reduce craft delays
3. **Not profitable** - Adjust prices and quantities
4. **Detection warnings** - Enable anti-detection features

### Common AccountConnector Issues
1. **Connection failures** - Check account credentials
2. **Resource sync issues** - Verify sharing settings
3. **Command queue problems** - Clear and restart queue
4. **Chat not working** - Enable chat features

## 📈 Best Practices

### SwordCraft Best Practices
- Start with small quantities to test profitability
- Monitor prices regularly and adjust accordingly
- Use anti-detection features for stealth
- Monitor statistics for optimization

### AccountConnector Best Practices
- Use different IPs for each account
- Rotate accounts regularly
- Monitor connection health
- Keep credentials secure

## 🎯 Conclusion

The SwordCraft and AccountConnector modules provide a comprehensive money-making solution with multi-account support. When properly configured, they can generate significant profits while maintaining anti-detection capabilities.

**Key Benefits:**
- ✅ Automated sword crafting process
- ✅ Multi-account management
- ✅ Resource sharing between accounts
- ✅ Anti-detection bypasses
- ✅ Profit calculation and optimization
- ✅ Statistics and monitoring
- ✅ Flexible configuration options

**Ready to use:** Both modules are fully functional and ready for deployment on Funtime servers!