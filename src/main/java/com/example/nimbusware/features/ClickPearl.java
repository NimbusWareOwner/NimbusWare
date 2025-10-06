package com.example.nimbusware.features;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.anti_detection.AntiDetectionManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class ClickPearl extends Module {
    // Click pearl settings
    private boolean clickPearlEnabled = true;
    private boolean autoThrow = true;
    private boolean autoCatch = true;
    private boolean smartThrow = true;
    private boolean smartCatch = true;
    private boolean instantThrow = false;
    private boolean instantCatch = false;
    
    // Pearl management
    private int pearlCount = 0;
    private int maxPearls = 16;
    private int minPearls = 1;
    private boolean autoRefill = true;
    private boolean prioritizePearls = true;
    private boolean dropOnLowHealth = true;
    private int lowHealthThreshold = 5; // hearts
    
    // Throw settings
    private boolean throwAtEnemies = true;
    private boolean throwAtFriends = false;
    private boolean throwAtNeutrals = false;
    private boolean throwAtPlayers = true;
    private boolean throwAtMobs = false;
    private boolean throwAtAnimals = false;
    private int throwRange = 50; // blocks
    private int throwCooldown = 1000; // milliseconds
    private boolean predictMovement = true;
    private boolean leadTarget = true;
    
    // Catch settings
    private boolean catchFromEnemies = true;
    private boolean catchFromFriends = true;
    private boolean catchFromNeutrals = true;
    private boolean catchFromPlayers = true;
    private boolean catchFromMobs = false;
    private boolean catchFromAnimals = false;
    private int catchRange = 30; // blocks
    private int catchCooldown = 500; // milliseconds
    private boolean predictPearlPath = true;
    private boolean smartCatching = true;
    
    // Visual settings
    private boolean showPearlTrajectory = true;
    private boolean showPearlPrediction = true;
    private boolean showCatchZone = true;
    private boolean showThrowZone = true;
    private String pearlColor = "§b"; // Aqua
    private String trajectoryColor = "§e"; // Yellow
    private String predictionColor = "§a"; // Green
    private boolean showPearlCount = true;
    private boolean showCooldown = true;
    
    // Server-specific bypasses
    private boolean useHypixelBypass = true;
    private boolean useFuntimeBypass = true;
    private boolean useMatrixBypass = true;
    private boolean useNCPBypass = true;
    private boolean useAACBypass = true;
    private boolean useGrimBypass = true;
    private boolean useVerusBypass = true;
    private boolean useVulcanBypass = true;
    private boolean useSpartanBypass = true;
    private boolean useIntaveBypass = true;
    
    // Anti-detection settings
    private boolean randomizeActions = true;
    private boolean humanLikeBehavior = true;
    private boolean varyTiming = true;
    private boolean randomizeAim = true;
    private int actionDelay = 100; // Base delay between actions
    private double aimAccuracy = 0.8; // 80% accuracy
    
    // Status tracking
    private int pearlsThrown = 0;
    private int pearlsCaught = 0;
    private int successfulThrows = 0;
    private int successfulCatches = 0;
    private int failedThrows = 0;
    private int failedCatches = 0;
    private long lastThrowTime = 0;
    private long lastCatchTime = 0;
    private boolean isThrowing = false;
    private boolean isCatching = false;
    
    private Random random = new Random();
    
    public ClickPearl() {
        super("ClickPearl", "Advanced ender pearl throwing and catching system", Module.Category.PLAYER, 0);
        
        initializeDefaultSettings();
    }
    
    @Override
    protected void onEnable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.enableFuntimeBypass("ClickPearl");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.enableMatrixBypass("ClickPearl");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.enableHypixelBypass("ClickPearl");
        }
        if (useNCPBypass) {
            AntiDetectionManager.enableNCPBypass("ClickPearl");
        }
        if (useAACBypass) {
            AntiDetectionManager.enableAACBypass("ClickPearl");
        }
        if (useGrimBypass) {
            AntiDetectionManager.enableGrimBypass("ClickPearl");
        }
        if (useVerusBypass) {
            AntiDetectionManager.enableVerusBypass("ClickPearl");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.enableVulcanBypass("ClickPearl");
        }
        if (useSpartanBypass) {
            AntiDetectionManager.enableSpartanBypass("ClickPearl");
        }
        if (useIntaveBypass) {
            AntiDetectionManager.enableIntaveBypass("ClickPearl");
        }
        
        Logger.info("ClickPearl enabled - Advanced pearl system active");
        Logger.info("Pearls: " + pearlCount + "/" + maxPearls);
    }
    
    @Override
    protected void onDisable() {
        if (useFuntimeBypass) {
            AntiDetectionManager.disableFuntimeBypass("ClickPearl");
        }
        if (useMatrixBypass) {
            AntiDetectionManager.disableMatrixBypass("ClickPearl");
        }
        if (useHypixelBypass) {
            AntiDetectionManager.disableHypixelBypass("ClickPearl");
        }
        if (useNCPBypass) {
            AntiDetectionManager.disableNCPBypass("ClickPearl");
        }
        if (useAACBypass) {
            AntiDetectionManager.disableAACBypass("ClickPearl");
        }
        if (useGrimBypass) {
            AntiDetectionManager.disableGrimBypass("ClickPearl");
        }
        if (useVerusBypass) {
            AntiDetectionManager.disableVerusBypass("ClickPearl");
        }
        if (useVulcanBypass) {
            AntiDetectionManager.disableVulcanBypass("ClickPearl");
        }
        if (useSpartanBypass) {
            AntiDetectionManager.disableSpartanBypass("ClickPearl");
        }
        if (useIntaveBypass) {
            AntiDetectionManager.disableIntaveBypass("ClickPearl");
        }
        
        isThrowing = false;
        isCatching = false;
        Logger.info("ClickPearl disabled - Session stats:");
        Logger.info("Pearls thrown: " + pearlsThrown + " (Success: " + successfulThrows + ", Failed: " + failedThrows + ")");
        Logger.info("Pearls caught: " + pearlsCaught + " (Success: " + successfulCatches + ", Failed: " + failedCatches + ")");
    }
    
    public void onTick() {
        if (!isEnabled() || !clickPearlEnabled) {
            return;
        }
        
        // Check for throw opportunities
        if (autoThrow && canThrow()) {
            checkThrowOpportunities();
        }
        
        // Check for catch opportunities
        if (autoCatch && canCatch()) {
            checkCatchOpportunities();
        }
        
        // Update pearl count
        updatePearlCount();
    }
    
    public void onRender() {
        if (!isEnabled() || !clickPearlEnabled) {
            return;
        }
        
        // Render pearl trajectory
        if (showPearlTrajectory) {
            renderPearlTrajectory();
        }
        
        // Render pearl prediction
        if (showPearlPrediction) {
            renderPearlPrediction();
        }
        
        // Render catch zone
        if (showCatchZone) {
            renderCatchZone();
        }
        
        // Render throw zone
        if (showThrowZone) {
            renderThrowZone();
        }
        
        // Render pearl count
        if (showPearlCount) {
            renderPearlCount();
        }
        
        // Render cooldown
        if (showCooldown) {
            renderCooldown();
        }
    }
    
    private void checkThrowOpportunities() {
        // Find targets to throw pearls at
        List<Target> targets = findThrowTargets();
        
        for (Target target : targets) {
            if (shouldThrowAt(target)) {
                throwPearlAt(target);
                break; // Only throw one pearl per tick
            }
        }
    }
    
    private void checkCatchOpportunities() {
        // Find pearls to catch
        List<Pearl> pearls = findCatchablePearls();
        
        for (Pearl pearl : pearls) {
            if (shouldCatch(pearl)) {
                catchPearl(pearl);
                break; // Only catch one pearl per tick
            }
        }
    }
    
    private List<Target> findThrowTargets() {
        // Simulate finding throw targets
        // In real implementation, this would scan for nearby entities
        List<Target> targets = new ArrayList<>();
        
        if (random.nextDouble() < 0.3) { // 30% chance per tick
            Target target = new Target();
            target.setName("Player" + random.nextInt(10));
            target.setDistance(10 + random.nextInt(40));
            target.setType(TargetType.PLAYER);
            target.setHealth(10 + random.nextInt(10));
            targets.add(target);
        }
        
        return targets;
    }
    
    private List<Pearl> findCatchablePearls() {
        // Simulate finding catchable pearls
        // In real implementation, this would scan for nearby pearls
        List<Pearl> pearls = new ArrayList<>();
        
        if (random.nextDouble() < 0.2) { // 20% chance per tick
            Pearl pearl = new Pearl();
            pearl.setDistance(5 + random.nextInt(25));
            pearl.setVelocity(0.5 + random.nextDouble());
            pearl.setOwner("Player" + random.nextInt(10));
            pearls.add(pearl);
        }
        
        return pearls;
    }
    
    private boolean shouldThrowAt(Target target) {
        // Check if we should throw pearl at this target
        if (target.getDistance() > throwRange) {
            return false;
        }
        
        if (target.getHealth() <= 0) {
            return false;
        }
        
        // Check target type
        switch (target.getType()) {
            case PLAYER:
                return throwAtPlayers;
            case MOB:
                return throwAtMobs;
            case ANIMAL:
                return throwAtAnimals;
            default:
                return false;
        }
    }
    
    private boolean shouldCatch(Pearl pearl) {
        // Check if we should catch this pearl
        if (pearl.getDistance() > catchRange) {
            return false;
        }
        
        // Check if pearl is coming towards us
        if (pearl.getVelocity() < 0.1) {
            return false;
        }
        
        return true;
    }
    
    private void throwPearlAt(Target target) {
        if (isThrowing) {
            return; // Already throwing
        }
        
        if (pearlCount <= 0) {
            Logger.warn("No pearls available to throw");
            return;
        }
        
        isThrowing = true;
        pearlsThrown++;
        pearlCount--;
        
        // Apply anti-detection
        applyAntiDetection();
        
        // Calculate throw angle and velocity
        ThrowData throwData = calculateThrow(target);
        
        // Execute throw
        if (executeThrow(throwData)) {
            successfulThrows++;
            Logger.info("Successfully threw pearl at " + target.getName());
        } else {
            failedThrows++;
            Logger.warn("Failed to throw pearl at " + target.getName());
        }
        
        lastThrowTime = System.currentTimeMillis();
        isThrowing = false;
    }
    
    private void catchPearl(Pearl pearl) {
        if (isCatching) {
            return; // Already catching
        }
        
        isCatching = true;
        pearlsCaught++;
        pearlCount++;
        
        // Apply anti-detection
        applyAntiDetection();
        
        // Calculate catch timing
        CatchData catchData = calculateCatch(pearl);
        
        // Execute catch
        if (executeCatch(catchData)) {
            successfulCatches++;
            Logger.info("Successfully caught pearl from " + pearl.getOwner());
        } else {
            failedCatches++;
            Logger.warn("Failed to catch pearl from " + pearl.getOwner());
        }
        
        lastCatchTime = System.currentTimeMillis();
        isCatching = false;
    }
    
    private ThrowData calculateThrow(Target target) {
        ThrowData data = new ThrowData();
        
        // Calculate basic throw angle
        data.setAngle(45 + random.nextInt(45)); // 45-90 degrees
        
        // Calculate velocity based on distance
        data.setVelocity(0.5 + (target.getDistance() / 100.0));
        
        // Apply prediction if enabled
        if (predictMovement && leadTarget) {
            data.setLeadDistance(calculateLeadDistance(target));
        }
        
        // Apply randomization for anti-detection
        if (randomizeAim) {
            data.setAngle(data.getAngle() + (random.nextGaussian() * 5));
            data.setVelocity(data.getVelocity() + (random.nextGaussian() * 0.1));
        }
        
        return data;
    }
    
    private CatchData calculateCatch(Pearl pearl) {
        CatchData data = new CatchData();
        
        // Calculate catch timing
        data.setTiming(pearl.getDistance() / pearl.getVelocity());
        
        // Calculate catch position
        data.setPosition(calculateCatchPosition(pearl));
        
        // Apply prediction if enabled
        if (predictPearlPath) {
            data.setPredictedPosition(calculatePredictedPosition(pearl));
        }
        
        return data;
    }
    
    private double calculateLeadDistance(Target target) {
        // Calculate how far ahead to aim based on target movement
        // In real implementation, this would analyze target velocity
        return target.getDistance() * 0.1; // 10% of distance
    }
    
    private double calculateCatchPosition(Pearl pearl) {
        // Calculate optimal catch position
        // In real implementation, this would calculate 3D position
        return pearl.getDistance() * 0.5; // Midpoint
    }
    
    private double calculatePredictedPosition(Pearl pearl) {
        // Calculate predicted pearl position
        // In real implementation, this would predict 3D trajectory
        return pearl.getDistance() * 0.8; // 80% of distance
    }
    
    private boolean executeThrow(ThrowData data) {
        // Simulate throw execution
        // In real implementation, this would execute the actual throw
        
        // Apply accuracy
        double accuracy = random.nextDouble();
        if (accuracy > aimAccuracy) {
            return false; // Miss
        }
        
        // Simulate throw delay
        try {
            Thread.sleep(instantThrow ? 0 : 100 + random.nextInt(100));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return true; // Success
    }
    
    private boolean executeCatch(CatchData data) {
        // Simulate catch execution
        // In real implementation, this would execute the actual catch
        
        // Apply accuracy
        double accuracy = random.nextDouble();
        if (accuracy > aimAccuracy) {
            return false; // Miss
        }
        
        // Simulate catch delay
        try {
            Thread.sleep(instantCatch ? 0 : 50 + random.nextInt(50));
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        return true; // Success
    }
    
    private void updatePearlCount() {
        // Simulate pearl count update
        // In real implementation, this would check actual inventory
        
        if (autoRefill && pearlCount < minPearls) {
            // Try to refill pearls
            if (random.nextDouble() < 0.1) { // 10% chance per tick
                pearlCount = Math.min(maxPearls, pearlCount + 1);
                Logger.debug("Refilled pearl, count: " + pearlCount);
            }
        }
    }
    
    private boolean canThrow() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastThrowTime >= throwCooldown;
    }
    
    private boolean canCatch() {
        long currentTime = System.currentTimeMillis();
        return currentTime - lastCatchTime >= catchCooldown;
    }
    
    private void renderPearlTrajectory() {
        // Render pearl trajectory
        // In real implementation, this would render 3D trajectory
        Logger.debug("Rendering pearl trajectory");
    }
    
    private void renderPearlPrediction() {
        // Render pearl prediction
        // In real implementation, this would render predicted path
        Logger.debug("Rendering pearl prediction");
    }
    
    private void renderCatchZone() {
        // Render catch zone
        // In real implementation, this would render catch area
        Logger.debug("Rendering catch zone");
    }
    
    private void renderThrowZone() {
        // Render throw zone
        // In real implementation, this would render throw area
        Logger.debug("Rendering throw zone");
    }
    
    private void renderPearlCount() {
        // Render pearl count
        // In real implementation, this would render count on screen
        Logger.debug("Pearl count: " + pearlCount + "/" + maxPearls);
    }
    
    private void renderCooldown() {
        // Render cooldown
        // In real implementation, this would render cooldown timer
        long throwCooldownLeft = Math.max(0, throwCooldown - (System.currentTimeMillis() - lastThrowTime));
        long catchCooldownLeft = Math.max(0, catchCooldown - (System.currentTimeMillis() - lastCatchTime));
        Logger.debug("Throw cooldown: " + throwCooldownLeft + "ms, Catch cooldown: " + catchCooldownLeft + "ms");
    }
    
    private void applyAntiDetection() {
        if (humanLikeBehavior) {
            // Simulate human-like behavior
            try {
                Thread.sleep(actionDelay + random.nextInt(50));
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            }
        }
        
        if (randomizeActions) {
            // Randomize actions
            String[] actions = {"aim", "throw", "catch", "wait"};
            String action = actions[random.nextInt(actions.length)];
            Logger.debug("Pearl action: " + action);
        }
    }
    
    private void initializeDefaultSettings() {
        pearlCount = 8; // Start with 8 pearls
    }
    
    // Getters and setters
    public boolean isClickPearlEnabled() { return clickPearlEnabled; }
    public void setClickPearlEnabled(boolean clickPearlEnabled) { this.clickPearlEnabled = clickPearlEnabled; }
    
    public boolean isAutoThrow() { return autoThrow; }
    public void setAutoThrow(boolean autoThrow) { this.autoThrow = autoThrow; }
    
    public boolean isAutoCatch() { return autoCatch; }
    public void setAutoCatch(boolean autoCatch) { this.autoCatch = autoCatch; }
    
    public boolean isSmartThrow() { return smartThrow; }
    public void setSmartThrow(boolean smartThrow) { this.smartThrow = smartThrow; }
    
    public boolean isSmartCatch() { return smartCatch; }
    public void setSmartCatch(boolean smartCatch) { this.smartCatch = smartCatch; }
    
    public boolean isInstantThrow() { return instantThrow; }
    public void setInstantThrow(boolean instantThrow) { this.instantThrow = instantThrow; }
    
    public boolean isInstantCatch() { return instantCatch; }
    public void setInstantCatch(boolean instantCatch) { this.instantCatch = instantCatch; }
    
    public int getPearlCount() { return pearlCount; }
    public void setPearlCount(int pearlCount) { this.pearlCount = Math.max(0, Math.min(maxPearls, pearlCount)); }
    
    public int getMaxPearls() { return maxPearls; }
    public void setMaxPearls(int maxPearls) { this.maxPearls = Math.max(1, maxPearls); }
    
    public int getMinPearls() { return minPearls; }
    public void setMinPearls(int minPearls) { this.minPearls = Math.max(0, minPearls); }
    
    public boolean isAutoRefill() { return autoRefill; }
    public void setAutoRefill(boolean autoRefill) { this.autoRefill = autoRefill; }
    
    public boolean isPrioritizePearls() { return prioritizePearls; }
    public void setPrioritizePearls(boolean prioritizePearls) { this.prioritizePearls = prioritizePearls; }
    
    public boolean isDropOnLowHealth() { return dropOnLowHealth; }
    public void setDropOnLowHealth(boolean dropOnLowHealth) { this.dropOnLowHealth = dropOnLowHealth; }
    
    public int getLowHealthThreshold() { return lowHealthThreshold; }
    public void setLowHealthThreshold(int lowHealthThreshold) { this.lowHealthThreshold = Math.max(1, lowHealthThreshold); }
    
    // Throw settings
    public boolean isThrowAtEnemies() { return throwAtEnemies; }
    public void setThrowAtEnemies(boolean throwAtEnemies) { this.throwAtEnemies = throwAtEnemies; }
    
    public boolean isThrowAtFriends() { return throwAtFriends; }
    public void setThrowAtFriends(boolean throwAtFriends) { this.throwAtFriends = throwAtFriends; }
    
    public boolean isThrowAtNeutrals() { return throwAtNeutrals; }
    public void setThrowAtNeutrals(boolean throwAtNeutrals) { this.throwAtNeutrals = throwAtNeutrals; }
    
    public boolean isThrowAtPlayers() { return throwAtPlayers; }
    public void setThrowAtPlayers(boolean throwAtPlayers) { this.throwAtPlayers = throwAtPlayers; }
    
    public boolean isThrowAtMobs() { return throwAtMobs; }
    public void setThrowAtMobs(boolean throwAtMobs) { this.throwAtMobs = throwAtMobs; }
    
    public boolean isThrowAtAnimals() { return throwAtAnimals; }
    public void setThrowAtAnimals(boolean throwAtAnimals) { this.throwAtAnimals = throwAtAnimals; }
    
    public int getThrowRange() { return throwRange; }
    public void setThrowRange(int throwRange) { this.throwRange = Math.max(1, throwRange); }
    
    public int getThrowCooldown() { return throwCooldown; }
    public void setThrowCooldown(int throwCooldown) { this.throwCooldown = Math.max(0, throwCooldown); }
    
    public boolean isPredictMovement() { return predictMovement; }
    public void setPredictMovement(boolean predictMovement) { this.predictMovement = predictMovement; }
    
    public boolean isLeadTarget() { return leadTarget; }
    public void setLeadTarget(boolean leadTarget) { this.leadTarget = leadTarget; }
    
    // Catch settings
    public boolean isCatchFromEnemies() { return catchFromEnemies; }
    public void setCatchFromEnemies(boolean catchFromEnemies) { this.catchFromEnemies = catchFromEnemies; }
    
    public boolean isCatchFromFriends() { return catchFromFriends; }
    public void setCatchFromFriends(boolean catchFromFriends) { this.catchFromFriends = catchFromFriends; }
    
    public boolean isCatchFromNeutrals() { return catchFromNeutrals; }
    public void setCatchFromNeutrals(boolean catchFromNeutrals) { this.catchFromNeutrals = catchFromNeutrals; }
    
    public boolean isCatchFromPlayers() { return catchFromPlayers; }
    public void setCatchFromPlayers(boolean catchFromPlayers) { this.catchFromPlayers = catchFromPlayers; }
    
    public boolean isCatchFromMobs() { return catchFromMobs; }
    public void setCatchFromMobs(boolean catchFromMobs) { this.catchFromMobs = catchFromMobs; }
    
    public boolean isCatchFromAnimals() { return catchFromAnimals; }
    public void setCatchFromAnimals(boolean catchFromAnimals) { this.catchFromAnimals = catchFromAnimals; }
    
    public int getCatchRange() { return catchRange; }
    public void setCatchRange(int catchRange) { this.catchRange = Math.max(1, catchRange); }
    
    public int getCatchCooldown() { return catchCooldown; }
    public void setCatchCooldown(int catchCooldown) { this.catchCooldown = Math.max(0, catchCooldown); }
    
    public boolean isPredictPearlPath() { return predictPearlPath; }
    public void setPredictPearlPath(boolean predictPearlPath) { this.predictPearlPath = predictPearlPath; }
    
    public boolean isSmartCatching() { return smartCatching; }
    public void setSmartCatching(boolean smartCatching) { this.smartCatching = smartCatching; }
    
    // Visual settings
    public boolean isShowPearlTrajectory() { return showPearlTrajectory; }
    public void setShowPearlTrajectory(boolean showPearlTrajectory) { this.showPearlTrajectory = showPearlTrajectory; }
    
    public boolean isShowPearlPrediction() { return showPearlPrediction; }
    public void setShowPearlPrediction(boolean showPearlPrediction) { this.showPearlPrediction = showPearlPrediction; }
    
    public boolean isShowCatchZone() { return showCatchZone; }
    public void setShowCatchZone(boolean showCatchZone) { this.showCatchZone = showCatchZone; }
    
    public boolean isShowThrowZone() { return showThrowZone; }
    public void setShowThrowZone(boolean showThrowZone) { this.showThrowZone = showThrowZone; }
    
    public String getPearlColor() { return pearlColor; }
    public void setPearlColor(String pearlColor) { this.pearlColor = pearlColor; }
    
    public String getTrajectoryColor() { return trajectoryColor; }
    public void setTrajectoryColor(String trajectoryColor) { this.trajectoryColor = trajectoryColor; }
    
    public String getPredictionColor() { return predictionColor; }
    public void setPredictionColor(String predictionColor) { this.predictionColor = predictionColor; }
    
    public boolean isShowPearlCount() { return showPearlCount; }
    public void setShowPearlCount(boolean showPearlCount) { this.showPearlCount = showPearlCount; }
    
    public boolean isShowCooldown() { return showCooldown; }
    public void setShowCooldown(boolean showCooldown) { this.showCooldown = showCooldown; }
    
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
    
    public boolean isUseSpartanBypass() { return useSpartanBypass; }
    public void setUseSpartanBypass(boolean useSpartanBypass) { this.useSpartanBypass = useSpartanBypass; }
    
    public boolean isUseIntaveBypass() { return useIntaveBypass; }
    public void setUseIntaveBypass(boolean useIntaveBypass) { this.useIntaveBypass = useIntaveBypass; }
    
    public boolean isRandomizeActions() { return randomizeActions; }
    public void setRandomizeActions(boolean randomizeActions) { this.randomizeActions = randomizeActions; }
    
    public boolean isHumanLikeBehavior() { return humanLikeBehavior; }
    public void setHumanLikeBehavior(boolean humanLikeBehavior) { this.humanLikeBehavior = humanLikeBehavior; }
    
    public boolean isVaryTiming() { return varyTiming; }
    public void setVaryTiming(boolean varyTiming) { this.varyTiming = varyTiming; }
    
    public boolean isRandomizeAim() { return randomizeAim; }
    public void setRandomizeAim(boolean randomizeAim) { this.randomizeAim = randomizeAim; }
    
    public int getActionDelay() { return actionDelay; }
    public void setActionDelay(int actionDelay) { this.actionDelay = Math.max(10, actionDelay); }
    
    public double getAimAccuracy() { return aimAccuracy; }
    public void setAimAccuracy(double aimAccuracy) { this.aimAccuracy = Math.max(0.0, Math.min(1.0, aimAccuracy)); }
    
    // Status getters
    public int getPearlsThrown() { return pearlsThrown; }
    public int getPearlsCaught() { return pearlsCaught; }
    public int getSuccessfulThrows() { return successfulThrows; }
    public int getSuccessfulCatches() { return successfulCatches; }
    public int getFailedThrows() { return failedThrows; }
    public int getFailedCatches() { return failedCatches; }
    public long getLastThrowTime() { return lastThrowTime; }
    public long getLastCatchTime() { return lastCatchTime; }
    public boolean isThrowing() { return isThrowing; }
    public boolean isCatching() { return isCatching; }
    
    public void resetStats() {
        pearlsThrown = 0;
        pearlsCaught = 0;
        successfulThrows = 0;
        successfulCatches = 0;
        failedThrows = 0;
        failedCatches = 0;
        Logger.info("ClickPearl statistics reset");
    }
    
    // Inner classes
    public static class Target {
        private String name;
        private double distance;
        private TargetType type;
        private int health;
        
        public String getName() { return name; }
        public void setName(String name) { this.name = name; }
        
        public double getDistance() { return distance; }
        public void setDistance(double distance) { this.distance = distance; }
        
        public TargetType getType() { return type; }
        public void setType(TargetType type) { this.type = type; }
        
        public int getHealth() { return health; }
        public void setHealth(int health) { this.health = health; }
    }
    
    public static class Pearl {
        private double distance;
        private double velocity;
        private String owner;
        
        public double getDistance() { return distance; }
        public void setDistance(double distance) { this.distance = distance; }
        
        public double getVelocity() { return velocity; }
        public void setVelocity(double velocity) { this.velocity = velocity; }
        
        public String getOwner() { return owner; }
        public void setOwner(String owner) { this.owner = owner; }
    }
    
    public static class ThrowData {
        private double angle;
        private double velocity;
        private double leadDistance;
        
        public double getAngle() { return angle; }
        public void setAngle(double angle) { this.angle = angle; }
        
        public double getVelocity() { return velocity; }
        public void setVelocity(double velocity) { this.velocity = velocity; }
        
        public double getLeadDistance() { return leadDistance; }
        public void setLeadDistance(double leadDistance) { this.leadDistance = leadDistance; }
    }
    
    public static class CatchData {
        private double timing;
        private double position;
        private double predictedPosition;
        
        public double getTiming() { return timing; }
        public void setTiming(double timing) { this.timing = timing; }
        
        public double getPosition() { return position; }
        public void setPosition(double position) { this.position = position; }
        
        public double getPredictedPosition() { return predictedPosition; }
        public void setPredictedPosition(double predictedPosition) { this.predictedPosition = predictedPosition; }
    }
    
    public enum TargetType {
        PLAYER,
        MOB,
        ANIMAL
    }
}