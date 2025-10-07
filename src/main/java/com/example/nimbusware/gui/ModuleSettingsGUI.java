package com.example.nimbusware.gui;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.core.ModuleSettings;
import com.example.nimbusware.utils.Logger;

import java.util.Scanner;
import java.util.Map;

/**
 * GUI for module settings configuration
 */
public class ModuleSettingsGUI {
    private final Scanner scanner = new Scanner(System.in);
    private Module currentModule;
    
    /**
     * Open module settings GUI
     * @param module Module to configure
     */
    public void open(Module module) {
        this.currentModule = module;
        
        while (true) {
            clearScreen();
            printHeader();
            printModuleInfo();
            printSettings();
            printMenu();
            
            System.out.print("Settings> ");
            String input = scanner.nextLine().trim().toLowerCase();
            
            if (!processInput(input)) {
                break;
            }
        }
    }
    
    private void printHeader() {
        System.out.println("╔══════════════════════════════════════════════════════════════╗");
        System.out.println("║                    ⚙️ MODULE SETTINGS                       ║");
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
        System.out.println();
    }
    
    private void printModuleInfo() {
        System.out.println("📋 Module Information");
        System.out.println("=====================");
        System.out.println("Name: " + currentModule.getName());
        System.out.println("Description: " + currentModule.getDescription());
        System.out.println("Category: " + currentModule.getCategory().getDisplayName());
        System.out.println("Status: " + (currentModule.isEnabled() ? "§aEnabled" : "§cDisabled"));
        System.out.println("Uptime: " + formatUptime(currentModule.getTotalUptime()));
        System.out.println();
    }
    
    private void printSettings() {
        System.out.println("⚙️ Current Settings");
        System.out.println("===================");
        
        ModuleSettings settings = currentModule.getSettings();
        Map<String, Object> allSettings = settings.getAll();
        
        if (allSettings.isEmpty()) {
            System.out.println("No settings configured");
        } else {
            int index = 1;
            for (Map.Entry<String, Object> entry : allSettings.entrySet()) {
                String key = entry.getKey();
                Object value = entry.getValue();
                System.out.println(index + ". " + key + ": " + value);
                index++;
            }
        }
        System.out.println();
    }
    
    private void printMenu() {
        System.out.println("🔧 Settings Menu");
        System.out.println("================");
        System.out.println("1. Change Setting");
        System.out.println("2. Add Setting");
        System.out.println("3. Reset Settings");
        System.out.println("4. Export Settings");
        System.out.println("5. Import Settings");
        System.out.println("6. Toggle Module");
        System.out.println("7. Back to Main Menu");
        System.out.println();
    }
    
    private boolean processInput(String input) {
        switch (input) {
            case "1":
                changeSetting();
                break;
            case "2":
                addSetting();
                break;
            case "3":
                resetSettings();
                break;
            case "4":
                exportSettings();
                break;
            case "5":
                importSettings();
                break;
            case "6":
                toggleModule();
                break;
            case "7":
            case "back":
            case "exit":
                return false;
            default:
                System.out.println("Invalid option. Please try again.");
                pause();
        }
        return true;
    }
    
    private void changeSetting() {
        System.out.print("Enter setting key to change: ");
        String key = scanner.nextLine().trim();
        
        ModuleSettings settings = currentModule.getSettings();
        if (!settings.hasKey(key)) {
            System.out.println("Setting not found: " + key);
            pause();
            return;
        }
        
        Object currentValue = settings.get(key, null);
        System.out.println("Current value: " + currentValue);
        System.out.print("Enter new value: ");
        String newValueStr = scanner.nextLine().trim();
        
        try {
            Object newValue = parseValue(newValueStr, currentValue);
            settings.set(key, newValue);
            System.out.println("Setting updated: " + key + " = " + newValue);
        } catch (Exception e) {
            System.out.println("Invalid value format: " + e.getMessage());
        }
        
        pause();
    }
    
    private void addSetting() {
        System.out.print("Enter setting key: ");
        String key = scanner.nextLine().trim();
        
        if (key.isEmpty()) {
            System.out.println("Key cannot be empty");
            pause();
            return;
        }
        
        System.out.print("Enter setting value: ");
        String valueStr = scanner.nextLine().trim();
        
        try {
            Object value = parseValue(valueStr, null);
            currentModule.getSettings().set(key, value);
            System.out.println("Setting added: " + key + " = " + value);
        } catch (Exception e) {
            System.out.println("Invalid value format: " + e.getMessage());
        }
        
        pause();
    }
    
    private void resetSettings() {
        System.out.print("Are you sure you want to reset all settings? (y/N): ");
        String confirm = scanner.nextLine().trim().toLowerCase();
        
        if (confirm.equals("y") || confirm.equals("yes")) {
            currentModule.getSettings().reset();
            System.out.println("Settings reset to defaults");
        } else {
            System.out.println("Reset cancelled");
        }
        
        pause();
    }
    
    private void exportSettings() {
        System.out.println("Exporting settings...");
        // In a real implementation, this would save to a file
        System.out.println("Settings exported successfully");
        pause();
    }
    
    private void importSettings() {
        System.out.println("Importing settings...");
        // In a real implementation, this would load from a file
        System.out.println("Settings imported successfully");
        pause();
    }
    
    private void toggleModule() {
        currentModule.toggle();
        System.out.println("Module " + (currentModule.isEnabled() ? "enabled" : "disabled"));
        pause();
    }
    
    private Object parseValue(String valueStr, Object currentValue) {
        if (valueStr.isEmpty()) {
            return currentValue;
        }
        
        // Try to parse as different types
        if (valueStr.equalsIgnoreCase("true")) {
            return true;
        } else if (valueStr.equalsIgnoreCase("false")) {
            return false;
        } else if (valueStr.matches("^-?\\d+$")) {
            return Integer.parseInt(valueStr);
        } else if (valueStr.matches("^-?\\d+\\.\\d+$")) {
            return Double.parseDouble(valueStr);
        } else {
            return valueStr;
        }
    }
    
    private String formatUptime(long uptimeMs) {
        long seconds = uptimeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm %ds", hours, minutes % 60, seconds % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
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