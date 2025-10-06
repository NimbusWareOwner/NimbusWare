package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;

public class Nimbus extends Module {
    // Nimbus Settings
    private boolean showNimbus = true;
    private boolean showGlow = true;
    private boolean showParticles = true;
    private boolean showAnimation = true;
    private boolean showRotation = true;
    
    // Visual Settings
    private String nimbusColor = "§b"; // Aqua
    private String glowColor = "§d"; // Light Purple
    private String particleColor = "§e"; // Yellow
    private float nimbusSize = 1.0f;
    private float glowIntensity = 0.8f;
    private float particleDensity = 0.5f;
    private float animationSpeed = 1.0f;
    private float rotationSpeed = 0.5f;
    
    // Position Settings
    private float nimbusHeight = 2.5f; // Height above player head
    private float nimbusOffsetX = 0.0f;
    private float nimbusOffsetZ = 0.0f;
    private boolean followPlayer = true;
    private boolean followLook = false;
    
    // Style Settings
    private NimbusStyle style = NimbusStyle.CLASSIC;
    private boolean showRays = true;
    private boolean showHalo = true;
    private boolean showWings = false;
    private int rayCount = 8;
    private float rayLength = 1.5f;
    private float haloRadius = 1.2f;
    
    // Animation Settings
    private boolean pulseAnimation = true;
    private boolean rotateAnimation = true;
    private boolean floatAnimation = true;
    private boolean particleAnimation = true;
    private float pulseSpeed = 2.0f;
    private float floatSpeed = 1.0f;
    private float floatAmplitude = 0.2f;
    
    // Particle Settings
    private ParticleType particleType = ParticleType.SPARKLE;
    private int particleCount = 20;
    private float particleSpeed = 0.5f;
    private float particleLifetime = 2.0f;
    private boolean particleGravity = false;
    private boolean particleFade = true;
    
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
    private List<NimbusParticle> particles = new ArrayList<>();
    private float animationTime = 0.0f;
    private float rotationAngle = 0.0f;
    private float pulsePhase = 0.0f;
    private float floatPhase = 0.0f;
    
    public Nimbus() {
        super("Nimbus", "Divine nimbus above player head", Module.Category.RENDER, 0);
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("Nimbus");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("Nimbus");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.enableHypixelBypass("Nimbus");
        }
        if (useNCPBypass) {
            AntiDetectionManager.enableNCPBypass("Nimbus");
        }
        if (useAACBypass) {
            AntiDetectionManager.enableAACBypass("Nimbus");
        }
        if (useGrimBypass) {
            AntiDetectionManager.enableGrimBypass("Nimbus");
        }
        if (useVerusBypass) {
            AntiDetectionManager.enableVerusBypass("Nimbus");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.enableVulcanBypass("Nimbus");
        }
        if (useSpartanBypass) {
            AntiDetectionManager.enableSpartanBypass("Nimbus");
        }
        if (useIntaveBypass) {
            AntiDetectionManager.enableIntaveBypass("Nimbus");
        }
        
