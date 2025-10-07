package com.example.nimbusware.profiles;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.utils.Logger;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Profile manager for saving and loading cheat configurations
 */
public class ProfileManager {
    private static volatile ProfileManager instance;
    private final Map<String, CheatProfile> profiles = new ConcurrentHashMap<>();
    private final File profilesDirectory;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(1, r -> {
        Thread t = new Thread(r, "ProfileManager-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private ProfileManager() {
        this.profilesDirectory = new File("profiles");
        
        if (!profilesDirectory.exists()) {
            profilesDirectory.mkdirs();
        }
        
        loadAllProfiles();
        startAutoSave();
    }
    
    public static ProfileManager getInstance() {
        if (instance == null) {
            synchronized (ProfileManager.class) {
                if (instance == null) {
                    instance = new ProfileManager();
                }
            }
        }
        return instance;
    }
    
    /**
     * Create a new profile
     * @param name Profile name
     * @param description Profile description
     * @return Created profile
     */
    public CheatProfile createProfile(String name, String description) {
        if (profiles.containsKey(name)) {
            throw new IllegalArgumentException("Profile with name '" + name + "' already exists");
        }
        
        CheatProfile profile = new CheatProfile(name, description);
        profiles.put(name, profile);
        
        Logger.info("Created profile: " + name);
        return profile;
    }
    
    /**
     * Save current state to profile
     * @param profileName Profile name
     * @return true if saved successfully
     */
    public boolean saveToProfile(String profileName) {
        CheatProfile profile = profiles.get(profileName);
        if (profile == null) {
            Logger.warn("Profile not found: " + profileName);
            return false;
        }
        
        try {
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            profile.saveFromCurrent(moduleManager);
            saveProfileToFile(profile);
            return true;
        } catch (Exception e) {
            Logger.error("Failed to save profile: " + profileName, e);
            return false;
        }
    }
    
    /**
     * Load profile to current state
     * @param profileName Profile name
     * @return true if loaded successfully
     */
    public boolean loadProfile(String profileName) {
        CheatProfile profile = profiles.get(profileName);
        if (profile == null) {
            Logger.warn("Profile not found: " + profileName);
            return false;
        }
        
        try {
            ModuleManager moduleManager = NimbusWare.INSTANCE.getModuleManager();
            profile.loadToCurrent(moduleManager);
            return true;
        } catch (Exception e) {
            Logger.error("Failed to load profile: " + profileName, e);
            return false;
        }
    }
    
    /**
     * Delete a profile
     * @param profileName Profile name
     * @return true if deleted successfully
     */
    public boolean deleteProfile(String profileName) {
        CheatProfile profile = profiles.remove(profileName);
        if (profile == null) {
            Logger.warn("Profile not found: " + profileName);
            return false;
        }
        
        // Delete profile file
        File profileFile = new File(profilesDirectory, profileName + ".json");
        if (profileFile.exists()) {
            profileFile.delete();
        }
        
        Logger.info("Deleted profile: " + profileName);
        return true;
    }
    
    /**
     * Get a profile by name
     * @param profileName Profile name
     * @return Profile or null
     */
    public CheatProfile getProfile(String profileName) {
        return profiles.get(profileName);
    }
    
    /**
     * Get all profiles
     * @return Map of profile names to profiles
     */
    public Map<String, CheatProfile> getAllProfiles() {
        return new HashMap<>(profiles);
    }
    
    /**
     * Create default profiles
     */
    public void createDefaultProfiles() {
        // PvP Profile
        CheatProfile pvpProfile = createProfile("PvP", "Aggressive PvP configuration");
        pvpProfile.setModuleState("KillAura", true);
        pvpProfile.setModuleState("AutoClicker", true);
        pvpProfile.setModuleState("Sprint", true);
        pvpProfile.setModuleState("WaterSpeed", true);
        pvpProfile.setGlobalSetting("combat.aggressive", true);
        
        // PvE Profile
        CheatProfile pveProfile = createProfile("PvE", "PvE and farming configuration");
        pveProfile.setModuleState("AutoFarm", true);
        pveProfile.setModuleState("AutoMine", true);
        pveProfile.setModuleState("AutoFish", true);
        pveProfile.setModuleState("AutoEat", true);
        pveProfile.setGlobalSetting("farming.efficient", true);
        
        // Stealth Profile
        CheatProfile stealthProfile = createProfile("Stealth", "Stealth configuration for detection avoidance");
        stealthProfile.setModuleState("Sprint", true);
        stealthProfile.setModuleState("AutoEat", true);
        stealthProfile.setGlobalSetting("stealth.enabled", true);
        stealthProfile.setGlobalSetting("detection.avoid", true);
        
        // Money Making Profile
        CheatProfile moneyProfile = createProfile("Money", "Money making configuration");
        moneyProfile.setModuleState("SwordCraft", true);
        moneyProfile.setModuleState("ChestStealer", true);
        moneyProfile.setModuleState("AutoBuy", true);
        moneyProfile.setGlobalSetting("money.optimized", true);
        
        Logger.info("Created " + profiles.size() + " default profiles");
    }
    
    private void loadAllProfiles() {
        File[] profileFiles = profilesDirectory.listFiles((dir, name) -> name.endsWith(".json"));
        if (profileFiles == null || profileFiles.length == 0) {
            createDefaultProfiles();
            return;
        }
        
        int loadedCount = 0;
        for (File profileFile : profileFiles) {
            try {
                String profileName = profileFile.getName().substring(0, profileFile.getName().lastIndexOf('.'));
                CheatProfile profile = loadProfileFromFile(profileFile);
                if (profile != null) {
                    profiles.put(profileName, profile);
                    loadedCount++;
                }
            } catch (Exception e) {
                Logger.error("Failed to load profile file: " + profileFile.getName(), e);
            }
        }
        
        if (loadedCount == 0) {
            createDefaultProfiles();
        } else {
            Logger.info("Loaded " + loadedCount + " profiles");
        }
    }
    
    private CheatProfile loadProfileFromFile(File profileFile) {
        try (FileReader reader = new FileReader(profileFile)) {
            com.google.gson.Gson gson = new com.google.gson.Gson();
            @SuppressWarnings("unchecked")
            Map<String, Object> data = gson.fromJson(reader, Map.class);
            
            String name = (String) data.get("name");
            String description = (String) data.get("description");
            
            CheatProfile profile = new CheatProfile(name, description);
            profile.importFrom(data);
            
            return profile;
        } catch (IOException e) {
            Logger.error("Failed to read profile file: " + profileFile.getName(), e);
            return null;
        }
    }
    
    private void saveProfileToFile(CheatProfile profile) {
        File profileFile = new File(profilesDirectory, profile.getName() + ".json");
        
        try (FileWriter writer = new FileWriter(profileFile)) {
            com.google.gson.Gson gson = new com.google.gson.GsonBuilder().setPrettyPrinting().create();
            gson.toJson(profile.export(), writer);
        } catch (IOException e) {
            Logger.error("Failed to save profile file: " + profileFile.getName(), e);
        }
    }
    
    private void startAutoSave() {
        // Auto-save profiles every 5 minutes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                for (CheatProfile profile : profiles.values()) {
                    saveProfileToFile(profile);
                }
            } catch (Exception e) {
                Logger.error("Error during auto-save", e);
            }
        }, 5, 5, TimeUnit.MINUTES);
    }
    
    /**
     * Save all profiles
     */
    public void saveAllProfiles() {
        for (CheatProfile profile : profiles.values()) {
            saveProfileToFile(profile);
        }
        Logger.info("Saved all profiles");
    }
    
    /**
     * Shutdown the profile manager
     */
    public void shutdown() {
        saveAllProfiles();
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}