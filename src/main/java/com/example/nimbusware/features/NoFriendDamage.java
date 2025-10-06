package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class NoFriendDamage extends Module {
    // No friend damage settings
    private boolean noFriendDamageEnabled = true;
    private boolean preventDamage = true;
    private boolean preventKnockback = true;
    private boolean preventPotionEffects = true;
    private boolean preventProjectiles = true;
    private boolean preventExplosions = true;
    private boolean preventFire = true;
    private boolean preventFallDamage = true;
    
    // Visual feedback settings
    private boolean showProtectionMessage = true;
    private boolean showDamageBlocked = true;
    private boolean showParticleEffects = true;
    private boolean showSoundEffects = true;
    private String protectionMessage = "§aProtected friend from damage!";
    private String damageBlockedMessage = "§cBlocked {damage} damage to {player}";
    
    // Friend detection settings
    private boolean useClickFriendsList = true;
    private boolean useCustomFriendList = false;
    private List<String> customFriends = new ArrayList<>();
    private boolean autoDetectTeammates = true;
    private boolean autoDetectGuildMembers = true;
    private boolean autoDetectPartyMembers = true;
    
    // Damage types to prevent
    private boolean preventMeleeDamage = true;
    private boolean preventRangedDamage = true;
    private boolean preventMagicDamage = true;
    private boolean preventEnvironmentalDamage = true;
    private boolean preventVoidDamage = true;
    private boolean preventLavaDamage = true;
    private boolean preventDrowningDamage = true;
    private boolean preventSuffocationDamage = true;
    
    // Server-specific bypasses
    private boolean useHypixelBypass = true;
    private boolean useFuntimeBypass = true;
    private boolean useMatrixBypass = true;
    private boolean useNCPBypass = true;
    private boolean useAACBypass = true;
    private boolean useGrimBypass = true;
    private boolean useVerusBypass = true;
    private boolean useVulcanBypass = true;
    
    // Anti-detection settings
    private boolean randomizeProtection = true;
    private boolean humanLikeBehavior = true;
    private boolean varyTiming = true;
    private int protectionDelay = 50; // Base delay for protection
    
    // Status tracking
    private int totalDamageBlocked = 0;
    private int meleeDamageBlocked = 0;
    private int rangedDamageBlocked = 0;
    private int magicDamageBlocked = 0;
    private int environmentalDamageBlocked = 0;
    private int friendsProtected = 0;
    private long lastProtectionTime = 0;
    
    private Random random = new Random();
    
    public NoFriendDamage() {
        super("NoFriendDamage", "Prevents damage to friends and teammates", Module.Category.PLAYER, 0);
        
        initializeDefaultSettings();
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("NoFriendDamage");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("NoFriendDamage");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.enableHypixelBypass("NoFriendDamage");
        }
        if (useNCPBypass) {
            AntiDetectionManager.enableNCPBypass("NoFriendDamage");
        }
        if (useAACBypass) {
            AntiDetectionManager.enableAACBypass("NoFriendDamage");
        }
        if (useGrimBypass) {
            AntiDetectionManager.enableGrimBypass("NoFriendDamage");
        }
        if (useVerusBypass) {
            AntiDetectionManager.enableVerusBypass("NoFriendDamage");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.enableVulcanBypass("NoFriendDamage");
        }
        
        Logger.info("NoFriendDamage enabled - Protecting friends from damage");
        Logger.info("Protection types: " + getEnabledProtectionTypes());
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("NoFriendDamage");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("NoFriendDamage");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.disableHypixelBypass("NoFriendDamage");
        }
        if (useNCPBypass) {
            AntiDetectionManager.disableNCPBypass("NoFriendDamage");
        }
        if (useAACBypass) {
            AntiDetectionManager.disableAACBypass("NoFriendDamage");
        }
        if (useGrimBypass) {
            AntiDetectionManager.disableGrimBypass("NoFriendDamage");
        }
        if (useVerusBypass) {
            AntiDetectionManager.disableVerusBypass("NoFriendDamage");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.disableVulcanBypass("NoFriendDamage");
        }
        
        Logger.info("NoFriendDamage disabled - Session stats:");
        Logger.info("Total damage blocked: " + totalDamageBlocked);
        Logger.info("Friends protected: " + friendsProtected);
    }
    
    public void onTick() {
        if (!isEnabled() || !noFriendDamageEnabled) {
            return;
        }
        
        // Simulate damage protection checking
        if (random.nextDouble() < 0.2) { // 20% chance per tick
            checkForFriendDamage();
        }
    }
    
    public void onRender() {
        if (!isEnabled() || !noFriendDamageEnabled) {
            return;
        }
        
        // Render protection indicators
        if (showParticleEffects) {
            renderProtectionParticles();
        }
    }
    
    private void checkForFriendDamage() {
        // Simulate checking for friend damage
        // In real implementation, this would check for damage events to friends
        
        String[] nearbyPlayers = getNearbyPlayers();
        for (String player : nearbyPlayers) {
            if (isFriend(player)) {
                if (isPlayerTakingDamage(player)) {
                    protectFriend(player);
                }
            }
        }
    }
    
    private void protectFriend(String playerName) {
        // Apply anti-detection
        applyAntiDetection();
        
        // Determine damage type and amount
        DamageInfo damage = getDamageInfo(playerName);
        
        // Block damage based on type
        boolean damageBlocked = false;
        
        if (preventMeleeDamage && damage.isMelee()) {
            damageBlocked = blockMeleeDamage(playerName, damage);
        } else if (preventRangedDamage && damage.isRanged()) {
            damageBlocked = blockRangedDamage(playerName, damage);
        } else if (preventMagicDamage && damage.isMagic()) {
            damageBlocked = blockMagicDamage(playerName, damage);
        } else if (preventEnvironmentalDamage && damage.isEnvironmental()) {
            damageBlocked = blockEnvironmentalDamage(playerName, damage);
        }
        
        if (damageBlocked) {
            handleDamageBlocked(playerName, damage);
        }
    }
    
    private boolean blockMeleeDamage(String playerName, DamageInfo damage) {
        if (preventDamage) {
            meleeDamageBlocked += damage.getAmount();
            totalDamageBlocked += damage.getAmount();
            friendsProtected++;
            lastProtectionTime = System.currentTimeMillis();
            
            Logger.debug("Blocked melee damage to " + playerName + ": " + damage.getAmount());
            return true;
        }
        return false;
    }
    
    private boolean blockRangedDamage(String playerName, DamageInfo damage) {
        if (preventDamage) {
            rangedDamageBlocked += damage.getAmount();
            totalDamageBlocked += damage.getAmount();
            friendsProtected++;
            lastProtectionTime = System.currentTimeMillis();
            
            Logger.debug("Blocked ranged damage to " + playerName + ": " + damage.getAmount());
            return true;
        }
        return false;
    }
    
    private boolean blockMagicDamage(String playerName, DamageInfo damage) {
        if (preventDamage) {
            magicDamageBlocked += damage.getAmount();
            totalDamageBlocked += damage.getAmount();
            friendsProtected++;
            lastProtectionTime = System.currentTimeMillis();
            
            Logger.debug("Blocked magic damage to " + playerName + ": " + damage.getAmount());
            return true;
        }
        return false;
    }
    
    private boolean blockEnvironmentalDamage(String playerName, DamageInfo damage) {
        if (preventDamage) {
            environmentalDamageBlocked += damage.getAmount();
            totalDamageBlocked += damage.getAmount();
            friendsProtected++;
            lastProtectionTime = System.currentTimeMillis();
            
            Logger.debug("Blocked environmental damage to " + playerName + ": " + damage.getAmount());
            return true;
        }
        return false;
    }
    
    private void handleDamageBlocked(String playerName, DamageInfo damage) {
        // Show protection message
        if (showProtectionMessage) {
            String message = protectionMessage.replace("{player}", playerName);
            Logger.info(message);
        }
        
        // Show damage blocked message
        if (showDamageBlocked) {
            String message = damageBlockedMessage
                .replace("{damage}", String.valueOf(damage.getAmount()))
                .replace("{player}", playerName);
            Logger.info(message);
        }
        
        // Play sound effect
        if (showSoundEffects) {
            playProtectionSound();
        }
        
        // Show particle effect
        if (showParticleEffects) {
            showProtectionParticles(playerName);
        }
    }
    
    private void renderProtectionParticles() {
        // Render protection particles around protected friends
        // In real implementation, this would render particles
        Logger.debug("Rendering protection particles");
    }
    
    private void playProtectionSound() {
        // Play protection sound effect
        // In real implementation, this would play a sound
        Logger.debug("Playing protection sound");
    }
    
    private void showProtectionParticles(String playerName) {
        // Show protection particles around player
        // In real implementation, this would show particles
        Logger.debug("Showing protection particles for " + playerName);
    }
    
    private String[] getNearbyPlayers() {
        // Simulate getting nearby players
        // In real implementation, this would get actual nearby players
        String[] players = {"Player1", "Player2", "Friend1", "Enemy1", "Teammate1"};
        return players;
    }
    
    private boolean isFriend(String playerName) {
        if (useClickFriendsList) {
            // Check ClickFriends list
            // In real implementation, this would check the ClickFriends module
            return random.nextDouble() < 0.3; // 30% chance of being friend
        }
        
        if (useCustomFriendList) {
            return customFriends.contains(playerName);
        }
        
        return false;
    }
    
    private boolean isPlayerTakingDamage(String playerName) {
        // Simulate checking if player is taking damage
        // In real implementation, this would check actual damage events
        return random.nextDouble() < 0.1; // 10% chance of taking damage
    }
    
    private DamageInfo getDamageInfo(String playerName) {
        // Simulate getting damage information
        // In real implementation, this would get actual damage data
        DamageInfo damage = new DamageInfo();
        damage.setAmount(5 + random.nextInt(15)); // 5-20 damage
        damage.setType(DamageType.values()[random.nextInt(DamageType.values().length)]);
        return damage;
    }
    
    private void applyAntiDetection() {
        if (humanLikeBehavior) {
            // Simulate human-like behavior
            try {
                Thread.sleep(protectionDelay + random.nextInt(50));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        if (randomizeProtection) {
            // Randomize protection behavior
            String[] actions = {"check", "protect", "monitor", "scan"};
            String action = actions[random.nextInt(actions.length)];
            Logger.debug("Protection action: " + action);
        }
    }
    
    private void initializeDefaultSettings() {
        // Initialize with some default friends
        customFriends.add("BestFriend");
        customFriends.add("Teammate1");
        customFriends.add("Teammate2");
    }
    
    private String getEnabledProtectionTypes() {
        List<String> types = new ArrayList<>();
        if (preventMeleeDamage) types.add("Melee");
        if (preventRangedDamage) types.add("Ranged");
        if (preventMagicDamage) types.add("Magic");
        if (preventEnvironmentalDamage) types.add("Environmental");
        return String.join(", ", types);
    }
    
    // Friend management methods
    public void addCustomFriend(String playerName) {
        if (!customFriends.contains(playerName)) {
            customFriends.add(playerName);
            Logger.info("Added custom friend: " + playerName);
        }
    }
    
    public void removeCustomFriend(String playerName) {
        if (customFriends.remove(playerName)) {
            Logger.info("Removed custom friend: " + playerName);
        }
    }
    
    public void clearCustomFriends() {
        customFriends.clear();
        Logger.info("Cleared custom friends list");
    }
    
    // Getters and setters
    public boolean isNoFriendDamageEnabled() { return noFriendDamageEnabled; }
    public void setNoFriendDamageEnabled(boolean noFriendDamageEnabled) { this.noFriendDamageEnabled = noFriendDamageEnabled; }
    
    public boolean isPreventDamage() { return preventDamage; }
    public void setPreventDamage(boolean preventDamage) { this.preventDamage = preventDamage; }
    
    public boolean isPreventKnockback() { return preventKnockback; }
    public void setPreventKnockback(boolean preventKnockback) { this.preventKnockback = preventKnockback; }
    
    public boolean isPreventPotionEffects() { return preventPotionEffects; }
    public void setPreventPotionEffects(boolean preventPotionEffects) { this.preventPotionEffects = preventPotionEffects; }
    
    public boolean isPreventProjectiles() { return preventProjectiles; }
    public void setPreventProjectiles(boolean preventProjectiles) { this.preventProjectiles = preventProjectiles; }
    
    public boolean isPreventExplosions() { return preventExplosions; }
    public void setPreventExplosions(boolean preventExplosions) { this.preventExplosions = preventExplosions; }
    
    public boolean isPreventFire() { return preventFire; }
    public void setPreventFire(boolean preventFire) { this.preventFire = preventFire; }
    
    public boolean isPreventFallDamage() { return preventFallDamage; }
    public void setPreventFallDamage(boolean preventFallDamage) { this.preventFallDamage = preventFallDamage; }
    
    public boolean isShowProtectionMessage() { return showProtectionMessage; }
    public void setShowProtectionMessage(boolean showProtectionMessage) { this.showProtectionMessage = showProtectionMessage; }
    
    public boolean isShowDamageBlocked() { return showDamageBlocked; }
    public void setShowDamageBlocked(boolean showDamageBlocked) { this.showDamageBlocked = showDamageBlocked; }
    
    public boolean isShowParticleEffects() { return showParticleEffects; }
    public void setShowParticleEffects(boolean showParticleEffects) { this.showParticleEffects = showParticleEffects; }
    
    public boolean isShowSoundEffects() { return showSoundEffects; }
    public void setShowSoundEffects(boolean showSoundEffects) { this.showSoundEffects = showSoundEffects; }
    
    public String getProtectionMessage() { return protectionMessage; }
    public void setProtectionMessage(String protectionMessage) { this.protectionMessage = protectionMessage; }
    
    public String getDamageBlockedMessage() { return damageBlockedMessage; }
    public void setDamageBlockedMessage(String damageBlockedMessage) { this.damageBlockedMessage = damageBlockedMessage; }
    
    public boolean isUseClickFriendsList() { return useClickFriendsList; }
    public void setUseClickFriendsList(boolean useClickFriendsList) { this.useClickFriendsList = useClickFriendsList; }
    
    public boolean isUseCustomFriendList() { return useCustomFriendList; }
    public void setUseCustomFriendList(boolean useCustomFriendList) { this.useCustomFriendList = useCustomFriendList; }
    
    public List<String> getCustomFriends() { return customFriends; }
    public void setCustomFriends(List<String> customFriends) { this.customFriends = customFriends; }
    
    public boolean isAutoDetectTeammates() { return autoDetectTeammates; }
    public void setAutoDetectTeammates(boolean autoDetectTeammates) { this.autoDetectTeammates = autoDetectTeammates; }
    
    public boolean isAutoDetectGuildMembers() { return autoDetectGuildMembers; }
    public void setAutoDetectGuildMembers(boolean autoDetectGuildMembers) { this.autoDetectGuildMembers = autoDetectGuildMembers; }
    
    public boolean isAutoDetectPartyMembers() { return autoDetectPartyMembers; }
    public void setAutoDetectPartyMembers(boolean autoDetectPartyMembers) { this.autoDetectPartyMembers = autoDetectPartyMembers; }
    
    // Damage type settings
    public boolean isPreventMeleeDamage() { return preventMeleeDamage; }
    public void setPreventMeleeDamage(boolean preventMeleeDamage) { this.preventMeleeDamage = preventMeleeDamage; }
    
    public boolean isPreventRangedDamage() { return preventRangedDamage; }
    public void setPreventRangedDamage(boolean preventRangedDamage) { this.preventRangedDamage = preventRangedDamage; }
    
    public boolean isPreventMagicDamage() { return preventMagicDamage; }
    public void setPreventMagicDamage(boolean preventMagicDamage) { this.preventMagicDamage = preventMagicDamage; }
    
    public boolean isPreventEnvironmentalDamage() { return preventEnvironmentalDamage; }
    public void setPreventEnvironmentalDamage(boolean preventEnvironmentalDamage) { this.preventEnvironmentalDamage = preventEnvironmentalDamage; }
    
    public boolean isPreventVoidDamage() { return preventVoidDamage; }
    public void setPreventVoidDamage(boolean preventVoidDamage) { this.preventVoidDamage = preventVoidDamage; }
    
    public boolean isPreventLavaDamage() { return preventLavaDamage; }
    public void setPreventLavaDamage(boolean preventLavaDamage) { this.preventLavaDamage = preventLavaDamage; }
    
    public boolean isPreventDrowningDamage() { return preventDrowningDamage; }
    public void setPreventDrowningDamage(boolean preventDrowningDamage) { this.preventDrowningDamage = preventDrowningDamage; }
    
    public boolean isPreventSuffocationDamage() { return preventSuffocationDamage; }
    public void setPreventSuffocationDamage(boolean preventSuffocationDamage) { this.preventSuffocationDamage = preventSuffocationDamage; }
    
    // Server bypass settings
    public boolean isUseHypixelBypass() { return useHypixelBypass; }
    public void setUseHypixelBypass(boolean useHypixelBypass) { this.useHypixelBypass = useHypixelBypass; }
    
    public boolean isUseFuntimeBypass() { return useFuntimeBypass; }
    public void setUseFuntimeBypass(boolean useFuntimeBypass) { this.useFuntimeBypass = useFuntimeBypass; }
    
    public boolean isUseMatrixBypass() { return useMatrixBypass; }
    public void setUseMatrixBypass(boolean useMatrixBypass) { this.useMatrixBypass = useMatrixBypass; }
    
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
    
    public boolean isRandomizeProtection() { return randomizeProtection; }
    public void setRandomizeProtection(boolean randomizeProtection) { this.randomizeProtection = randomizeProtection; }
    
    public boolean isHumanLikeBehavior() { return humanLikeBehavior; }
    public void setHumanLikeBehavior(boolean humanLikeBehavior) { this.humanLikeBehavior = humanLikeBehavior; }
    
    public boolean isVaryTiming() { return varyTiming; }
    public void setVaryTiming(boolean varyTiming) { this.varyTiming = varyTiming; }
    
    public int getProtectionDelay() { return protectionDelay; }
    public void setProtectionDelay(int protectionDelay) { this.protectionDelay = Math.max(10, protectionDelay); }
    
    // Status getters
    public int getTotalDamageBlocked() { return totalDamageBlocked; }
    public int getMeleeDamageBlocked() { return meleeDamageBlocked; }
    public int getRangedDamageBlocked() { return rangedDamageBlocked; }
    public int getMagicDamageBlocked() { return magicDamageBlocked; }
    public int getEnvironmentalDamageBlocked() { return environmentalDamageBlocked; }
    public int getFriendsProtected() { return friendsProtected; }
    public long getLastProtectionTime() { return lastProtectionTime; }
    
    public void resetStats() {
        totalDamageBlocked = 0;
        meleeDamageBlocked = 0;
        rangedDamageBlocked = 0;
        magicDamageBlocked = 0;
        environmentalDamageBlocked = 0;
        friendsProtected = 0;
        Logger.info("NoFriendDamage statistics reset");
    }
    
    // Inner classes
    public static class DamageInfo {
        private int amount;
        private DamageType type;
        
        public int getAmount() { return amount; }
        public void setAmount(int amount) { this.amount = amount; }
        
        public DamageType getType() { return type; }
        public void setType(DamageType type) { this.type = type; }
        
        public boolean isMelee() { return type == DamageType.MELEE; }
        public boolean isRanged() { return type == DamageType.RANGED; }
        public boolean isMagic() { return type == DamageType.MAGIC; }
        public boolean isEnvironmental() { return type == DamageType.ENVIRONMENTAL; }
    }
    
    public enum DamageType {
        MELEE,
        RANGED,
        MAGIC,
        ENVIRONMENTAL,
        VOID,
        LAVA,
        DROWNING,
        SUFFOCATION
    }
}