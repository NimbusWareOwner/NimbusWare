package com.example.minecraftcheat;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

public class Config {
    private static final Gson GSON = new GsonBuilder().setPrettyPrinting().create();
    private static final File CONFIG_FILE = new File(FabricLoader.getInstance().getConfigDir().toFile(), "minecraft-cheat.json");
    
    // Cheat settings
    public boolean flyEnabled = false;
    public boolean speedEnabled = false;
    public boolean xrayEnabled = false;
    public boolean noFallEnabled = false;
    public boolean autoSprintEnabled = false;
    public boolean killAuraEnabled = false;
    
    // Speed multiplier
    public float speedMultiplier = 2.0f;
    
    // Kill aura settings
    public float killAuraRange = 4.0f;
    public int killAuraDelay = 20; // ticks
    
    public static Config load() {
        if (CONFIG_FILE.exists()) {
            try (FileReader reader = new FileReader(CONFIG_FILE)) {
                return GSON.fromJson(reader, Config.class);
            } catch (IOException e) {
                MinecraftCheatMod.LOGGER.error("Failed to load config", e);
            }
        }
        return new Config();
    }
    
    public void save() {
        try (FileWriter writer = new FileWriter(CONFIG_FILE)) {
            GSON.toJson(this, writer);
        } catch (IOException e) {
            MinecraftCheatMod.LOGGER.error("Failed to save config", e);
        }
    }
    
    public void reset() {
        flyEnabled = false;
        speedEnabled = false;
        xrayEnabled = false;
        noFallEnabled = false;
        autoSprintEnabled = false;
        killAuraEnabled = false;
        speedMultiplier = 2.0f;
        killAuraRange = 4.0f;
        killAuraDelay = 20;
    }
}