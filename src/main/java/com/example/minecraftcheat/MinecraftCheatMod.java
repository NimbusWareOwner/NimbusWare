package com.example.minecraftcheat;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.rendering.v1.HudRenderCallback;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class MinecraftCheatMod implements ClientModInitializer {
    public static final String MOD_ID = "minecraft-cheat";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);
    
    // Configuration
    public static Config config;
    
    // Key bindings
    public static KeyBinding flyKey;
    public static KeyBinding speedKey;
    public static KeyBinding xrayKey;
    public static KeyBinding noFallKey;
    public static KeyBinding autoSprintKey;
    public static KeyBinding killAuraKey;
    public static KeyBinding configKey;
    
    @Override
    public void onInitializeClient() {
        LOGGER.info("Initializing Minecraft Cheat Mod");
        
        // Load configuration
        config = Config.load();
        
        // Register key bindings
        registerKeyBindings();
        
        // Register tick events
        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if (client.player != null) {
                handleCheats(client);
            }
        });
        
        // Register HUD rendering
        HudRenderCallback.EVENT.register((matrices, tickDelta) -> {
            CheatHUD.render(matrices, tickDelta);
        });
        
        // Register commands
        CheatCommand.register(ClientCommandManager.getDispatcher());
        
        LOGGER.info("Minecraft Cheat Mod initialized successfully!");
    }
    
    private void registerKeyBindings() {
        flyKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.fly",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_F,
            "category.minecraft-cheat.general"
        ));
        
        speedKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.speed",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "category.minecraft-cheat.general"
        ));
        
        xrayKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.xray",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_X,
            "category.minecraft-cheat.general"
        ));
        
        noFallKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.nofall",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_N,
            "category.minecraft-cheat.general"
        ));
        
        autoSprintKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.autosprint",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_R,
            "category.minecraft-cheat.general"
        ));
        
        killAuraKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.killaura",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_K,
            "category.minecraft-cheat.general"
        ));
        
        configKey = KeyBindingHelper.registerKeyBinding(new KeyBinding(
            "key.minecraft-cheat.config",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_C,
            "category.minecraft-cheat.general"
        ));
    }
    
    private void handleCheats(net.minecraft.client.MinecraftClient client) {
        // Handle fly toggle
        if (flyKey.wasPressed()) {
            config.flyEnabled = !config.flyEnabled;
            CheatManager.toggleFly();
        }
        
        // Handle speed toggle
        if (speedKey.wasPressed()) {
            config.speedEnabled = !config.speedEnabled;
            if (config.speedEnabled) {
                CheatManager.setSpeed(config.speedMultiplier);
            } else {
                CheatManager.setSpeed(1.0f);
            }
        }
        
        // Handle xray toggle
        if (xrayKey.wasPressed()) {
            config.xrayEnabled = !config.xrayEnabled;
            LOGGER.info("X-Ray " + (config.xrayEnabled ? "enabled" : "disabled"));
        }
        
        // Handle no fall toggle
        if (noFallKey.wasPressed()) {
            config.noFallEnabled = !config.noFallEnabled;
            LOGGER.info("No Fall " + (config.noFallEnabled ? "enabled" : "disabled"));
        }
        
        // Handle auto sprint toggle
        if (autoSprintKey.wasPressed()) {
            config.autoSprintEnabled = !config.autoSprintEnabled;
            LOGGER.info("Auto Sprint " + (config.autoSprintEnabled ? "enabled" : "disabled"));
        }
        
        // Handle kill aura toggle
        if (killAuraKey.wasPressed()) {
            config.killAuraEnabled = !config.killAuraEnabled;
            LOGGER.info("Kill Aura " + (config.killAuraEnabled ? "enabled" : "disabled"));
        }
        
        // Handle config save
        if (configKey.wasPressed()) {
            config.save();
            LOGGER.info("Configuration saved");
        }
        
        // Apply continuous effects
        if (config.noFallEnabled) {
            CheatManager.toggleNoFall();
        }
        
        if (config.autoSprintEnabled) {
            CheatManager.toggleAutoSprint();
        }
        
        if (config.killAuraEnabled) {
            CheatManager.performKillAura();
        }
    }
}