package com.example.nimbusware.gui;

import com.example.nimbusware.utils.Logger;

import java.util.*;

public class VisualSettingsManager {
    private static VisualSettingsManager instance;
    
    // Visual themes
    private String currentTheme = "Default";
    private Map<String, VisualTheme> themes = new HashMap<>();
    
    // Color schemes
    private String primaryColor = "§a"; // Green
    private String secondaryColor = "§7"; // Gray
    private String accentColor = "§b"; // Aqua
    private String warningColor = "§e"; // Yellow
    private String errorColor = "§c"; // Red
    private String successColor = "§a"; // Green
    private String infoColor = "§b"; // Aqua
    
    // Display settings
    private boolean showAnimations = true;
    private boolean showParticles = true;
    private boolean showEffects = true;
    private boolean showNotifications = true;
    private boolean showTooltips = true;
    private boolean showBorders = true;
    private boolean showShadows = true;
    private boolean showGradients = true;
    
    // Font settings
    private int fontSize = 12;
    private String fontFamily = "Minecraft";
    private boolean boldText = false;
    private boolean italicText = false;
    private boolean underlinedText = false;
    
    // Layout settings
    private int guiScale = 1;
    private boolean compactMode = false;
    private boolean showIcons = true;
    private boolean showDescriptions = true;
    private boolean showCategories = true;
    private boolean showStatus = true;
    
    // Animation settings
    private int animationSpeed = 100; // milliseconds
    private boolean smoothAnimations = true;
    private boolean fadeEffects = true;
    private boolean slideEffects = true;
    private boolean bounceEffects = false;
    
    // Particle settings
    private boolean showParticleTrails = true;
    private int particleDensity = 50; // 0-100
    private int particleSize = 1;
    private boolean particlePhysics = true;
    private boolean particleCollision = false;
    
    // Effect settings
    private boolean showGlowEffects = true;
    private boolean showOutlineEffects = true;
    private boolean showHighlightEffects = true;
    private boolean showPulseEffects = false;
    private boolean showShakeEffects = false;
    
    // Notification settings
    private boolean showToastNotifications = true;
    private boolean showChatNotifications = true;
    private boolean showSoundNotifications = true;
    private int notificationDuration = 3000; // milliseconds
    private String notificationPosition = "top-right";
    
    // Tooltip settings
    private boolean showModuleTooltips = true;
    private boolean showSettingTooltips = true;
    private boolean showKeybindTooltips = true;
    private int tooltipDelay = 500; // milliseconds
    private int tooltipDuration = 2000; // milliseconds
    
    private VisualSettingsManager() {
        initializeThemes();
    }
    
    public static VisualSettingsManager getInstance() {
        if (instance == null) {
            instance = new VisualSettingsManager();
        }
        return instance;
    }
    
