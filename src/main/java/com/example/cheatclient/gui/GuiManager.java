package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.Text;

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
    
    public void render(MatrixStack matrices, float partialTicks) {
        if (!guiOpen) return;
        
        MinecraftClient mc = CheatClient.mc;
        if (mc == null) return;
        
        // Render background
        DrawableHelper.fill(matrices, 10, 10, 200, 300, 0x80000000);
        
        // Render title
        mc.textRenderer.drawWithShadow(matrices, "CheatClient v" + CheatClient.VERSION, 15, 15, 0xFFFFFF);
        
        // Render categories
        int y = 35;
        for (int i = 0; i < categories.length; i++) {
            Module.Category category = categories[i];
            int color = (i == selectedCategory) ? 0x00FF00 : 0xFFFFFF;
            mc.textRenderer.drawWithShadow(matrices, category.getDisplayName(), 15, y, color);
            y += 12;
        }
        
        // Render modules for selected category
        List<Module> modules = CheatClient.INSTANCE.getModuleManager().getModulesByCategory(categories[selectedCategory]);
        y = 35;
        for (int i = 0; i < modules.size(); i++) {
            Module module = modules.get(i);
            int color = module.isEnabled() ? 0x00FF00 : 0xFFFFFF;
            if (i == selectedModule) {
                color = 0xFF0000;
            }
            mc.textRenderer.drawWithShadow(matrices, 
                (module.isEnabled() ? "§a" : "§f") + module.getName(), 120, y, color);
            y += 12;
        }
        
        // Render instructions
        mc.textRenderer.drawWithShadow(matrices, "WASD: Navigate", 15, 280, 0xAAAAAA);
        mc.textRenderer.drawWithShadow(matrices, "Enter: Toggle", 15, 290, 0xAAAAAA);
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