package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.mock.MockMinecraftClient;

import java.util.List;

public class GuiManager {
    private boolean guiOpen = false;
    private int selectedCategory = 0;
    private int selectedModule = 0;
    private final Module.Category[] categories = Module.Category.values();
    
    public void toggleGui() {
        guiOpen = !guiOpen;
    }
    
    public boolean isGuiOpen() {
        return guiOpen;
    }
    
    public void render(Object matrices, float partialTicks) {
        if (!guiOpen) return;
        
        MockMinecraftClient mc = CheatClient.INSTANCE.mc;
        if (mc == null) return;
        
        // Render background (simplified)
        System.out.println("Rendering GUI background");
        
        // Render title with anti-detection info
        System.out.println("§6CheatClient §fv" + CheatClient.VERSION);
        System.out.println("§7[Matrix & Funtime Bypass]");
        
        // Render categories
        int y = 45;
        for (int i = 0; i < categories.length; i++) {
            Module.Category category = categories[i];
            String color = (i == selectedCategory) ? "§a" : "§f";
            System.out.println(color + category.getDisplayName());
            y += 12;
        }
        
        // Render modules for selected category
        List<Module> modules = CheatClient.INSTANCE.getModuleManager().getModulesByCategory(categories[selectedCategory]);
        y = 45;
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
            
            System.out.println(color + module.getName() + bypassInfo);
            y += 12;
        }
        
        // Render status info
        int enabledCount = CheatClient.INSTANCE.getModuleManager().getEnabledModules().size();
        System.out.println("§7Enabled: §a" + enabledCount);
        
        // Render instructions
        System.out.println("§7WASD: Navigate");
        System.out.println("§7Enter: Toggle");
        System.out.println("§7ESC: Close");
    }
    
    public void handleKeyPress(int key) {
        if (!guiOpen) return;
        
        List<Module> modules = CheatClient.INSTANCE.getModuleManager().getModulesByCategory(categories[selectedCategory]);
        
        switch (key) {
            case 87: // W
                selectedModule = Math.max(0, selectedModule - 1);
                break;
            case 83: // S
                selectedModule = Math.min(modules.size() - 1, selectedModule + 1);
                break;
            case 65: // A
                selectedCategory = Math.max(0, selectedCategory - 1);
                selectedModule = 0;
                break;
            case 68: // D
                selectedCategory = Math.min(categories.length - 1, selectedCategory + 1);
                selectedModule = 0;
                break;
            case 257: // Enter
                if (selectedModule < modules.size()) {
                    modules.get(selectedModule).toggle();
                }
                break;
            case 256: // Escape
                guiOpen = false;
                break;
        }
    }
}