    private void initializeThemes() {
        // Default theme
        VisualTheme defaultTheme = new VisualTheme("Default");
        defaultTheme.setPrimaryColor("§a");
        defaultTheme.setSecondaryColor("§7");
        defaultTheme.setAccentColor("§b");
        defaultTheme.setBackgroundColor("§0");
        defaultTheme.setBorderColor("§8");
        themes.put("Default", defaultTheme);
        
        // Dark theme
        VisualTheme darkTheme = new VisualTheme("Dark");
        darkTheme.setPrimaryColor("§f");
        darkTheme.setSecondaryColor("§8");
        darkTheme.setAccentColor("§d");
        darkTheme.setBackgroundColor("§0");
        darkTheme.setBorderColor("§7");
        themes.put("Dark", darkTheme);
        
        // Light theme
        VisualTheme lightTheme = new VisualTheme("Light");
        lightTheme.setPrimaryColor("§0");
        lightTheme.setSecondaryColor("§7");
        lightTheme.setAccentColor("§9");
        lightTheme.setBackgroundColor("§f");
        lightTheme.setBorderColor("§8");
        themes.put("Light", lightTheme);
        
        // Neon theme
        VisualTheme neonTheme = new VisualTheme("Neon");
        neonTheme.setPrimaryColor("§b");
        neonTheme.setSecondaryColor("§7");
        neonTheme.setAccentColor("§d");
        neonTheme.setBackgroundColor("§0");
        neonTheme.setBorderColor("§b");
        themes.put("Neon", neonTheme);
        
        // Rainbow theme
        VisualTheme rainbowTheme = new VisualTheme("Rainbow");
        rainbowTheme.setPrimaryColor("§c");
        rainbowTheme.setSecondaryColor("§7");
        rainbowTheme.setAccentColor("§d");
        rainbowTheme.setBackgroundColor("§0");
        rainbowTheme.setBorderColor("§e");
        themes.put("Rainbow", rainbowTheme);
        
        // Matrix theme
        VisualTheme matrixTheme = new VisualTheme("Matrix");
        matrixTheme.setPrimaryColor("§a");
        matrixTheme.setSecondaryColor("§2");
        matrixTheme.setAccentColor("§a");
        matrixTheme.setBackgroundColor("§0");
        matrixTheme.setBorderColor("§a");
        themes.put("Matrix", matrixTheme);
        
        // Funtime theme
        VisualTheme funtimeTheme = new VisualTheme("Funtime");
        funtimeTheme.setPrimaryColor("§e");
        funtimeTheme.setSecondaryColor("§7");
        funtimeTheme.setAccentColor("§6");
        funtimeTheme.setBackgroundColor("§0");
        funtimeTheme.setBorderColor("§e");
        themes.put("Funtime", funtimeTheme);
    }
    
    public void applyTheme(String themeName) {
        VisualTheme theme = themes.get(themeName);
        if (theme != null) {
            currentTheme = themeName;
            primaryColor = theme.getPrimaryColor();
            secondaryColor = theme.getSecondaryColor();
            accentColor = theme.getAccentColor();
            Logger.info("Applied theme: " + themeName);
        } else {
            Logger.warn("Theme not found: " + themeName);
        }
    }
    
    public void createCustomTheme(String name, String primary, String secondary, String accent, String background, String border) {
        VisualTheme theme = new VisualTheme(name);
        theme.setPrimaryColor(primary);
        theme.setSecondaryColor(secondary);
        theme.setAccentColor(accent);
        theme.setBackgroundColor(background);
        theme.setBorderColor(border);
        themes.put(name, theme);
        Logger.info("Created custom theme: " + name);
    }
    
    public void deleteTheme(String themeName) {
        if (themes.containsKey(themeName) && !themeName.equals("Default")) {
            themes.remove(themeName);
            if (currentTheme.equals(themeName)) {
                applyTheme("Default");
            }
            Logger.info("Deleted theme: " + themeName);
        } else {
            Logger.warn("Cannot delete theme: " + themeName);
        }
    }
    
    public List<String> getAvailableThemes() {
        return new ArrayList<>(themes.keySet());
    }
    
    public String getCurrentTheme() {
        return currentTheme;
    }
    
    public VisualTheme getTheme(String name) {
        return themes.get(name);
    }
    
    // Getters and setters
    public String getPrimaryColor() { return primaryColor; }
    public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
    
