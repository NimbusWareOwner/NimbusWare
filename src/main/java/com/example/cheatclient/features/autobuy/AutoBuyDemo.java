package com.example.cheatclient.features.autobuy;

import com.example.cheatclient.utils.Logger;

import java.util.List;
import java.util.Random;

public class AutoBuyDemo {
    private AutoBuySettings settings;
    private Random random;
    
    public AutoBuyDemo(AutoBuySettings settings) {
        this.settings = settings;
        this.random = new Random();
    }
    
    public void simulateMarketActivity() {
        Logger.info("Starting AutoBuy market simulation...");
        
        // Simulate some random purchases
        simulateRandomPurchases();
        
        // Simulate price changes
        simulatePriceChanges();
        
        // Simulate new items appearing
        simulateNewItems();
        
        Logger.info("Market simulation completed!");
    }
    
    private void simulateRandomPurchases() {
        AutoBuyItem[] items = settings.getAllItems().toArray(new AutoBuyItem[0]);
        if (items.length == 0) return;
        
        int purchaseCount = random.nextInt(5) + 1; // 1-5 purchases
        
        for (int i = 0; i < purchaseCount; i++) {
            AutoBuyItem item = items[random.nextInt(items.length)];
            int quantity = random.nextInt(3) + 1; // 1-3 quantity
            
            // Simulate purchase
            settings.getPurchaseHistory().addPurchase(item, quantity, "DemoAccount" + (i + 1));
            
            Logger.info("Simulated purchase: " + quantity + "x " + item.getDisplayName() + 
                       " for " + (item.getPrice() * quantity) + " coins");
        }
    }
    
    private void simulatePriceChanges() {
        AutoBuyItem[] items = settings.getAllItems().toArray(new AutoBuyItem[0]);
        if (items.length == 0) return;
        
        int changeCount = random.nextInt(3) + 1; // 1-3 price changes
        
        for (int i = 0; i < changeCount; i++) {
            AutoBuyItem item = items[random.nextInt(items.length)];
            int oldPrice = item.getPrice();
            
            // Random price change between -20% and +30%
            double changePercent = (random.nextDouble() - 0.2) * 0.5; // -0.2 to 0.3
            int newPrice = (int) (oldPrice * (1 + changePercent));
            newPrice = Math.max(100, newPrice); // Minimum 100 coins
            
            item.setPrice(newPrice);
            
            String change = newPrice > oldPrice ? "increased" : "decreased";
            Logger.info("Price " + change + " for " + item.getDisplayName() + 
                       ": " + oldPrice + " → " + newPrice + " coins");
        }
    }
    
    private void simulateNewItems() {
        // Simulate new items appearing on the market
        String[] newItemNames = {
            "enchanted_diamond_sword", "magic_bow", "legendary_helmet", 
            "mystic_talisman", "rare_potion"
        };
        
        String[] newItemDisplays = {
            "Enchanted Diamond Sword", "Magic Bow", "Legendary Helmet",
            "Mystic Talisman", "Rare Potion"
        };
        
        String[] categories = {"Weapon", "Weapon", "Armor", "Talisman", "Consumable"};
        
        AutoBuyItem.ItemType[] types = {
            AutoBuyItem.ItemType.WEAPON, AutoBuyItem.ItemType.WEAPON, AutoBuyItem.ItemType.ARMOR,
            AutoBuyItem.ItemType.TALISMAN, AutoBuyItem.ItemType.CONSUMABLE
        };
        
        int newItemCount = random.nextInt(2) + 1; // 1-2 new items
        
        for (int i = 0; i < newItemCount; i++) {
            int index = random.nextInt(newItemNames.length);
            
            AutoBuyItem newItem = new AutoBuyItem(
                newItemNames[index],
                newItemDisplays[index],
                categories[index],
                random.nextInt(50000) + 10000, // 10k-60k coins
                types[index]
            );
            
            // Add some random enchantments
            if (random.nextBoolean()) {
                String[] enchantNames = {"Sharpness", "Protection", "Efficiency", "Fortune", "Unbreaking"};
                int level = random.nextInt(5) + 1;
                newItem.addEnchantment(new AutoBuyItem.Enchantment(
                    enchantNames[random.nextInt(enchantNames.length)], 
                    level, 
                    "Random enchantment"
                ));
            }
            
            settings.addItem(newItem);
            Logger.info("New item appeared: " + newItem.getDisplayName() + 
                       " for " + newItem.getFormattedPrice());
        }
    }
    
    public void showMarketStatus() {
        Logger.info("=== MARKET STATUS ===");
        Logger.info("Total items: " + settings.getTotalItems());
        Logger.info("Enabled items: " + settings.getEnabledItemsCount());
        Logger.info("Total purchases: " + settings.getPurchaseHistory().getTotalPurchases());
        Logger.info("Total spent: " + settings.getPurchaseHistory().getFormattedTotalSpent());
        Logger.info("Max price: " + settings.getFormattedMaxPrice());
        
        // Show recent purchases
        List<PurchaseHistory.PurchaseRecord> recent = settings.getPurchaseHistory().getRecentPurchases(3);
        if (!recent.isEmpty()) {
            Logger.info("Recent purchases:");
            for (PurchaseHistory.PurchaseRecord purchase : recent) {
                Logger.info("  " + purchase.getItemDisplay() + " x" + purchase.getQuantity() + 
                           " - " + purchase.getFormattedPrice());
            }
        }
    }
}