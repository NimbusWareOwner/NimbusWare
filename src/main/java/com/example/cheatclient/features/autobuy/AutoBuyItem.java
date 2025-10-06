package com.example.cheatclient.features.autobuy;

import java.util.List;
import java.util.ArrayList;

public class AutoBuyItem {
    private String name;
    private String displayName;
    private String category;
    private int price;
    private List<Enchantment> enchantments;
    private ItemType itemType;
    private boolean enabled;
    private int priority;
    private String description;
    
    public AutoBuyItem(String name, String displayName, String category, int price, ItemType itemType) {
        this.name = name;
        this.displayName = displayName;
        this.category = category;
        this.price = price;
        this.itemType = itemType;
        this.enchantments = new ArrayList<>();
        this.enabled = true;
        this.priority = 0;
        this.description = "";
    }
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    
    public String getDisplayName() { return displayName; }
    public void setDisplayName(String displayName) { this.displayName = displayName; }
    
    public String getCategory() { return category; }
    public void setCategory(String category) { this.category = category; }
    
    public int getPrice() { return price; }
    public void setPrice(int price) { this.price = price; }
    
    public List<Enchantment> getEnchantments() { return enchantments; }
    public void setEnchantments(List<Enchantment> enchantments) { this.enchantments = enchantments; }
    
    public ItemType getItemType() { return itemType; }
    public void setItemType(ItemType itemType) { this.itemType = itemType; }
    
    public boolean isEnabled() { return enabled; }
    public void setEnabled(boolean enabled) { this.enabled = enabled; }
    
    public int getPriority() { return priority; }
    public void setPriority(int priority) { this.priority = priority; }
    
    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }
    
    public void addEnchantment(Enchantment enchantment) {
        this.enchantments.add(enchantment);
    }
    
    public void removeEnchantment(Enchantment enchantment) {
        this.enchantments.remove(enchantment);
    }
    
    public String getFormattedPrice() {
        return String.format("%,d", price) + " coins";
    }
    
    public String getEnchantmentString() {
        if (enchantments.isEmpty()) {
            return "No enchantments";
        }
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < enchantments.size(); i++) {
            if (i > 0) sb.append(", ");
            sb.append(enchantments.get(i).getName()).append(" ").append(enchantments.get(i).getLevel());
        }
        return sb.toString();
    }
    
    public enum ItemType {
        WEAPON("Weapon", "⚔️"),
        ARMOR("Armor", "🛡️"),
        TOOL("Tool", "⛏️"),
        TALISMAN("Talisman", "🔮"),
        CONSUMABLE("Consumable", "🍎"),
        BLOCK("Block", "🧱"),
        MISC("Miscellaneous", "📦");
        
        private final String displayName;
        private final String emoji;
        
        ItemType(String displayName, String emoji) {
            this.displayName = displayName;
            this.emoji = emoji;
        }
        
        public String getDisplayName() { return displayName; }
        public String getEmoji() { return emoji; }
    }
    
    public static class Enchantment {
        private String name;
        private int level;
        private String description;
        
        public Enchantment(String name, int level, String description) {
            this.name = name;
            this.level = level;
            this.description = description;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public int getLevel() { return level; }
        public void setLevel(int level) { this.level = level; }
        
        public String getDescription() { return description; }
        public void setDescription(String description) { this.description = description; }
        
        @Override
        public String toString() {
            return name + " " + level;
        }
    }
}