    public String getSecondaryColor() { return secondaryColor; }
    public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }
    
    public String getAccentColor() { return accentColor; }
    public void setAccentColor(String accentColor) { this.accentColor = accentColor; }
    
    public String getWarningColor() { return warningColor; }
    public void setWarningColor(String warningColor) { this.warningColor = warningColor; }
    
    public String getErrorColor() { return errorColor; }
    public void setErrorColor(String errorColor) { this.errorColor = errorColor; }
    
    public String getSuccessColor() { return successColor; }
    public void setSuccessColor(String successColor) { this.successColor = successColor; }
    
    public String getInfoColor() { return infoColor; }
    public void setInfoColor(String infoColor) { this.infoColor = infoColor; }
    
    public boolean isShowAnimations() { return showAnimations; }
    public void setShowAnimations(boolean showAnimations) { this.showAnimations = showAnimations; }
    
    public boolean isShowParticles() { return showParticles; }
    public void setShowParticles(boolean showParticles) { this.showParticles = showParticles; }
    
    public boolean isShowEffects() { return showEffects; }
    public void setShowEffects(boolean showEffects) { this.showEffects = showEffects; }
    
    public boolean isShowNotifications() { return showNotifications; }
    public void setShowNotifications(boolean showNotifications) { this.showNotifications = showNotifications; }
    
    public boolean isShowTooltips() { return showTooltips; }
    public void setShowTooltips(boolean showTooltips) { this.showTooltips = showTooltips; }
    
    public boolean isShowBorders() { return showBorders; }
    public void setShowBorders(boolean showBorders) { this.showBorders = showBorders; }
    
    public boolean isShowShadows() { return showShadows; }
    public void setShowShadows(boolean showShadows) { this.showShadows = showShadows; }
    
    public boolean isShowGradients() { return showGradients; }
    public void setShowGradients(boolean showGradients) { this.showGradients = showGradients; }
    
    public int getFontSize() { return fontSize; }
    public void setFontSize(int fontSize) { this.fontSize = Math.max(8, Math.min(24, fontSize)); }
    
    public String getFontFamily() { return fontFamily; }
    public void setFontFamily(String fontFamily) { this.fontFamily = fontFamily; }
    
    public boolean isBoldText() { return boldText; }
    public void setBoldText(boolean boldText) { this.boldText = boldText; }
    
    public boolean isItalicText() { return italicText; }
    public void setItalicText(boolean italicText) { this.italicText = italicText; }
    
    public boolean isUnderlinedText() { return underlinedText; }
    public void setUnderlinedText(boolean underlinedText) { this.underlinedText = underlinedText; }
    
    public int getGuiScale() { return guiScale; }
    public void setGuiScale(int guiScale) { this.guiScale = Math.max(1, Math.min(4, guiScale)); }
    
    public boolean isCompactMode() { return compactMode; }
    public void setCompactMode(boolean compactMode) { this.compactMode = compactMode; }
    
    public boolean isShowIcons() { return showIcons; }
    public void setShowIcons(boolean showIcons) { this.showIcons = showIcons; }
    
    public boolean isShowDescriptions() { return showDescriptions; }
    public void setShowDescriptions(boolean showDescriptions) { this.showDescriptions = showDescriptions; }
    
    public boolean isShowCategories() { return showCategories; }
    public void setShowCategories(boolean showCategories) { this.showCategories = showCategories; }
    
    public boolean isShowStatus() { return showStatus; }
    public void setShowStatus(boolean showStatus) { this.showStatus = showStatus; }
    
    public int getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(int animationSpeed) { this.animationSpeed = Math.max(50, Math.min(1000, animationSpeed)); }
    
    public boolean isSmoothAnimations() { return smoothAnimations; }
    public void setSmoothAnimations(boolean smoothAnimations) { this.smoothAnimations = smoothAnimations; }
    
    public boolean isFadeEffects() { return fadeEffects; }
    public void setFadeEffects(boolean fadeEffects) { this.fadeEffects = fadeEffects; }
    
    public boolean isSlideEffects() { return slideEffects; }
    public void setSlideEffects(boolean slideEffects) { this.slideEffects = slideEffects; }
    
    public boolean isBounceEffects() { return bounceEffects; }
    public void setBounceEffects(boolean bounceEffects) { this.bounceEffects = bounceEffects; }
    
    public boolean isShowParticleTrails() { return showParticleTrails; }
    public void setShowParticleTrails(boolean showParticleTrails) { this.showParticleTrails = showParticleTrails; }
    
    public int getParticleDensity() { return particleDensity; }
    public void setParticleDensity(int particleDensity) { this.particleDensity = Math.max(0, Math.min(100, particleDensity)); }
    
    public int getParticleSize() { return particleSize; }
    public void setParticleSize(int particleSize) { this.particleSize = Math.max(1, Math.min(10, particleSize)); }
    
    public boolean isParticlePhysics() { return particlePhysics; }
    public void setParticlePhysics(boolean particlePhysics) { this.particlePhysics = particlePhysics; }
    
    public boolean isParticleCollision() { return particleCollision; }
    public void setParticleCollision(boolean particleCollision) { this.particleCollision = particleCollision; }
    
    public boolean isShowGlowEffects() { return showGlowEffects; }
    public void setShowGlowEffects(boolean showGlowEffects) { this.showGlowEffects = showGlowEffects; }
    
    public boolean isShowOutlineEffects() { return showOutlineEffects; }
    public void setShowOutlineEffects(boolean showOutlineEffects) { this.showOutlineEffects = showOutlineEffects; }
    
    public boolean isShowHighlightEffects() { return showHighlightEffects; }
    public void setShowHighlightEffects(boolean showHighlightEffects) { this.showHighlightEffects = showHighlightEffects; }
    
    public boolean isShowPulseEffects() { return showPulseEffects; }
    public void setShowPulseEffects(boolean showPulseEffects) { this.showPulseEffects = showPulseEffects; }
    
    public boolean isShowShakeEffects() { return showShakeEffects; }
    public void setShowShakeEffects(boolean showShakeEffects) { this.showShakeEffects = showShakeEffects; }
    
    public boolean isShowToastNotifications() { return showToastNotifications; }
    public void setShowToastNotifications(boolean showToastNotifications) { this.showToastNotifications = showToastNotifications; }
    
    public boolean isShowChatNotifications() { return showChatNotifications; }
    public void setShowChatNotifications(boolean showChatNotifications) { this.showChatNotifications = showChatNotifications; }
    
    public boolean isShowSoundNotifications() { return showSoundNotifications; }
    public void setShowSoundNotifications(boolean showSoundNotifications) { this.showSoundNotifications = showSoundNotifications; }
    
    public int getNotificationDuration() { return notificationDuration; }
    public void setNotificationDuration(int notificationDuration) { this.notificationDuration = Math.max(1000, Math.min(10000, notificationDuration)); }
    
    public String getNotificationPosition() { return notificationPosition; }
    public void setNotificationPosition(String notificationPosition) { this.notificationPosition = notificationPosition; }
    
    public boolean isShowModuleTooltips() { return showModuleTooltips; }
    public void setShowModuleTooltips(boolean showModuleTooltips) { this.showModuleTooltips = showModuleTooltips; }
    
    public boolean isShowSettingTooltips() { return showSettingTooltips; }
    public void setShowSettingTooltips(boolean showSettingTooltips) { this.showSettingTooltips = showSettingTooltips; }
    
    public boolean isShowKeybindTooltips() { return showKeybindTooltips; }
    public void setShowKeybindTooltips(boolean showKeybindTooltips) { this.showKeybindTooltips = showKeybindTooltips; }
    
    public int getTooltipDelay() { return tooltipDelay; }
    public void setTooltipDelay(int tooltipDelay) { this.tooltipDelay = Math.max(100, Math.min(2000, tooltipDelay)); }
    
    public int getTooltipDuration() { return tooltipDuration; }
    public void setTooltipDuration(int tooltipDuration) { this.tooltipDuration = Math.max(1000, Math.min(10000, tooltipDuration)); }
    
    // Inner class for themes
    public static class VisualTheme {
        private String name;
        private String primaryColor;
        private String secondaryColor;
        private String accentColor;
        private String backgroundColor;
        private String borderColor;
        
        public VisualTheme(String name) {
            this.name = name;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getPrimaryColor() { return primaryColor; }
        public void setPrimaryColor(String primaryColor) { this.primaryColor = primaryColor; }
        
        public String getSecondaryColor() { return secondaryColor; }
        public void setSecondaryColor(String secondaryColor) { this.secondaryColor = secondaryColor; }
        
        public String getAccentColor() { return accentColor; }
        public void setAccentColor(String accentColor) { this.accentColor = accentColor; }
        
        public String getBackgroundColor() { return backgroundColor; }
        public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
        
        public String getBorderColor() { return borderColor; }
        public void setBorderColor(String borderColor) { this.borderColor = borderColor; }
    }
}