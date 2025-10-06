package com.example.nimbusware.core;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class KeyManager {
    private static KeyManager instance;
    private NimbusWare client;
    private Map<Integer, KeyAction> keyBindings = new ConcurrentHashMap<>();
    private Map<Integer, Long> lastKeyPress = new HashMap<>();
    private long keyPressDelay = 100; // 100ms delay between key presses
    
    // Key codes
    public static final int KEY_RIGHT_SHIFT = 16; // Right Shift key
    public static final int KEY_LEFT_SHIFT = 17;  // Left Shift key (different code)
    public static final int KEY_ESCAPE = 27;      // Escape key
    public static final int KEY_ENTER = 13;       // Enter key
    public static final int KEY_SPACE = 32;       // Space key
    public static final int KEY_TAB = 9;          // Tab key
    public static final int KEY_F1 = 112;         // F1 key
    public static final int KEY_F2 = 113;         // F2 key
    public static final int KEY_F3 = 114;         // F3 key
    public static final int KEY_F4 = 115;         // F4 key
    public static final int KEY_F5 = 116;         // F5 key
    public static final int KEY_F6 = 117;         // F6 key
    public static final int KEY_F7 = 118;         // F7 key
    public static final int KEY_F8 = 119;         // F8 key
    public static final int KEY_F9 = 120;         // F9 key
    public static final int KEY_F10 = 121;        // F10 key
    public static final int KEY_F11 = 122;        // F11 key
    public static final int KEY_F12 = 123;        // F12 key
    
    // Arrow keys
    public static final int KEY_UP = 38;          // Up arrow
    public static final int KEY_DOWN = 40;        // Down arrow
    public static final int KEY_LEFT = 37;        // Left arrow
    public static final int KEY_RIGHT = 39;       // Right arrow
    
    // Number keys
    public static final int KEY_0 = 48;           // 0 key
    public static final int KEY_1 = 49;           // 1 key
    public static final int KEY_2 = 50;           // 2 key
    public static final int KEY_3 = 51;           // 3 key
    public static final int KEY_4 = 52;           // 4 key
    public static final int KEY_5 = 53;           // 5 key
    public static final int KEY_6 = 54;           // 6 key
    public static final int KEY_7 = 55;           // 7 key
    public static final int KEY_8 = 56;           // 8 key
    public static final int KEY_9 = 57;           // 9 key
    
    // Letter keys
    public static final int KEY_A = 65;           // A key
    public static final int KEY_B = 66;           // B key
    public static final int KEY_C = 67;           // C key
    public static final int KEY_D = 68;           // D key
    public static final int KEY_E = 69;           // E key
    public static final int KEY_F = 70;           // F key
    public static final int KEY_G = 71;           // G key
    public static final int KEY_H = 72;           // H key
    public static final int KEY_I = 73;           // I key
    public static final int KEY_J = 74;           // J key
    public static final int KEY_K = 75;           // K key
    public static final int KEY_L = 76;           // L key
    public static final int KEY_M = 77;           // M key
    public static final int KEY_N = 78;           // N key
    public static final int KEY_O = 79;           // O key
    public static final int KEY_P = 80;           // P key
    public static final int KEY_Q = 81;           // Q key
    public static final int KEY_R = 82;           // R key
    public static final int KEY_S = 83;           // S key
    public static final int KEY_T = 84;           // T key
    public static final int KEY_U = 85;           // U key
    public static final int KEY_V = 86;           // V key
    public static final int KEY_W = 87;           // W key
    public static final int KEY_X = 88;           // X key
    public static final int KEY_Y = 89;           // Y key
    public static final int KEY_Z = 90;           // Z key
    
    private KeyManager(NimbusWare client) {
        this.client = client;
        initializeDefaultBindings();
    }
    
    public static KeyManager getInstance(NimbusWare client) {
        if (instance == null) {
            instance = new KeyManager(client);
        }
        return instance;
    }
    
    private void initializeDefaultBindings() {
        // Right Shift - Open main menu
        registerKeyBinding(KEY_RIGHT_SHIFT, "Main Menu", () -> {
            Logger.info("Opening main menu...");
            client.getMainMenu().open();
        });
        
        
        // Escape - Close menus
        registerKeyBinding(KEY_ESCAPE, "Close Menu", () -> {
            Logger.info("Closing menu...");
            closeMenus();
        });
        
        // Number keys for quick module toggles
        registerKeyBinding(KEY_1, "Toggle Module 1", () -> toggleModuleByIndex(0));
        registerKeyBinding(KEY_2, "Toggle Module 2", () -> toggleModuleByIndex(1));
        registerKeyBinding(KEY_3, "Toggle Module 3", () -> toggleModuleByIndex(2));
        registerKeyBinding(KEY_4, "Toggle Module 4", () -> toggleModuleByIndex(3));
        registerKeyBinding(KEY_5, "Toggle Module 5", () -> toggleModuleByIndex(4));
        registerKeyBinding(KEY_6, "Toggle Module 6", () -> toggleModuleByIndex(5));
        registerKeyBinding(KEY_7, "Toggle Module 7", () -> toggleModuleByIndex(6));
        registerKeyBinding(KEY_8, "Toggle Module 8", () -> toggleModuleByIndex(7));
        registerKeyBinding(KEY_9, "Toggle Module 9", () -> toggleModuleByIndex(8));
        registerKeyBinding(KEY_0, "Toggle Module 10", () -> toggleModuleByIndex(9));
    }
    
    public void registerKeyBinding(int keyCode, String description, KeyAction action) {
        keyBindings.put(keyCode, action);
        Logger.debug("Registered key binding: " + getKeyName(keyCode) + " -> " + description);
    }
    
    public void unregisterKeyBinding(int keyCode) {
        keyBindings.remove(keyCode);
        Logger.debug("Unregistered key binding: " + getKeyName(keyCode));
    }
    
    public void onKeyPress(int keyCode) {
        long currentTime = System.currentTimeMillis();
        
        // Check for key press delay
        if (lastKeyPress.containsKey(keyCode)) {
            long lastPress = lastKeyPress.get(keyCode);
            if (currentTime - lastPress < keyPressDelay) {
                return; // Ignore rapid key presses
            }
        }
        
        lastKeyPress.put(keyCode, currentTime);
        
        // Check if BindManager is handling this key
        if (client.getBindManager() != null && client.getBindManager().isInBindingMode()) {
            client.getBindManager().onKeyPress(keyCode);
            return;
        }
        
        // Check for custom binds first
        if (client.getBindManager() != null) {
            client.getBindManager().onKeyPress(keyCode);
        }
        
        // Then check default key bindings
        KeyAction action = keyBindings.get(keyCode);
        if (action != null) {
            try {
                action.execute();
            } catch (Exception e) {
                Logger.error("Error executing key action for " + getKeyName(keyCode) + ": " + e.getMessage());
            }
        }
    }
    
    public void onKeyRelease(int keyCode) {
        // Handle key release if needed
        Logger.debug("Key released: " + getKeyName(keyCode));
    }
    
    public String getKeyName(int keyCode) {
        switch (keyCode) {
            case KEY_RIGHT_SHIFT: return "Right Shift";
            case KEY_LEFT_SHIFT: return "Left Shift";
            case KEY_ESCAPE: return "Escape";
            case KEY_ENTER: return "Enter";
            case KEY_SPACE: return "Space";
            case KEY_TAB: return "Tab";
            case KEY_F1: return "F1";
            case KEY_F2: return "F2";
            case KEY_F3: return "F3";
            case KEY_F4: return "F4";
            case KEY_F5: return "F5";
            case KEY_F6: return "F6";
            case KEY_F7: return "F7";
            case KEY_F8: return "F8";
            case KEY_F9: return "F9";
            case KEY_F10: return "F10";
            case KEY_F11: return "F11";
            case KEY_F12: return "F12";
            case KEY_UP: return "Up Arrow";
            case KEY_DOWN: return "Down Arrow";
            case KEY_LEFT: return "Left Arrow";
            case KEY_RIGHT: return "Right Arrow";
            case KEY_0: return "0";
            case KEY_1: return "1";
            case KEY_2: return "2";
            case KEY_3: return "3";
            case KEY_4: return "4";
            case KEY_5: return "5";
            case KEY_6: return "6";
            case KEY_7: return "7";
            case KEY_8: return "8";
            case KEY_9: return "9";
            case KEY_A: return "A";
            case KEY_B: return "B";
            case KEY_C: return "C";
            case KEY_D: return "D";
            case KEY_E: return "E";
            case KEY_F: return "F";
            case KEY_G: return "G";
            case KEY_H: return "H";
            case KEY_I: return "I";
            case KEY_J: return "J";
            case KEY_K: return "K";
            case KEY_L: return "L";
            case KEY_M: return "M";
            case KEY_N: return "N";
            case KEY_O: return "O";
            case KEY_P: return "P";
            case KEY_Q: return "Q";
            case KEY_R: return "R";
            case KEY_S: return "S";
            case KEY_T: return "T";
            case KEY_U: return "U";
            case KEY_V: return "V";
            case KEY_W: return "W";
            case KEY_X: return "X";
            case KEY_Y: return "Y";
            case KEY_Z: return "Z";
            default: return "Key " + keyCode;
        }
    }
    
    private void listModules() {
        Logger.info("=== Available Modules ===");
        for (Module module : client.getModuleManager().getModules()) {
            String status = module.isEnabled() ? "§aEnabled" : "§cDisabled";
            Logger.info(module.getName() + " - " + status + " §f(" + module.getDescription() + ")");
        }
        Logger.info("=========================");
    }
    
    private void openAutoBuySettings() {
        Module autoBuyModule = client.getModuleManager().getModule("AutoBuy");
        if (autoBuyModule != null) {
            // Open AutoBuy settings
            Logger.info("AutoBuy settings opened");
        } else {
            Logger.warn("AutoBuy module not found");
        }
    }
    
    private void showStatistics() {
        Logger.info("=== NimbusWare Statistics ===");
        Logger.info("Modules: " + client.getModuleManager().getEnabledModules().size() + "/" + client.getModuleManager().getModules().size() + " enabled");
        Logger.info("Uptime: " + getUptime());
        Logger.info("Memory Usage: " + getMemoryUsage());
        Logger.info("=============================");
    }
    
    private void showHelp() {
        Logger.info("=== NimbusWare Key Bindings ===");
        Logger.info("Right Shift - Open Main Menu");
        Logger.info("F1 - Toggle GUI");
        Logger.info("F2 - List Modules");
        Logger.info("F3 - AutoBuy Settings");
        Logger.info("F4 - Account Manager");
        Logger.info("F5 - Statistics");
        Logger.info("F6 - Help");
        Logger.info("Escape - Close Menus");
        Logger.info("1-0 - Quick Module Toggle");
        Logger.info("==============================");
    }
    
    private void closeMenus() {
        // Close any open menus
        Logger.info("Closing all menus...");
    }
    
    private void toggleModuleByIndex(int index) {
        java.util.List<Module> modules = client.getModuleManager().getModules();
        if (index >= 0 && index < modules.size()) {
            Module module = modules.get(index);
            module.toggle();
            Logger.info("Toggled " + module.getName() + " - " + (module.isEnabled() ? "Enabled" : "Disabled"));
        }
    }
    
    private String getUptime() {
        long uptime = System.currentTimeMillis() - client.getStartTime();
        long seconds = uptime / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return hours + "h " + (minutes % 60) + "m " + (seconds % 60) + "s";
        } else if (minutes > 0) {
            return minutes + "m " + (seconds % 60) + "s";
        } else {
            return seconds + "s";
        }
    }
    
    private String getMemoryUsage() {
        Runtime runtime = Runtime.getRuntime();
        long totalMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();
        long usedMemory = totalMemory - freeMemory;
        
        return formatBytes(usedMemory) + " / " + formatBytes(totalMemory);
    }
    
    private String formatBytes(long bytes) {
        if (bytes < 1024) return bytes + " B";
        if (bytes < 1024 * 1024) return String.format("%.1f KB", bytes / 1024.0);
        if (bytes < 1024 * 1024 * 1024) return String.format("%.1f MB", bytes / (1024.0 * 1024.0));
        return String.format("%.1f GB", bytes / (1024.0 * 1024.0 * 1024.0));
    }
    
    public void setKeyPressDelay(long delay) {
        this.keyPressDelay = Math.max(10, delay);
    }
    
    public long getKeyPressDelay() {
        return keyPressDelay;
    }
    
    public Map<Integer, KeyAction> getKeyBindings() {
        return new HashMap<>(keyBindings);
    }
    
    // Inner interface for key actions
    @FunctionalInterface
    public interface KeyAction {
        void execute();
    }
}