package com.example.minecraftcheat;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.util.math.MatrixStack;

public class CheatHUD extends DrawableHelper {
    private static final MinecraftClient mc = MinecraftClient.getInstance();
    
    public static void render(MatrixStack matrices, float tickDelta) {
        if (mc.player == null || mc.world == null) return;
        
        int y = 10;
        int color = 0xFFFFFF;
        
        // Render cheat status
        if (MinecraftCheatMod.config.flyEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "Fly: ON", 10, y, 0x00FF00);
            y += 12;
        }
        
        if (MinecraftCheatMod.config.speedEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "Speed: ON (" + MinecraftCheatMod.config.speedMultiplier + "x)", 10, y, 0x00FF00);
            y += 12;
        }
        
        if (MinecraftCheatMod.config.xrayEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "X-Ray: ON", 10, y, 0x00FF00);
            y += 12;
        }
        
        if (MinecraftCheatMod.config.noFallEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "No Fall: ON", 10, y, 0x00FF00);
            y += 12;
        }
        
        if (MinecraftCheatMod.config.autoSprintEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "Auto Sprint: ON", 10, y, 0x00FF00);
            y += 12;
        }
        
        if (MinecraftCheatMod.config.killAuraEnabled) {
            mc.textRenderer.drawWithShadow(matrices, "Kill Aura: ON", 10, y, 0x00FF00);
            y += 12;
        }
    }
}