        initializeParticles();
        Logger.info("Nimbus enabled - Divine aura active");
    }
    
    @Override
    protected void onDisable() {
        AntiDetectionManager.disableFuntimeBypass("Nimbus");
        AntiDetectionManager.disableMatrixBypass("Nimbus");
        AntiDetectionManager.disableHypixelBypass("Nimbus");
        AntiDetectionManager.disableNCPBypass("Nimbus");
        AntiDetectionManager.disableAACBypass("Nimbus");
        AntiDetectionManager.disableGrimBypass("Nimbus");
        AntiDetectionManager.disableVerusBypass("Nimbus");
        AntiDetectionManager.disableVulcanBypass("Nimbus");
        AntiDetectionManager.disableSpartanBypass("Nimbus");
        AntiDetectionManager.disableIntaveBypass("Nimbus");
        
        particles.clear();
        Logger.info("Nimbus disabled");
    }
    
    public void onTick() {
        if (!isEnabled()) return;
        
        updateAnimation();
        updateParticles();
    }
    
    public void onRender() {
        if (!isEnabled() || !showNimbus) return;
        
        renderNimbus();
        if (showParticles) {
            renderParticles();
        }
    }
    
    private void updateAnimation() {
        animationTime += 0.016f * animationSpeed; // 60 FPS
        
        if (rotateAnimation) {
            rotationAngle += rotationSpeed * 0.016f;
            if (rotationAngle >= 360.0f) {
                rotationAngle -= 360.0f;
            }
        }
        
        if (pulseAnimation) {
            pulsePhase += pulseSpeed * 0.016f;
            if (pulsePhase >= 360.0f) {
                pulsePhase -= 360.0f;
            }
        }
        
        if (floatAnimation) {
            floatPhase += floatSpeed * 0.016f;
            if (floatPhase >= 360.0f) {
                floatPhase -= 360.0f;
            }
        }
    }
    
    private void updateParticles() {
        if (!showParticles) return;
        
        // Remove old particles
        particles.removeIf(particle -> particle.getAge() >= particleLifetime);
        
        // Add new particles
        if (particles.size() < particleCount) {
            addRandomParticle();
        }
        
        // Update existing particles
        for (NimbusParticle particle : particles) {
            particle.update();
        }
    }
    
    private void addRandomParticle() {
        float angle = (float) (Math.random() * 2 * Math.PI);
        float distance = (float) (Math.random() * haloRadius);
        float x = (float) (Math.cos(angle) * distance);
        float z = (float) (Math.sin(angle) * distance);
        float y = (float) (Math.random() * 0.5f);
        
        NimbusParticle particle = new NimbusParticle(x, y, z, particleSpeed, particleColor);
        particles.add(particle);
    }
    
    private void renderNimbus() {
        float playerX = 0.0f; // Mock player position
        float playerY = 64.0f;
        float playerZ = 0.0f;
        
        float nimbusX = playerX + nimbusOffsetX;
        float nimbusY = playerY + nimbusHeight + (floatAnimation ? (float) Math.sin(floatPhase) * floatAmplitude : 0.0f);
        float nimbusZ = playerZ + nimbusOffsetZ;
        
        float currentSize = nimbusSize;
        if (pulseAnimation) {
            currentSize *= (1.0f + (float) Math.sin(pulsePhase) * 0.2f);
        }
        
        // Render main nimbus
        Logger.debug("Rendering nimbus at (" + nimbusX + ", " + nimbusY + ", " + nimbusZ + ") with size " + currentSize);
        
        // Render rays
        if (showRays) {
            renderRays(nimbusX, nimbusY, nimbusZ, currentSize);
        }
        
        // Render halo
        if (showHalo) {
            renderHalo(nimbusX, nimbusY, nimbusZ, currentSize);
        }
        
        // Render wings
        if (showWings) {
            renderWings(nimbusX, nimbusY, nimbusZ, currentSize);
        }
        
        // Render glow
        if (showGlow) {
            renderGlow(nimbusX, nimbusY, nimbusZ, currentSize);
        }
    }
    
    private void renderRays(float x, float y, float z, float size) {
        for (int i = 0; i < rayCount; i++) {
            float angle = (360.0f / rayCount) * i + rotationAngle;
            float rayX = x + (float) (Math.cos(Math.toRadians(angle)) * size * rayLength);
            float rayZ = z + (float) (Math.sin(Math.toRadians(angle)) * size * rayLength);
            
            Logger.debug("Rendering ray " + i + " from (" + x + ", " + y + ", " + z + ") to (" + rayX + ", " + y + ", " + rayZ + ")");
        }
    }
    
    private void renderHalo(float x, float y, float z, float size) {
        float haloSize = size * haloRadius;
        Logger.debug("Rendering halo at (" + x + ", " + y + ", " + z + ") with radius " + haloSize);
    }
    
    private void renderWings(float x, float y, float z, float size) {
        Logger.debug("Rendering wings at (" + x + ", " + y + ", " + z + ") with size " + size);
    }
    
    private void renderGlow(float x, float y, float z, float size) {
        float glowSize = size * (1.0f + glowIntensity);
        Logger.debug("Rendering glow at (" + x + ", " + y + ", " + z + ") with size " + glowSize);
    }
    
    private void renderParticles() {
        for (NimbusParticle particle : particles) {
            particle.render();
        }
    }
    
    private void initializeParticles() {
        particles.clear();
        for (int i = 0; i < particleCount / 2; i++) {
            addRandomParticle();
        }
    }
    
    // Getters and Setters
    public boolean isShowNimbus() { return showNimbus; }
    public void setShowNimbus(boolean showNimbus) { this.showNimbus = showNimbus; }
    
    public boolean isShowGlow() { return showGlow; }
    public void setShowGlow(boolean showGlow) { this.showGlow = showGlow; }
    
    public boolean isShowParticles() { return showParticles; }
    public void setShowParticles(boolean showParticles) { this.showParticles = showParticles; }
    
    public boolean isShowAnimation() { return showAnimation; }
    public void setShowAnimation(boolean showAnimation) { this.showAnimation = showAnimation; }
    
    public boolean isShowRotation() { return showRotation; }
    public void setShowRotation(boolean showRotation) { this.showRotation = showRotation; }
    
    public String getNimbusColor() { return nimbusColor; }
    public void setNimbusColor(String nimbusColor) { this.nimbusColor = nimbusColor; }
    
    public String getGlowColor() { return glowColor; }
    public void setGlowColor(String glowColor) { this.glowColor = glowColor; }
    
    public String getParticleColor() { return particleColor; }
    public void setParticleColor(String particleColor) { this.particleColor = particleColor; }
    
    public float getNimbusSize() { return nimbusSize; }
    public void setNimbusSize(float nimbusSize) { this.nimbusSize = nimbusSize; }
    
    public float getGlowIntensity() { return glowIntensity; }
    public void setGlowIntensity(float glowIntensity) { this.glowIntensity = glowIntensity; }
    
    public float getParticleDensity() { return particleDensity; }
    public void setParticleDensity(float particleDensity) { this.particleDensity = particleDensity; }
    
    public float getAnimationSpeed() { return animationSpeed; }
    public void setAnimationSpeed(float animationSpeed) { this.animationSpeed = animationSpeed; }
    
    public float getRotationSpeed() { return rotationSpeed; }
    public void setRotationSpeed(float rotationSpeed) { this.rotationSpeed = rotationSpeed; }
    
    public float getNimbusHeight() { return nimbusHeight; }
    public void setNimbusHeight(float nimbusHeight) { this.nimbusHeight = nimbusHeight; }
    
    public float getNimbusOffsetX() { return nimbusOffsetX; }
    public void setNimbusOffsetX(float nimbusOffsetX) { this.nimbusOffsetX = nimbusOffsetX; }
    
    public float getNimbusOffsetZ() { return nimbusOffsetZ; }
    public void setNimbusOffsetZ(float nimbusOffsetZ) { this.nimbusOffsetZ = nimbusOffsetZ; }
    
    public boolean isFollowPlayer() { return followPlayer; }
    public void setFollowPlayer(boolean followPlayer) { this.followPlayer = followPlayer; }
    
    public boolean isFollowLook() { return followLook; }
    public void setFollowLook(boolean followLook) { this.followLook = followLook; }
    
    public NimbusStyle getStyle() { return style; }
    public void setStyle(NimbusStyle style) { this.style = style; }
    
    public boolean isShowRays() { return showRays; }
    public void setShowRays(boolean showRays) { this.showRays = showRays; }
    
    public boolean isShowHalo() { return showHalo; }
    public void setShowHalo(boolean showHalo) { this.showHalo = showHalo; }
    
    public boolean isShowWings() { return showWings; }
    public void setShowWings(boolean showWings) { this.showWings = showWings; }
    
    public int getRayCount() { return rayCount; }
    public void setRayCount(int rayCount) { this.rayCount = rayCount; }
    
    public float getRayLength() { return rayLength; }
    public void setRayLength(float rayLength) { this.rayLength = rayLength; }
    
    public float getHaloRadius() { return haloRadius; }
    public void setHaloRadius(float haloRadius) { this.haloRadius = haloRadius; }
    
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
    
    // Enums
    public enum NimbusStyle {
        CLASSIC, MODERN, ANCIENT, DIVINE, MYSTICAL, CELESTIAL
    }
    
    public enum ParticleType {
        SPARKLE, STAR, CIRCLE, SQUARE, DIAMOND, HEART, FLAME, LIGHTNING
    }
    
    // Inner Classes
    private static class NimbusParticle {
        private float x, y, z;
        private float velocityX, velocityY, velocityZ;
        private String color;
        private float age = 0.0f;
        private float lifetime;
        private boolean gravity;
        private boolean fade;
        
        public NimbusParticle(float x, float y, float z, float speed, String color) {
            this.x = x;
            this.y = y;
            this.z = z;
            this.color = color;
            this.lifetime = 2.0f;
            this.gravity = false;
            this.fade = true;
            
            // Random velocity
            this.velocityX = (float) (Math.random() - 0.5) * speed;
            this.velocityY = (float) (Math.random() - 0.5) * speed;
            this.velocityZ = (float) (Math.random() - 0.5) * speed;
        }
        
        public void update() {
            age += 0.016f; // 60 FPS
            
            x += velocityX * 0.016f;
            y += velocityY * 0.016f;
            z += velocityZ * 0.016f;
            
            if (gravity) {
                velocityY -= 0.1f; // Gravity
            }
        }
        
        public void render() {
            float alpha = fade ? (1.0f - (age / lifetime)) : 1.0f;
            Logger.debug("Rendering particle at (" + x + ", " + y + ", " + z + ") with color " + color + " and alpha " + alpha);
        }
        
        public float getAge() { return age; }
        public float getX() { return x; }
        public float getY() { return y; }
        public float getZ() { return z; }
    }
}