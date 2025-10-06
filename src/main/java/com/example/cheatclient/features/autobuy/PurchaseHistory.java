package com.example.cheatclient.features.autobuy;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class PurchaseHistory {
    private List<PurchaseRecord> purchases;
    private int totalSpent;
    private int totalPurchases;
    
    public PurchaseHistory() {
        this.purchases = new ArrayList<>();
        this.totalSpent = 0;
        this.totalPurchases = 0;
    }
    
    public void addPurchase(AutoBuyItem item, int quantity, String account) {
        PurchaseRecord record = new PurchaseRecord(item, quantity, account);
        purchases.add(record);
        totalSpent += item.getPrice() * quantity;
        totalPurchases += quantity;
    }
    
    public List<PurchaseRecord> getPurchases() {
        return new ArrayList<>(purchases);
    }
    
    public List<PurchaseRecord> getRecentPurchases(int count) {
        int start = Math.max(0, purchases.size() - count);
        return purchases.subList(start, purchases.size());
    }
    
    public List<PurchaseRecord> getAllPurchases() {
        return new ArrayList<>(purchases);
    }
    
    public int getTotalSpent() {
        return totalSpent;
    }
    
    public int getTotalPurchases() {
        return totalPurchases;
    }
    
    public String getFormattedTotalSpent() {
        return String.format("%,d", totalSpent) + " coins";
    }
    
    public void clearHistory() {
        purchases.clear();
        totalSpent = 0;
        totalPurchases = 0;
    }
    
    public static class PurchaseRecord {
        private AutoBuyItem item;
        private int quantity;
        private String account;
        private LocalDateTime timestamp;
        private String status;
        
        public PurchaseRecord(AutoBuyItem item, int quantity, String account) {
            this.item = item;
            this.quantity = quantity;
            this.account = account;
            this.timestamp = LocalDateTime.now();
            this.status = "Completed";
        }
        
        public AutoBuyItem getItem() { return item; }
        public void setItem(AutoBuyItem item) { this.item = item; }
        
        public int getQuantity() { return quantity; }
        public void setQuantity(int quantity) { this.quantity = quantity; }
        
        public String getAccount() { return account; }
        public void setAccount(String account) { this.account = account; }
        
        public LocalDateTime getTimestamp() { return timestamp; }
        public void setTimestamp(LocalDateTime timestamp) { this.timestamp = timestamp; }
        
        public String getStatus() { return status; }
        public void setStatus(String status) { this.status = status; }
        
        public String getFormattedTimestamp() {
            return timestamp.format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss"));
        }
        
        public String getFormattedPrice() {
            return String.format("%,d", item.getPrice() * quantity) + " coins";
        }
        
        public String getItemDisplay() {
            return item.getItemType().getEmoji() + " " + item.getDisplayName();
        }
    }
}