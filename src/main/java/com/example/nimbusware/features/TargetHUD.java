package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class TargetHUD extends Module {
    // HUD Settings
    private boolean showTargetInfo = true;
    private boolean showTargetHealth = true;
    private boolean showTargetArmor = true;
    private boolean showTargetDistance = true;
    private boolean showTargetDirection = true;
    private boolean showTargetPing = true;
    private boolean showTargetSpeed = true;
    private boolean showTargetGamemode = true;
    private boolean showTargetEffects = true;
    private boolean showTargetItems = true;
    
    // Position Settings
    private HUDPosition position = HUDPosition.TOP_RIGHT;
    private float xOffset = 10.0f;
    private float yOffset = 10.0f;
    private float scale = 1.0f;
    
    // Visual Settings
    private boolean showBackground = true;
    private boolean showBorder = true;
    private String backgroundColor = "§0"; // Black
    private String borderColor = "§7"; // Gray
    private String textColor = "§f"; // White
    private String accentColor = "§a"; // Green
    private String warningColor = "§c"; // Red
    private String infoColor = "§b"; // Aqua
    
    // Background Settings
    private float backgroundAlpha = 0.8f;
    private float borderWidth = 2.0f;
    private float padding = 5.0f;
    private float cornerRadius = 3.0f;
    
    // Animation Settings
    private boolean enableAnimations = true;
    private boolean fadeInOut = true;
    private boolean slideInOut = true;
    private boolean pulseEffect = false;
    private boolean glowEffect = false;
    private float animationSpeed = 1.0f;
    
    // Target Selection
    private boolean autoSelectTarget = true;
    private boolean selectNearestPlayer = true;
    private boolean selectNearestMob = false;
    private boolean selectNearestAnimal = false;
    private float maxSelectionDistance = 50.0f;
    private long selectionCooldown = 1000; // 1 second
    
    // Display Settings
    private boolean showTargetName = true;
    private boolean showTargetUUID = false;
    private boolean showTargetIP = false;
    private boolean showTargetServer = false;
    private boolean showTargetWorld = true;
    private boolean showTargetBiome = true;
    private boolean showTargetYLevel = true;
    private boolean showTargetCoordinates = true;
    
    // Health Display
    private boolean showHealthBar = true;
    private boolean showHealthText = true;
    private boolean showHealthPercentage = true;
    private boolean showMaxHealth = true;
    private String healthBarColor = "§c"; // Red
    private String healthTextColor = "§f"; // White
    
    // Armor Display
    private boolean showArmorBar = true;
    private boolean showArmorText = true;
    private boolean showArmorDurability = true;
    private boolean showArmorEnchants = true;
    private String armorBarColor = "§b"; // Aqua
    private String armorTextColor = "§f"; // White
    
    // Distance Display
    private boolean showDistanceText = true;
    private boolean showDistanceBar = false;
    private boolean showDistanceDirection = true;
    private String distanceColor = "§e"; // Yellow
    
    // Effects Display
    private boolean showActiveEffects = true;
    private boolean showEffectIcons = true;
    private boolean showEffectDuration = true;
    private boolean showEffectAmplifier = true;
    private int maxEffectsDisplayed = 5;
    
    // Items Display
    private boolean showHeldItem = true;
    private boolean showMainHand = true;
    private boolean showOffHand = true;
    private boolean showArmorSlots = true;
    private boolean showItemEnchants = true;
    private boolean showItemDurability = true;
    
    // Anti-Detection
    private boolean useFuntimeBypass = true;
    private boolean useMatrixBypass = true;
    private boolean useHypixelBypass = false;
    private boolean useNCPBypass = false;
    private boolean useAACBypass = false;
    private boolean useGrimBypass = false;
    private boolean useVerusBypass = false;
    private boolean useVulcanBypass = false;
    private boolean useSpartanBypass = false;
    private boolean useIntaveBypass = false;
    
    // Internal
    private Target currentTarget = null;
    private long lastTargetUpdate = 0;
    private long lastSelectionTime = 0;
    private List<Target> availableTargets = new ArrayList<>();
    
    public TargetHUD() {
        super("TargetHUD", "Advanced target information HUD", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("TargetHUD");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("TargetHUD");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.enableHypixelBypass("TargetHUD");
        }
        if (useNCPBypass) {
            AntiDetectionManager.enableNCPBypass("TargetHUD");
        }
        if (useAACBypass) {
            AntiDetectionManager.enableAACBypass("TargetHUD");
        }
        if (useGrimBypass) {
            AntiDetectionManager.enableGrimBypass("TargetHUD");
        }
        if (useVerusBypass) {
            AntiDetectionManager.enableVerusBypass("TargetHUD");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.enableVulcanBypass("TargetHUD");
        }
        if (useSpartanBypass) {
            AntiDetectionManager.enableSpartanBypass("TargetHUD");
        }
        if (useIntaveBypass) {
            AntiDetectionManager.enableIntaveBypass("TargetHUD");
        }
        
        Logger.info("TargetHUD enabled - Advanced target information display active");
    }
    
    @Override
    protected void onDisable() {
        AntiDetectionManager.disableFuntimeBypass("TargetHUD");
        AntiDetectionManager.disableMatrixBypass("TargetHUD");
        AntiDetectionManager.disableHypixelBypass("TargetHUD");
        AntiDetectionManager.disableNCPBypass("TargetHUD");
        AntiDetectionManager.disableAACBypass("TargetHUD");
        AntiDetectionManager.disableGrimBypass("TargetHUD");
        AntiDetectionManager.disableVerusBypass("TargetHUD");
        AntiDetectionManager.disableVulcanBypass("TargetHUD");
        AntiDetectionManager.disableSpartanBypass("TargetHUD");
        AntiDetectionManager.disableIntaveBypass("TargetHUD");
        
        currentTarget = null;
        availableTargets.clear();
        Logger.info("TargetHUD disabled");
    }
    
    public void onTick() {
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastTargetUpdate < 100) { // 10 TPS
            return;
        }
        lastTargetUpdate = currentTime;
        
        updateTargets();
        selectTarget();
    }
    
    public void onRender() {
        if (!isEnabled() || currentTarget == null) return;
        
        renderTargetHUD();
    }
    
    private void updateTargets() {
        availableTargets.clear();
        
        // Simulate finding targets
        availableTargets.add(new Target("Player1", TargetType.PLAYER, 25.5f, 64.0f, 10.2f, 20.0f, 20.0f, 0.8f, "Survival", 50, 5.2f));
        availableTargets.add(new Target("Player2", TargetType.PLAYER, -15.3f, 65.0f, -5.7f, 15.0f, 15.0f, 0.6f, "Creative", 30, 3.1f));
        availableTargets.add(new Target("Zombie", TargetType.MOB, 30.1f, 63.0f, 8.9f, 20.0f, 20.0f, 1.0f, "Survival", 0, 0.0f));
        availableTargets.add(new Target("Skeleton", TargetType.MOB, -20.4f, 64.0f, 12.1f, 20.0f, 20.0f, 1.0f, "Survival", 0, 0.0f));
    }
    
    private void selectTarget() {
        if (!autoSelectTarget) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastSelectionTime < selectionCooldown) {
            return;
        }
        lastSelectionTime = currentTime;
        
        Target nearestTarget = null;
        float nearestDistance = Float.MAX_VALUE;
        
        for (Target target : availableTargets) {
            if (target.distance > maxSelectionDistance) continue;
            
            boolean shouldSelect = false;
            if (selectNearestPlayer && target.type == TargetType.PLAYER) {
                shouldSelect = true;
            } else if (selectNearestMob && target.type == TargetType.MOB) {
                shouldSelect = true;
            } else if (selectNearestAnimal && target.type == TargetType.ANIMAL) {
                shouldSelect = true;
            }
            
            if (shouldSelect && target.distance < nearestDistance) {
                nearestTarget = target;
                nearestDistance = target.distance;
            }
        }
        
        if (nearestTarget != null && nearestTarget != currentTarget) {
            currentTarget = nearestTarget;
            Logger.debug("Selected new target: " + currentTarget.name);
        }
    }
    
    private void renderTargetHUD() {
        if (currentTarget == null) return;
        
        StringBuilder hudText = new StringBuilder();
        
        // Header
        if (showTargetName) {
            hudText.append(accentColor).append("§l").append(currentTarget.name).append("§r\n");
        }
        
        // Basic Info
        if (showTargetInfo) {
            hudText.append(textColor).append("Type: ").append(getTargetTypeString(currentTarget.type)).append("\n");
            if (showTargetGamemode) {
                hudText.append(textColor).append("Gamemode: ").append(currentTarget.gamemode).append("\n");
            }
        }
        
        // Health
        if (showTargetHealth) {
            hudText.append(healthTextColor).append("Health: ");
            if (showHealthText) {
                hudText.append(String.format("%.1f", currentTarget.health));
            }
            if (showMaxHealth) {
                hudText.append("/").append(String.format("%.1f", currentTarget.maxHealth));
            }
            if (showHealthPercentage) {
                float percentage = (currentTarget.health / currentTarget.maxHealth) * 100;
                hudText.append(" (").append(String.format("%.1f", percentage)).append("%)");
            }
            hudText.append("\n");
            
            if (showHealthBar) {
                hudText.append(renderHealthBar(currentTarget.health, currentTarget.maxHealth));
            }
        }
        
        // Armor
        if (showTargetArmor) {
            hudText.append(armorTextColor).append("Armor: ");
            if (showArmorText) {
                hudText.append(String.format("%.1f", currentTarget.armor));
            }
            hudText.append("\n");
            
            if (showArmorBar) {
                hudText.append(renderArmorBar(currentTarget.armor));
            }
        }
        
        // Distance
        if (showTargetDistance) {
            hudText.append(distanceColor).append("Distance: ");
            if (showDistanceText) {
                hudText.append(String.format("%.1fm", currentTarget.distance));
            }
            if (showDistanceDirection) {
                hudText.append(" ").append(getDirection(currentTarget));
            }
            hudText.append("\n");
        }
        
        // Speed
        if (showTargetSpeed) {
            hudText.append(infoColor).append("Speed: ").append(String.format("%.1f m/s", currentTarget.speed)).append("\n");
        }
        
        // Ping
        if (showTargetPing) {
            hudText.append(infoColor).append("Ping: ").append(currentTarget.ping).append("ms\n");
        }
        
        // Location
        if (showTargetCoordinates) {
            hudText.append(textColor).append("Position: ")
                   .append(String.format("%.1f, %.1f, %.1f", currentTarget.x, currentTarget.y, currentTarget.z))
                   .append("\n");
        }
        
        if (showTargetYLevel) {
            hudText.append(textColor).append("Y Level: ").append(String.format("%.0f", currentTarget.y)).append("\n");
        }
        
        if (showTargetBiome) {
            hudText.append(textColor).append("Biome: Plains\n");
        }
        
        if (showTargetWorld) {
            hudText.append(textColor).append("World: Overworld\n");
        }
        
        // Effects
        if (showTargetEffects) {
            hudText.append(textColor).append("Effects: Speed I, Jump Boost II\n");
        }
        
        // Items
        if (showTargetItems) {
            hudText.append(textColor).append("Held: Diamond Sword\n");
            hudText.append(textColor).append("Armor: Iron Helmet, Iron Chestplate\n");
        }
        
        // Render the HUD
        Logger.debug("Target HUD:\n" + hudText.toString());
    }
    
    private String getTargetTypeString(TargetType type) {
        switch (type) {
            case PLAYER: return "§aPlayer";
            case MOB: return "§cMob";
            case ANIMAL: return "§eAnimal";
            default: return "§fUnknown";
        }
    }
    
    private String renderHealthBar(float health, float maxHealth) {
        float percentage = health / maxHealth;
        int bars = (int) (percentage * 20);
        
        StringBuilder bar = new StringBuilder();
        bar.append(healthBarColor);
        
        for (int i = 0; i < 20; i++) {
            if (i < bars) {
                bar.append("█");
            } else {
                bar.append("§7█");
            }
        }
        
        return bar.toString() + "\n";
    }
    
    private String renderArmorBar(float armor) {
        int bars = (int) (armor / 4); // Assuming max armor is 20 (5 pieces * 4 points each)
        
        StringBuilder bar = new StringBuilder();
        bar.append(armorBarColor);
        
        for (int i = 0; i < 5; i++) {
            if (i < bars) {
                bar.append("█");
            } else {
                bar.append("§7█");
            }
        }
        
        return bar.toString() + "\n";
    }
    
    private String getDirection(Target target) {
        if (target.x > 0) return "E";
        if (target.x < 0) return "W";
        if (target.z > 0) return "S";
        if (target.z < 0) return "N";
        return "?";
    }
    
    // Getters and Setters
    public boolean isShowTargetInfo() { return showTargetInfo; }
    public void setShowTargetInfo(boolean showTargetInfo) { this.showTargetInfo = showTargetInfo; }
    
    public boolean isShowTargetHealth() { return showTargetHealth; }
    public void setShowTargetHealth(boolean showTargetHealth) { this.showTargetHealth = showTargetHealth; }
    
    public boolean isShowTargetArmor() { return showTargetArmor; }
    public void setShowTargetArmor(boolean showTargetArmor) { this.showTargetArmor = showTargetArmor; }
    
    public boolean isShowTargetDistance() { return showTargetDistance; }
    public void setShowTargetDistance(boolean showTargetDistance) { this.showTargetDistance = showTargetDistance; }
    
    public boolean isShowTargetDirection() { return showTargetDirection; }
    public void setShowTargetDirection(boolean showTargetDirection) { this.showTargetDirection = showTargetDirection; }
    
    public boolean isShowTargetPing() { return showTargetPing; }
    public void setShowTargetPing(boolean showTargetPing) { this.showTargetPing = showTargetPing; }
    
    public boolean isShowTargetSpeed() { return showTargetSpeed; }
    public void setShowTargetSpeed(boolean showTargetSpeed) { this.showTargetSpeed = showTargetSpeed; }
    
    public boolean isShowTargetGamemode() { return showTargetGamemode; }
    public void setShowTargetGamemode(boolean showTargetGamemode) { this.showTargetGamemode = showTargetGamemode; }
    
    public boolean isShowTargetEffects() { return showTargetEffects; }
    public void setShowTargetEffects(boolean showTargetEffects) { this.showTargetEffects = showTargetEffects; }
    
    public boolean isShowTargetItems() { return showTargetItems; }
    public void setShowTargetItems(boolean showTargetItems) { this.showTargetItems = showTargetItems; }
    
    public HUDPosition getPosition() { return position; }
    public void setPosition(HUDPosition position) { this.position = position; }
    
    public float getXOffset() { return xOffset; }
    public void setXOffset(float xOffset) { this.xOffset = xOffset; }
    
    public float getYOffset() { return yOffset; }
    public void setYOffset(float yOffset) { this.yOffset = yOffset; }
    
    public float getScale() { return scale; }
    public void setScale(float scale) { this.scale = scale; }
    
    public boolean isShowBackground() { return showBackground; }
    public void setShowBackground(boolean showBackground) { this.showBackground = showBackground; }
    
    public boolean isShowBorder() { return showBorder; }
    public void setShowBorder(boolean showBorder) { this.showBorder = showBorder; }
    
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    
    public String getBorderColor() { return borderColor; }
    public void setBorderColor(String borderColor) { this.borderColor = borderColor; }
    
    public String getTextColor() { return textColor; }
    public void setTextColor(String textColor) { this.textColor = textColor; }
    
    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }
    
    public String getWarningColor() { return warningColor; }
    public void setWarningColor(String warningColor) { this.warningColor = warningColor; }
    
    public String getInfoColor() { return infoColor; }
    public void setInfoColor(String infoColor) { this.infoColor = infoColor; }
    
    public boolean isAutoSelectTarget() { return autoSelectTarget; }
    public void setAutoSelectTarget(boolean autoSelectTarget) { this.autoSelectTarget = autoSelectTarget; }
    
    public boolean isSelectNearestPlayer() { return selectNearestPlayer; }
    public void setSelectNearestPlayer(boolean selectNearestPlayer) { this.selectNearestPlayer = selectNearestPlayer; }
    
    public boolean isSelectNearestMob() { return selectNearestMob; }
    public void setSelectNearestMob(boolean selectNearestMob) { this.selectNearestMob = selectNearestMob; }
    
    public boolean isSelectNearestAnimal() { return selectNearestAnimal; }
    public void setSelectNearestAnimal(boolean selectNearestAnimal) { this.selectNearestAnimal = selectNearestAnimal; }
    
    public float getMaxSelectionDistance() { return maxSelectionDistance; }
    public void setMaxSelectionDistance(float maxSelectionDistance) { this.maxSelectionDistance = maxSelectionDistance; }
    
    // Anti-Detection Getters and Setters
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isUseMatrixBypass() { return useMatrixBypass; }
    public void setUseMatrixBypass(boolean useMatrixBypass) { this.useMatrixBypass = useMatrixBypass; }
    
    public boolean isUseHypixelBypass() { return useHypixelBypass; }
    public void setUseHypixelBypass(boolean useHypixelBypass) { this.useHypixelBypass = useHypixelBypass; }
    
    public boolean isUseNCPBypass() { return useNCPBypass; }
    public void setUseNCPBypass(boolean useNCPBypass) { this.useNCPBypass = useNCPBypass; }
    
    public boolean isUseAACBypass() { return useAACBypass; }
    public void setUseAACBypass(boolean useAACBypass) { this.useAACBypass = useAACBypass; }
    
    public boolean isUseGrimBypass() { return useGrimBypass; }
    public void setUseGrimBypass(boolean useGrimBypass) { this.useGrimBypass = useGrimBypass; }
    
    public boolean isUseVerusBypass() { return useVerusBypass; }
    public void setUseVerusBypass(boolean useVerusBypass) { this.useVerusBypass = useVerusBypass; }
    
    public boolean isUseVulcanBypass() { return useVulcanBypass; }
    public void setUseVulcanBypass(boolean useVulcanBypass) { this.useVulcanBypass = useVulcanBypass; }
    
    public boolean isUseSpartanBypass() { return useSpartanBypass; }
    public void setUseSpartanBypass(boolean useSpartanBypass) { this.useSpartanBypass = useSpartanBypass; }
    
    public boolean isUseIntaveBypass() { return useIntaveBypass; }
    public void setUseIntaveBypass(boolean useIntaveBypass) { this.useIntaveBypass = useIntaveBypass; }
    
    // Inner Classes
    private static class Target {
        String name;
        TargetType type;
        float x, y, z;
        float distance;
        float health;
        float maxHealth;
        float armor;
        String gamemode;
        int ping;
        float speed;
        
        Target(String name, TargetType type, float x, float y, float z, float distance, 
               float health, float armor, String gamemode, int ping, float speed) {
            this.name = name;
            this.type = type;
            this.x = x;
            this.y = y;
            this.z = z;
            this.distance = distance;
            this.health = health;
            this.maxHealth = health;
            this.armor = armor;
            this.gamemode = gamemode;
            this.ping = ping;
            this.speed = speed;
        }
    }
    
    public enum TargetType {
        PLAYER, MOB, ANIMAL, ITEM, CHEST, END_CRYSTAL
    }
    
    public enum HUDPosition {
        TOP_LEFT, TOP_RIGHT, BOTTOM_LEFT, BOTTOM_RIGHT, CENTER, CUSTOM
    }
}