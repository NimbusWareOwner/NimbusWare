package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class HUDManager {
    private static HUDManager instance;
    private CheatClient client;
    private boolean hudEnabled = true;
    private boolean showFPS = true;
    private boolean showPing = true;
    private boolean showCoordinates = true;
    private boolean showBiome = true;
    private boolean showTime = true;
    private boolean showSpeed = true;
    private boolean showDirection = true;
    private boolean showHealth = true;
    private boolean showHunger = true;
    private boolean showArmor = true;
    private boolean showExperience = true;
    private boolean showModules = true;
    private boolean showWatermark = true;
    private boolean showNotifications = true;
    
    // HUD positioning
    private String fpsPosition = "top-right";
    private String pingPosition = "top-right";
    private String coordinatesPosition = "top-left";
    private String biomePosition = "top-left";
    private String timePosition = "top-right";
    private String speedPosition = "top-right";
    private String directionPosition = "top-right";
    private String healthPosition = "bottom-left";
    private String hungerPosition = "bottom-left";
    private String armorPosition = "bottom-left";
    private String experiencePosition = "bottom-left";
    private String modulesPosition = "top-left";
    private String watermarkPosition = "top-center";
    private String notificationsPosition = "top-center";
    
    // HUD styling
    private String primaryColor = "§a";
    private String secondaryColor = "§7";
    private String accentColor = "§b";
    private String warningColor = "§e";
    private String errorColor = "§c";
    private String successColor = "§a";
    private String infoColor = "§b";
    private String backgroundColor = "§0";
    private String borderColor = "§8";
    
    // HUD effects
    private boolean showGlow = true;
    private boolean showOutline = true;
    private boolean showShadow = true;
    private boolean showGradient = true;
    private boolean showPulse = false;
    private boolean showRainbow = false;
    private boolean showMatrix = false;
    private boolean showParticles = true;
    private boolean showAnimations = true;
    
    // Animation settings
    private int animationSpeed = 100;
    private boolean smoothAnimations = true;
    private boolean fadeInOut = true;
    private boolean slideInOut = true;
    private boolean bounceEffect = false;
    
    // Particle settings
    private int particleDensity = 30;
    private int particleSize = 1;
    private boolean particlePhysics = true;
    private boolean particleTrails = true;
    
    // HUD data
    private Map<String, Object> hudData = new ConcurrentHashMap<>();
    private List<HUDNotification> notifications = new ArrayList<>();
    private List<HUDParticle> particles = new ArrayList<>();
    private long lastUpdate = 0;
    private int frameCount = 0;
    private long lastFPSUpdate = 0;
    private int currentFPS = 0;
    
    // HUD elements
    private List<HUDElement> elements = new ArrayList<>();
    private Random random = new Random();
    
    private HUDManager(CheatClient client) {
        this.client = client;
        initializeHUD();
    }
    
    public static HUDManager getInstance(CheatClient client) {
        if (instance == null) {
            instance = new HUDManager(client);
        }
        return instance;
    }
    
    private void initializeHUD() {
        // Initialize HUD elements
        elements.add(new HUDElement("fps", "FPS: {fps}", fpsPosition, true));
        elements.add(new HUDElement("ping", "Ping: {ping}ms", pingPosition, true));
        elements.add(new HUDElement("coordinates", "XYZ: {x}, {y}, {z}", coordinatesPosition, true));
        elements.add(new HUDElement("biome", "Biome: {biome}", biomePosition, true));
        elements.add(new HUDElement("time", "Time: {time}", timePosition, true));
        elements.add(new HUDElement("speed", "Speed: {speed} m/s", speedPosition, true));
        elements.add(new HUDElement("direction", "Direction: {direction}", directionPosition, true));
        elements.add(new HUDElement("health", "Health: {health}/20", healthPosition, true));
        elements.add(new HUDElement("hunger", "Hunger: {hunger}/20", hungerPosition, true));
        elements.add(new HUDElement("armor", "Armor: {armor}/20", armorPosition, true));
        elements.add(new HUDElement("experience", "XP: {experience}", experiencePosition, true));
        elements.add(new HUDElement("modules", "Modules: {modules}", modulesPosition, true));
        elements.add(new HUDElement("watermark", "§l§bNimbusWare §7v1.0.0", watermarkPosition, true));
        
        // Initialize HUD data
        updateHUDData();
    }
    
    public void updateHUD() {
        if (!hudEnabled) return;
        
        long currentTime = System.currentTimeMillis();
        if (currentTime - lastUpdate < 50) return; // 20 FPS update rate
        
        lastUpdate = currentTime;
        frameCount++;
        
        // Update FPS
        if (currentTime - lastFPSUpdate >= 1000) {
            currentFPS = frameCount;
            frameCount = 0;
            lastFPSUpdate = currentTime;
        }
        
        // Update HUD data
        updateHUDData();
        
        // Update particles
        updateParticles();
        
        // Update notifications
        updateNotifications();
        
        // Render HUD
        renderHUD();
    }
    
    private void updateHUDData() {
        // Simulate HUD data
        hudData.put("fps", currentFPS);
        hudData.put("ping", 50 + random.nextInt(100));
        hudData.put("x", 100 + random.nextInt(1000));
        hudData.put("y", 64 + random.nextInt(100));
        hudData.put("z", 200 + random.nextInt(1000));
        hudData.put("biome", getRandomBiome());
        hudData.put("time", getCurrentTime());
        hudData.put("speed", String.format("%.2f", 0.5 + random.nextDouble() * 5));
        hudData.put("direction", getRandomDirection());
        hudData.put("health", 15 + random.nextInt(5));
        hudData.put("hunger", 18 + random.nextInt(2));
        hudData.put("armor", 10 + random.nextInt(10));
        hudData.put("experience", 1000 + random.nextInt(5000));
        hudData.put("modules", getActiveModulesCount());
    }
    
    private void updateParticles() {
        if (!showParticles) return;
        
        // Add new particles
        if (random.nextDouble() < particleDensity / 1000.0) {
            particles.add(new HUDParticle(
                random.nextDouble() * 100,
                random.nextDouble() * 100,
                random.nextDouble() * 2 - 1,
                random.nextDouble() * 2 - 1,
                getRandomColor(),
                particleSize
            ));
        }
        
        // Update existing particles
        particles.removeIf(particle -> {
            particle.update();
            return particle.isDead();
        });
    }
    
    private void updateNotifications() {
        if (!showNotifications) return;
        
        // Remove expired notifications
        notifications.removeIf(notification -> {
            notification.update();
            return notification.isExpired();
        });
    }
    
    private void renderHUD() {
        // Render background effects
        if (showMatrix) {
            renderMatrixEffect();
        }
        
        if (showRainbow) {
            renderRainbowEffect();
        }
        
        // Render particles
        if (showParticles) {
            renderParticles();
        }
        
        // Render HUD elements
        for (HUDElement element : elements) {
            if (element.isVisible()) {
                renderElement(element);
            }
        }
        
        // Render notifications
        if (showNotifications) {
            renderNotifications();
        }
    }
    
    private void renderElement(HUDElement element) {
        String text = element.getText();
        
        // Replace placeholders
        for (Map.Entry<String, Object> entry : hudData.entrySet()) {
            text = text.replace("{" + entry.getKey() + "}", String.valueOf(entry.getValue()));
        }
        
        // Apply colors and effects
        text = applyElementEffects(text, element);
        
        // Render element
        Logger.debug("HUD Element [" + element.getName() + "]: " + text);
    }
    
    private String applyElementEffects(String text, HUDElement element) {
        // Apply colors
        if (text.contains("FPS")) {
            text = primaryColor + text;
        } else if (text.contains("Ping")) {
            text = infoColor + text;
        } else if (text.contains("XYZ")) {
            text = accentColor + text;
        } else if (text.contains("Biome")) {
            text = secondaryColor + text;
        } else if (text.contains("Time")) {
            text = warningColor + text;
        } else if (text.contains("Speed")) {
            text = successColor + text;
        } else if (text.contains("Direction")) {
            text = infoColor + text;
        } else if (text.contains("Health")) {
            text = errorColor + text;
        } else if (text.contains("Hunger")) {
            text = warningColor + text;
        } else if (text.contains("Armor")) {
            text = accentColor + text;
        } else if (text.contains("XP")) {
            text = successColor + text;
        } else if (text.contains("Modules")) {
            text = primaryColor + text;
        } else if (text.contains("CheatClient")) {
            text = accentColor + text;
        }
        
        // Apply effects
        if (showGlow) {
            text = "§k" + text + "§r";
        }
        
        if (showOutline) {
            text = "§l" + text;
        }
        
        if (showShadow) {
            text = "§0" + text + "§r";
        }
        
        if (showPulse && random.nextDouble() < 0.1) {
            text = "§k" + text + "§r";
        }
        
        return text;
    }
    
    private void renderMatrixEffect() {
        // Simulate matrix effect
        if (random.nextDouble() < 0.1) {
            Logger.debug("Matrix effect: " + generateMatrixString());
        }
    }
    
    private void renderRainbowEffect() {
        // Simulate rainbow effect
        if (random.nextDouble() < 0.1) {
            Logger.debug("Rainbow effect: " + generateRainbowString());
        }
    }
    
    private void renderParticles() {
        for (HUDParticle particle : particles) {
            Logger.debug("Particle: " + particle.toString());
        }
    }
    
    private void renderNotifications() {
        for (HUDNotification notification : notifications) {
            Logger.debug("Notification: " + notification.toString());
        }
    }
    
    private String generateMatrixString() {
        String chars = "01";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return "§a" + sb.toString();
    }
    
    private String generateRainbowString() {
        String[] colors = {"§c", "§6", "§e", "§a", "§b", "§d"};
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 10; i++) {
            sb.append(colors[random.nextInt(colors.length)]).append("█");
        }
        return sb.toString();
    }
    
    private String getRandomBiome() {
        String[] biomes = {"Plains", "Forest", "Desert", "Mountains", "Ocean", "Swamp", "Jungle", "Tundra"};
        return biomes[random.nextInt(biomes.length)];
    }
    
    private String getCurrentTime() {
        return String.format("%02d:%02d", random.nextInt(24), random.nextInt(60));
    }
    
    private String getRandomDirection() {
        String[] directions = {"North", "South", "East", "West", "Northeast", "Northwest", "Southeast", "Southwest"};
        return directions[random.nextInt(directions.length)];
    }
    
    private int getActiveModulesCount() {
        return client.getModuleManager().getEnabledModules().size();
    }
    
    private String getRandomColor() {
        String[] colors = {"§c", "§6", "§e", "§a", "§b", "§d", "§f"};
        return colors[random.nextInt(colors.length)];
    }
    
    // Public methods
    public void addNotification(String message, int duration) {
        notifications.add(new HUDNotification(message, duration));
    }
    
    public void addNotification(String message, String color, int duration) {
        notifications.add(new HUDNotification(color + message, duration));
    }
    
    public void clearNotifications() {
        notifications.clear();
    }
    
    public void setElementVisible(String name, boolean visible) {
        for (HUDElement element : elements) {
            if (element.getName().equals(name)) {
                element.setVisible(visible);
                break;
            }
        }
    }
    
    public void setElementPosition(String name, String position) {
        for (HUDElement element : elements) {
            if (element.getName().equals(name)) {
                element.setPosition(position);
                break;
            }
        }
    }
    
    public void setElementText(String name, String text) {
        for (HUDElement element : elements) {
            if (element.getName().equals(name)) {
                element.setText(text);
                break;
            }
        }
    }
    
    // Getters and setters
    public boolean isHudEnabled() { return hudEnabled; }
    public void setHudEnabled(boolean hudEnabled) { this.hudEnabled = hudEnabled; }
    
    public boolean isShowFPS() { return showFPS; }
    public void setShowFPS(boolean showFPS) { this.showFPS = showFPS; }
    
    public boolean isShowPing() { return showPing; }
    public void setShowPing(boolean showPing) { this.showPing = showPing; }
    
    public boolean isShowCoordinates() { return showCoordinates; }
    public void setShowCoordinates(boolean showCoordinates) { this.showCoordinates = showCoordinates; }
    
    public boolean isShowBiome() { return showBiome; }
    public void setShowBiome(boolean showBiome) { this.showBiome = showBiome; }
    
    public boolean isShowTime() { return showTime; }
    public void setShowTime(boolean showTime) { this.showTime = showTime; }
    
    public boolean isShowSpeed() { return showSpeed; }
    public void setShowSpeed(boolean showSpeed) { this.showSpeed = showSpeed; }
    
    public boolean isShowDirection() { return showDirection; }
    public void setShowDirection(boolean showDirection) { this.showDirection = showDirection; }
    
    public boolean isShowHealth() { return showHealth; }
    public void setShowHealth(boolean showHealth) { this.showHealth = showHealth; }
    
    public boolean isShowHunger() { return showHunger; }
    public void setShowHunger(boolean showHunger) { this.showHunger = showHunger; }
    
    public boolean isShowArmor() { return showArmor; }
    public void setShowArmor(boolean showArmor) { this.showArmor = showArmor; }
    
    public boolean isShowExperience() { return showExperience; }
    public void setShowExperience(boolean showExperience) { this.showExperience = showExperience; }
    
    public boolean isShowModules() { return showModules; }
    public void setShowModules(boolean showModules) { this.showModules = showModules; }
    
    public boolean isShowWatermark() { return showWatermark; }
    public void setShowWatermark(boolean showWatermark) { this.showWatermark = showWatermark; }
    
    public boolean isShowNotifications() { return showNotifications; }
    public void setShowNotifications(boolean showNotifications) { this.showNotifications = showNotifications; }
    
    // Position getters and setters
    public String getFpsPosition() { return fpsPosition; }
    public void setFpsPosition(String fpsPosition) { this.fpsPosition = fpsPosition; }
    
    public String getPingPosition() { return pingPosition; }
    public void setPingPosition(String pingPosition) { this.pingPosition = pingPosition; }
    
    public String getCoordinatesPosition() { return coordinatesPosition; }
    public void setCoordinatesPosition(String coordinatesPosition) { this.coordinatesPosition = coordinatesPosition; }
    
    public String getBiomePosition() { return biomePosition; }
    public void setBiomePosition(String biomePosition) { this.biomePosition = biomePosition; }
    
    public String getTimePosition() { return timePosition; }
    public void setTimePosition(String timePosition) { this.timePosition = timePosition; }
    
    public String getSpeedPosition() { return speedPosition; }
    public void setSpeedPosition(String speedPosition) { this.speedPosition = speedPosition; }
    
    public String getDirectionPosition() { return directionPosition; }
    public void setDirectionPosition(String directionPosition) { this.directionPosition = directionPosition; }
    
    public String getHealthPosition() { return healthPosition; }
    public void setHealthPosition(String healthPosition) { this.healthPosition = healthPosition; }
    
    public String getHungerPosition() { return hungerPosition; }
    public void setHungerPosition(String hungerPosition) { this.hungerPosition = hungerPosition; }
    
    public String getArmorPosition() { return armorPosition; }
    public void setArmorPosition(String armorPosition) { this.armorPosition = armorPosition; }
    
    public String getExperiencePosition() { return experiencePosition; }
    public void setExperiencePosition(String experiencePosition) { this.experiencePosition = experiencePosition; }
    
    public String getModulesPosition() { return modulesPosition; }
    public void setModulesPosition(String modulesPosition) { this.modulesPosition = modulesPosition; }
    
    public String getWatermarkPosition() { return watermarkPosition; }
    public void setWatermarkPosition(String watermarkPosition) { this.watermarkPosition = watermarkPosition; }
    
    public String getNotificationsPosition() { return notificationsPosition; }
    public void setNotificationsPosition(String notificationsPosition) { this.notificationsPosition = notificationsPosition; }
    
    // Color getters and setters
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
    
    public String getBackgroundColor() { return backgroundColor; }
    public void setBackgroundColor(String backgroundColor) { this.backgroundColor = backgroundColor; }
    
    public String getBorderColor() { return borderColor; }
    public void setBorderColor(String borderColor) { this.borderColor = borderColor; }
    
    // Effect getters and setters
    public boolean isShowGlow() { return showGlow; }
    public void setShowGlow(boolean showGlow) { this.showGlow = showGlow; }
    
    public boolean isShowOutline() { return showOutline; }
    public void setShowOutline(boolean showOutline) { this.showOutline = showOutline; }
    
    public boolean isShowShadow() { return showShadow; }
    public void setShowShadow(boolean showShadow) { this.showShadow = showShadow; }
    
    public boolean isShowGradient() { return showGradient; }
    public void setShowGradient(boolean showGradient) { this.showGradient = showGradient; }
    
    public boolean isShowPulse() { return showPulse; }
    public void setShowPulse(boolean showPulse) { this.showPulse = showPulse; }
    
    public boolean isShowRainbow() { return showRainbow; }
    public void setShowRainbow(boolean showRainbow) { this.showRainbow = showRainbow; }
    
    public boolean isShowMatrix() { return showMatrix; }
    public void setShowMatrix(boolean showMatrix) { this.showMatrix = showMatrix; }
    
    public boolean isShowParticles() { return showParticles; }
    public void setShowParticles(boolean showParticles) { this.showParticles = showParticles; }
    
    public boolean isShowAnimations() { return showAnimations; }
    public void setShowAnimations(boolean showAnimations) { this.showAnimations = showAnimations; }
    
    // Animation getters and setters
    public int getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(int animationSpeed) { this.animationSpeed = Math.max(10, Math.min(1000, animationSpeed)); }
    
    public boolean isSmoothAnimations() { return smoothAnimations; }
    public void setSmoothAnimations(boolean smoothAnimations) { this.smoothAnimations = smoothAnimations; }
    
    public boolean isFadeInOut() { return fadeInOut; }
    public void setFadeInOut(boolean fadeInOut) { this.fadeInOut = fadeInOut; }
    
    public boolean isSlideInOut() { return slideInOut; }
    public void setSlideInOut(boolean slideInOut) { this.slideInOut = slideInOut; }
    
    public boolean isBounceEffect() { return bounceEffect; }
    public void setBounceEffect(boolean bounceEffect) { this.bounceEffect = bounceEffect; }
    
    // Particle getters and setters
    public int getParticleDensity() { return particleDensity; }
    public void setParticleDensity(int particleDensity) { this.particleDensity = Math.max(0, Math.min(100, particleDensity)); }
    
    public int getParticleSize() { return particleSize; }
    public void setParticleSize(int particleSize) { this.particleSize = Math.max(1, Math.min(10, particleSize)); }
    
    public boolean isParticlePhysics() { return particlePhysics; }
    public void setParticlePhysics(boolean particlePhysics) { this.particlePhysics = particlePhysics; }
    
    public boolean isParticleTrails() { return particleTrails; }
    public void setParticleTrails(boolean particleTrails) { this.particleTrails = particleTrails; }
    
    // Inner classes
    public static class HUDElement {
        private String name;
        private String text;
        private String position;
        private boolean visible;
        
        public HUDElement(String name, String text, String position, boolean visible) {
            this.name = name;
            this.text = text;
            this.position = position;
            this.visible = visible;
        }
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public String getText() { return text; }
        public void setText(String text) { this.text = text; }
        
        public String getPosition() { return position; }
        public void setPosition(String position) { this.position = position; }
        
        public boolean isVisible() { return visible; }
        public void setVisible(boolean visible) { this.visible = visible; }
    }
    
    public static class HUDNotification {
        private String message;
        private int duration;
        private long startTime;
        private boolean expired;
        
        public HUDNotification(String message, int duration) {
            this.message = message;
            this.duration = duration;
            this.startTime = System.currentTimeMillis();
            this.expired = false;
        }
        
        public void update() {
            if (System.currentTimeMillis() - startTime >= duration) {
                expired = true;
            }
        }
        
        public boolean isExpired() { return expired; }
        public String getMessage() { return message; }
        public int getDuration() { return duration; }
        public long getStartTime() { return startTime; }
        
        @Override
        public String toString() {
            return message;
        }
    }
    
    public static class HUDParticle {
        private double x, y;
        private double vx, vy;
        private String color;
        private int size;
        private int life;
        private int maxLife;
        private boolean dead;
        
        public HUDParticle(double x, double y, double vx, double vy, String color, int size) {
            this.x = x;
            this.y = y;
            this.vx = vx;
            this.vy = vy;
            this.color = color;
            this.size = size;
            this.life = 0;
            this.maxLife = 60 + (int)(Math.random() * 120);
            this.dead = false;
        }
        
        public void update() {
            x += vx;
            y += vy;
            life++;
            
            if (life >= maxLife) {
                dead = true;
            }
        }
        
        public boolean isDead() { return dead; }
        public double getX() { return x; }
        public double getY() { return y; }
        public String getColor() { return color; }
        public int getSize() { return size; }
        
        @Override
        public String toString() {
            return color + "●";
        }
    }
}