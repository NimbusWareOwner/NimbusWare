package com.example.nimbusware.core;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class BindManager {
    private static BindManager instance;
    private NimbusWare client;
    private Map<Integer, BindAction> bindings = new ConcurrentHashMap<>();
    private Map<String, Integer> moduleKeyBinds = new HashMap<>();
    private boolean isBindingMode = false;
    private String pendingBindModule = null;
    private String pendingBindAction = null;
    
    private BindManager(NimbusWare client) {
        this.client = client;
        initializeDefaultBinds();
    }
    
    public static BindManager getInstance(NimbusWare client) {
        if (instance == null) {
            instance = new BindManager(client);
        }
        return instance;
    }
    
    private void initializeDefaultBinds() {
        // No default keybinds - user must set them manually
        Logger.info("BindManager initialized - No default keybinds set");
    }
    
    public void startBindingMode(String moduleName, String action) {
        isBindingMode = true;
        pendingBindModule = moduleName;
        pendingBindAction = action;
        Logger.info("Binding mode started for " + moduleName + " -> " + action);
        Logger.info("Press any key to bind, or 'ESC' to cancel");
    }
    
    public void cancelBindingMode() {
        isBindingMode = false;
        pendingBindModule = null;
        pendingBindAction = null;
        Logger.info("Binding mode cancelled");
    }
    
    public boolean isInBindingMode() {
        return isBindingMode;
    }
    
    public void onKeyPress(int keyCode) {
        if (isBindingMode) {
            if (keyCode == KeyManager.KEY_ESCAPE) {
                cancelBindingMode();
                return;
            }
            
            // Bind the key
            bindKey(keyCode, pendingBindModule, pendingBindAction);
            cancelBindingMode();
            return;
        }
        
        // Execute bound action
        BindAction action = bindings.get(keyCode);
        if (action != null) {
            try {
                action.execute();
            } catch (Exception e) {
                Logger.error("Error executing bind action: " + e.getMessage());
            }
        }
    }
    
    public void bindKey(int keyCode, String moduleName, String action) {
        String keyName = getKeyName(keyCode);
        
        // Remove existing binding for this key
        unbindKey(keyCode);
        
        // Create new binding
        BindAction bindAction = createBindAction(moduleName, action);
        if (bindAction != null) {
            bindings.put(keyCode, bindAction);
            moduleKeyBinds.put(moduleName, keyCode);
            Logger.info("Bound " + keyName + " to " + moduleName + " -> " + action);
        } else {
            Logger.error("Failed to create bind action for " + moduleName + " -> " + action);
        }
    }
    
    public void unbindKey(int keyCode) {
        BindAction action = bindings.remove(keyCode);
        if (action != null) {
            String keyName = getKeyName(keyCode);
            Logger.info("Unbound " + keyName);
            
            // Remove from module keybinds
            moduleKeyBinds.entrySet().removeIf(entry -> entry.getValue() == keyCode);
        }
    }
    
    public void unbindModule(String moduleName) {
        Integer keyCode = moduleKeyBinds.remove(moduleName);
        if (keyCode != null) {
            bindings.remove(keyCode);
            String keyName = getKeyName(keyCode);
            Logger.info("Unbound " + moduleName + " from " + keyName);
        }
    }
    
    public String getModuleKeyBind(String moduleName) {
        Integer keyCode = moduleKeyBinds.get(moduleName);
        if (keyCode != null) {
            return getKeyName(keyCode);
        }
        return "None";
    }
    
    public int getModuleKeyCode(String moduleName) {
        Integer keyCode = moduleKeyBinds.get(moduleName);
        return keyCode != null ? keyCode : -1;
    }
    
    private BindAction createBindAction(String moduleName, String action) {
        return () -> {
            switch (action.toLowerCase()) {
                case "toggle":
                    Module module = client.getModuleManager().getModule(moduleName);
                    if (module != null) {
                        module.toggle();
                        Logger.info(module.getName() + " " + (module.isEnabled() ? "enabled" : "disabled"));
                    } else {
                        Logger.error("Module not found: " + moduleName);
                    }
                    break;
                    
                case "enable":
                    Module enableModule = client.getModuleManager().getModule(moduleName);
                    if (enableModule != null) {
                        if (!enableModule.isEnabled()) {
                            enableModule.toggle();
                            Logger.info(enableModule.getName() + " enabled");
                        }
                    } else {
                        Logger.error("Module not found: " + moduleName);
                    }
                    break;
                    
                case "disable":
                    Module disableModule = client.getModuleManager().getModule(moduleName);
                    if (disableModule != null) {
                        if (disableModule.isEnabled()) {
                            disableModule.toggle();
                            Logger.info(disableModule.getName() + " disabled");
                        }
                    } else {
                        Logger.error("Module not found: " + moduleName);
                    }
                    break;
                    
                case "menu":
                    client.getMainMenu().open();
                    break;
                    
                case "autobuy":
                    openAutoBuySettings();
                    break;
                    
                case "accountmanager":
                    client.getAccountManager().open();
                    break;
                    
                case "hud":
                    toggleHUD();
                    break;
                    
                case "gui":
                    // Toggle GUI implementation
                    Logger.info("GUI toggled");
                    break;
                    
                case "config":
                    saveConfig();
                    break;
                    
                case "reload":
                    reloadConfig();
                    break;
                    
                default:
                    Logger.warn("Unknown bind action: " + action);
                    break;
            }
        };
    }
    
    private void openAutoBuySettings() {
        Logger.info("Opening AutoBuy settings...");
        // Implementation for AutoBuy settings
    }
    
    private void toggleHUD() {
        if (client.getHUDManager() != null) {
            // Toggle HUD visibility
            Logger.info("HUD toggled");
        }
    }
    
    private void saveConfig() {
        if (client.getConfigManager() != null) {
            client.getConfigManager().saveConfig();
            Logger.info("Configuration saved");
        }
    }
    
    private void reloadConfig() {
        if (client.getConfigManager() != null) {
            client.getConfigManager().loadConfig();
            Logger.info("Configuration reloaded");
        }
    }
    
    private String getKeyName(int keyCode) {
        switch (keyCode) {
            case KeyManager.KEY_RIGHT_SHIFT: return "Right Shift";
            case KeyManager.KEY_LEFT_SHIFT: return "Left Shift";
            case KeyManager.KEY_ESCAPE: return "Escape";
            case KeyManager.KEY_ENTER: return "Enter";
            case KeyManager.KEY_SPACE: return "Space";
            case KeyManager.KEY_TAB: return "Tab";
            case KeyManager.KEY_F1: return "F1";
            case KeyManager.KEY_F2: return "F2";
            case KeyManager.KEY_F3: return "F3";
            case KeyManager.KEY_F4: return "F4";
            case KeyManager.KEY_F5: return "F5";
            case KeyManager.KEY_F6: return "F6";
            case KeyManager.KEY_F7: return "F7";
            case KeyManager.KEY_F8: return "F8";
            case KeyManager.KEY_F9: return "F9";
            case KeyManager.KEY_F10: return "F10";
            case KeyManager.KEY_F11: return "F11";
            case KeyManager.KEY_F12: return "F12";
            case KeyManager.KEY_1: return "1";
            case KeyManager.KEY_2: return "2";
            case KeyManager.KEY_3: return "3";
            case KeyManager.KEY_4: return "4";
            case KeyManager.KEY_5: return "5";
            case KeyManager.KEY_6: return "6";
            case KeyManager.KEY_7: return "7";
            case KeyManager.KEY_8: return "8";
            case KeyManager.KEY_9: return "9";
            case KeyManager.KEY_0: return "0";
            case KeyManager.KEY_A: return "A";
            case KeyManager.KEY_B: return "B";
            case KeyManager.KEY_C: return "C";
            case KeyManager.KEY_D: return "D";
            case KeyManager.KEY_E: return "E";
            case KeyManager.KEY_F: return "F";
            case KeyManager.KEY_G: return "G";
            case KeyManager.KEY_H: return "H";
            case KeyManager.KEY_I: return "I";
            case KeyManager.KEY_J: return "J";
            case KeyManager.KEY_K: return "K";
            case KeyManager.KEY_L: return "L";
            case KeyManager.KEY_M: return "M";
            case KeyManager.KEY_N: return "N";
            case KeyManager.KEY_O: return "O";
            case KeyManager.KEY_P: return "P";
            case KeyManager.KEY_Q: return "Q";
            case KeyManager.KEY_R: return "R";
            case KeyManager.KEY_S: return "S";
            case KeyManager.KEY_T: return "T";
            case KeyManager.KEY_U: return "U";
            case KeyManager.KEY_V: return "V";
            case KeyManager.KEY_W: return "W";
            case KeyManager.KEY_X: return "X";
            case KeyManager.KEY_Y: return "Y";
            case KeyManager.KEY_Z: return "Z";
            default: return "Key " + keyCode;
        }
    }
    
    public void listBinds() {
        Logger.info("=== Current Key Binds ===");
        if (bindings.isEmpty()) {
            Logger.info("No key binds set");
            return;
        }
        
        for (Map.Entry<Integer, BindAction> entry : bindings.entrySet()) {
            String keyName = getKeyName(entry.getKey());
            Logger.info(keyName + " -> " + entry.getValue().getDescription());
        }
        Logger.info("========================");
    }
    
    public void clearAllBinds() {
        bindings.clear();
        moduleKeyBinds.clear();
        Logger.info("All key binds cleared");
    }
    
    @FunctionalInterface
    public interface BindAction {
        void execute();
        
        default String getDescription() {
            return "Custom Action";
        }
    }
}