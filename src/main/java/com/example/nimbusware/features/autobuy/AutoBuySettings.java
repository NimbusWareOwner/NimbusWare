package com.example.nimbusware.features.autobuy;

import java.util.*;
import java.util.stream.Collectors;

public class AutoBuySettings {
    private Map<String, AutoBuyItem> items;
    private PurchaseHistory purchaseHistory;
    private int maxPrice;
    private boolean autoBuyEnabled;
    private boolean priorityMode;
    private int checkInterval;
    private boolean notificationsEnabled;
    private String notificationSound;
    
    public AutoBuySettings() {
        this.items = new HashMap<>();
        this.purchaseHistory = new PurchaseHistory();
        this.maxPrice = 1000000; // 1M coins default
        this.autoBuyEnabled = true;
        this.priorityMode = false;
        this.checkInterval = 5000; // 5 seconds
        this.notificationsEnabled = true;
        this.notificationSound = "default";
        
        initializeDefaultItems();
    }
    
    private void initializeDefaultItems() {
        // Weapons
        addItem(new AutoBuyItem("diamond_sword", "Diamond Sword", "Weapon", 50000, AutoBuyItem.ItemType.WEAPON));
        addItem(new AutoBuyItem("netherite_sword", "Netherite Sword", "Weapon", 200000, AutoBuyItem.ItemType.WEAPON));
        addItem(new AutoBuyItem("bow", "Bow", "Weapon", 30000, AutoBuyItem.ItemType.WEAPON));
        addItem(new AutoBuyItem("crossbow", "Crossbow", "Weapon", 40000, AutoBuyItem.ItemType.WEAPON));
        
        // Armor
        addItem(new AutoBuyItem("diamond_helmet", "Diamond Helmet", "Armor", 40000, AutoBuyItem.ItemType.ARMOR));
        addItem(new AutoBuyItem("diamond_chestplate", "Diamond Chestplate", "Armor", 60000, AutoBuyItem.ItemType.ARMOR));
        addItem(new AutoBuyItem("diamond_leggings", "Diamond Leggings", "Armor", 50000, AutoBuyItem.ItemType.ARMOR));
        addItem(new AutoBuyItem("diamond_boots", "Diamond Boots", "Armor", 35000, AutoBuyItem.ItemType.ARMOR));
        
        // Tools
        addItem(new AutoBuyItem("diamond_pickaxe", "Diamond Pickaxe", "Tool", 45000, AutoBuyItem.ItemType.TOOL));
        addItem(new AutoBuyItem("diamond_axe", "Diamond Axe", "Tool", 40000, AutoBuyItem.ItemType.TOOL));
        addItem(new AutoBuyItem("diamond_shovel", "Diamond Shovel", "Tool", 25000, AutoBuyItem.ItemType.TOOL));
        
        // Talismans
        addItem(new AutoBuyItem("speed_talisman", "Speed Talisman", "Talisman", 75000, AutoBuyItem.ItemType.TALISMAN));
        addItem(new AutoBuyItem("strength_talisman", "Strength Talisman", "Talisman", 80000, AutoBuyItem.ItemType.TALISMAN));
        addItem(new AutoBuyItem("health_talisman", "Health Talisman", "Talisman", 90000, AutoBuyItem.ItemType.TALISMAN));
        addItem(new AutoBuyItem("luck_talisman", "Luck Talisman", "Talisman", 100000, AutoBuyItem.ItemType.TALISMAN));
        
        // Consumables
        addItem(new AutoBuyItem("golden_apple", "Golden Apple", "Consumable", 1000, AutoBuyItem.ItemType.CONSUMABLE));
        addItem(new AutoBuyItem("enchanted_golden_apple", "Enchanted Golden Apple", "Consumable", 5000, AutoBuyItem.ItemType.CONSUMABLE));
        addItem(new AutoBuyItem("potion_healing", "Healing Potion", "Consumable", 500, AutoBuyItem.ItemType.CONSUMABLE));
        
        // Add enchantments to some items
        addEnchantmentsToItems();
    }
    
