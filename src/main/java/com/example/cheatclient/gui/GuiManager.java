package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;

import java.util.List;

public class GuiManager {
    private boolean guiOpen = false;
    private int selectedCategory = 0;
    private int selectedModule = 0;
    private final Module.Category[] categories = Module.Category.values();
    
    public void toggleGui() {
        guiOpen = !guiOpen;
        if (guiOpen) {
            renderGui();
        }
    }
    
    public boolean isGuiOpen() {
        return guiOpen;
    }
    
    public void renderGui() {
        if (!guiOpen) return;
        
        System.out.println("\n" + "=".repeat(60));
        System.out.println("§6CheatClient §fv" + CheatClient.VERSION);
        System.out.println("§7[Matrix & Funtime Bypass]");
        System.out.println("=".repeat(60));
        
        // Render categories
        System.out.println("\n§eCategories:");
        for (int i = 0; i < categories.length; i++) {
            Module.Category category = categories[i];
            String color = (i == selectedCategory) ? "§a" : "§f";
            System.out.println("  " + color + category.getDisplayName());
        }
        
        // Render modules for selected category
        List<Module> modules = CheatClient.INSTANCE.getModuleManager().getModulesByCategory(categories[selectedCategory]);
        System.out.println("\n§eModules (" + categories[selectedCategory].getDisplayName() + "):");
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            String color = module.isEnabled() ? "§a" : "§f";
            if (i == selectedModule) {
                color = "§c";
            }
            
            // Add bypass indicators
            String bypassInfo = "";
            if (module.getName().contains("KillAura") || module.getName().contains("WaterSpeed") || 
                module.getName().contains("Spider") || module.getName().contains("AutoClicker")) {
                bypassInfo = " §8[Matrix+FT]";
            } else if (module.getName().contains("AutoFish") || module.getName().contains("AutoTool")) {
                bypassInfo = " §8[FT]";
            }
            
            System.out.println("  " + color + module.getName() + bypassInfo);
        }
        
        // Render status info
        int enabledCount = CheatClient.INSTANCE.getModuleManager().getEnabledModules().size();
        System.out.println("\n§7Enabled: §a" + enabledCount);
        
        // Render instructions
        System.out.println("\n§7Controls:");
        System.out.println("  §7WASD: Navigate");
        System.out.println("  §7Enter: Toggle");
        System.out.println("  §7ESC: Close");
        System.out.println("=".repeat(60));
    }
    
    public void handleKeyPress(int key) {
        if (!guiOpen) return;
        
        List<Module> modules = CheatClient.INSTANCE.getModuleManager().getModulesByCategory(categories[selectedCategory]);
        
        switch (key) {
            case 87: // W
                selectedModule = Math.max(0, selectedModule - 1);
                renderGui();
                break;
            case 83: // S
                selectedModule = Math.min(modules.size() - 1, selectedModule + 1);
                renderGui();
                break;
            case 65: // A
                selectedCategory = Math.max(0, selectedCategory - 1);
                selectedModule = 0;
                renderGui();
                break;
            case 68: // D
                selectedCategory = Math.min(categories.length - 1, selectedCategory + 1);
                selectedModule = 0;
                renderGui();
                break;
            case 257: // Enter
                if (selectedModule < modules.size()) {
                    modules.get(selectedModule).toggle();
                    renderGui();
                }
                break;
            case 256: // Escape
                guiOpen = false;
                System.out.println("\n§7GUI closed");
                break;
        }
    }
}