    private void addEnchantmentsToItems() {
        // Add enchantments to weapons
        AutoBuyItem sword = items.get("diamond_sword");
        if (sword != null) {
            sword.addEnchantment(new AutoBuyItem.Enchantment("Sharpness", 5, "Increases melee damage"));
            sword.addEnchantment(new AutoBuyItem.Enchantment("Unbreaking", 3, "Increases durability"));
            sword.addEnchantment(new AutoBuyItem.Enchantment("Fire Aspect", 2, "Sets targets on fire"));
        }
        
        AutoBuyItem bow = items.get("bow");
        if (bow != null) {
            bow.addEnchantment(new AutoBuyItem.Enchantment("Power", 5, "Increases arrow damage"));
            bow.addEnchantment(new AutoBuyItem.Enchantment("Punch", 2, "Knocks back targets"));
            bow.addEnchantment(new AutoBuyItem.Enchantment("Flame", 1, "Arrows set targets on fire"));
        }
        
        // Add enchantments to armor
        AutoBuyItem helmet = items.get("diamond_helmet");
        if (helmet != null) {
            helmet.addEnchantment(new AutoBuyItem.Enchantment("Protection", 4, "Reduces damage"));
            helmet.addEnchantment(new AutoBuyItem.Enchantment("Unbreaking", 3, "Increases durability"));
            helmet.addEnchantment(new AutoBuyItem.Enchantment("Respiration", 3, "Extends underwater breathing"));
        }
        
        // Add enchantments to tools
        AutoBuyItem pickaxe = items.get("diamond_pickaxe");
        if (pickaxe != null) {
            pickaxe.addEnchantment(new AutoBuyItem.Enchantment("Efficiency", 5, "Increases mining speed"));
            pickaxe.addEnchantment(new AutoBuyItem.Enchantment("Fortune", 3, "Increases block drops"));
            pickaxe.addEnchantment(new AutoBuyItem.Enchantment("Unbreaking", 3, "Increases durability"));
        }
    }
    
    public void addItem(AutoBuyItem item) {
        items.put(item.getName(), item);
    }
    
    public void removeItem(String name) {
        items.remove(name);
    }
    
    public AutoBuyItem getItem(String name) {
        return items.get(name);
    }
    
    public List<AutoBuyItem> getAllItems() {
        return new ArrayList<>(items.values());
    }
    
    public List<AutoBuyItem> getItemsByCategory(String category) {
        return items.values().stream()
                .filter(item -> item.getCategory().equals(category))
                .collect(Collectors.toList());
    }
    
    public List<AutoBuyItem> getEnabledItems() {
        return items.values().stream()
                .filter(AutoBuyItem::isEnabled)
                .collect(Collectors.toList());
    }
    
    public List<AutoBuyItem> getItemsByType(AutoBuyItem.ItemType type) {
        return items.values().stream()
                .filter(item -> item.getItemType() == type)
                .collect(Collectors.toList());
    }
    
    public List<AutoBuyItem> getAffordableItems(int budget) {
        return items.values().stream()
                .filter(item -> item.isEnabled() && item.getPrice() <= budget)
                .sorted(Comparator.comparingInt(AutoBuyItem::getPrice))
                .collect(Collectors.toList());
    }
    
    public List<AutoBuyItem> getItemsByPriority() {
        return items.values().stream()
                .filter(AutoBuyItem::isEnabled)
                .sorted(Comparator.comparingInt(AutoBuyItem::getPriority).reversed())
                .collect(Collectors.toList());
    }
    
    // Getters and setters
    public Map<String, AutoBuyItem> getItems() { return items; }
    public void setItems(Map<String, AutoBuyItem> items) { this.items = items; }
    
    public PurchaseHistory getPurchaseHistory() { return purchaseHistory; }
    public void setPurchaseHistory(PurchaseHistory purchaseHistory) { this.purchaseHistory = purchaseHistory; }
    
    public int getMaxPrice() { return maxPrice; }
    public void setMaxPrice(int maxPrice) { this.maxPrice = maxPrice; }
    
    public boolean isAutoBuyEnabled() { return autoBuyEnabled; }
    public void setAutoBuyEnabled(boolean autoBuyEnabled) { this.autoBuyEnabled = autoBuyEnabled; }
    
    public boolean isPriorityMode() { return priorityMode; }
    public void setPriorityMode(boolean priorityMode) { this.priorityMode = priorityMode; }
    
    public int getCheckInterval() { return checkInterval; }
    public void setCheckInterval(int checkInterval) { this.checkInterval = checkInterval; }
    
    public boolean isNotificationsEnabled() { return notificationsEnabled; }
    public void setNotificationsEnabled(boolean notificationsEnabled) { this.notificationsEnabled = notificationsEnabled; }
    
    public String getNotificationSound() { return notificationSound; }
    public void setNotificationSound(String notificationSound) { this.notificationSound = notificationSound; }
    
    public String getFormattedMaxPrice() {
        return String.format("%,d", maxPrice) + " coins";
    }
    
    public int getTotalItems() {
        return items.size();
    }
    
    public int getEnabledItemsCount() {
        return (int) items.values().stream().filter(AutoBuyItem::isEnabled).count();
    }
}