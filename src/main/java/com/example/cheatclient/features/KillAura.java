package com.example.cheatclient.features;

import com.example.cheatclient.core.Module;
import net.minecraft.entity.Entity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.mob.MobEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.EntityHitResult;
import net.minecraft.util.hit.HitResult;

public class KillAura extends Module {
    private double range = 4.0;
    private boolean targetPlayers = true;
    private boolean targetMobs = true;
    private boolean targetAnimals = false;
    private int attackDelay = 20; // ticks
    private int lastAttack = 0;
    
    public KillAura() {
        super("KillAura", "Automatically attacks nearby entities", Module.Category.COMBAT, 0);
    }
    
    @Override
    protected void onEnable() {
        // No special setup needed
    }
    
    @Override
    protected void onDisable() {
        // No cleanup needed
    }
    
    public void onTick() {
        if (!isEnabled() || CheatClient.mc.player == null || CheatClient.mc.world == null) {
            return;
        }
        
        // Check attack delay
        if (CheatClient.mc.player.age - lastAttack < attackDelay) {
            return;
        }
        
        // Find target
        Entity target = findTarget();
        if (target != null) {
            attack(target);
        }
    }
    
    private Entity findTarget() {
        Entity closest = null;
        double closestDistance = range;
        
        for (Entity entity : CheatClient.mc.world.getEntities()) {
            if (entity == CheatClient.mc.player) continue;
            if (!(entity instanceof LivingEntity)) continue;
            if (!shouldTarget(entity)) continue;
            
            double distance = CheatClient.mc.player.distanceTo(entity);
            if (distance <= range && distance < closestDistance) {
                closest = entity;
                closestDistance = distance;
            }
        }
        
        return closest;
    }
    
    private boolean shouldTarget(Entity entity) {
        if (entity instanceof PlayerEntity) {
            return targetPlayers;
        } else if (entity instanceof MobEntity) {
            return targetMobs;
        } else {
            return targetAnimals;
        }
    }
    
    private void attack(Entity target) {
        if (CheatClient.mc.crosshairTarget != null && 
            CheatClient.mc.crosshairTarget.getType() == HitResult.Type.ENTITY) {
            
            EntityHitResult hitResult = (EntityHitResult) CheatClient.mc.crosshairTarget;
            if (hitResult.getEntity() == target) {
                CheatClient.mc.interactionManager.attackEntity(CheatClient.mc.player, target);
                CheatClient.mc.player.swingHand(Hand.MAIN_HAND);
                lastAttack = CheatClient.mc.player.age;
            }
        }
    }
    
    public double getRange() {
        return range;
    }
    
    public void setRange(double range) {
        this.range = range;
    }
    
    public boolean isTargetPlayers() {
        return targetPlayers;
    }
    
    public void setTargetPlayers(boolean targetPlayers) {
        this.targetPlayers = targetPlayers;
    }
    
    public boolean isTargetMobs() {
        return targetMobs;
    }
    
    public void setTargetMobs(boolean targetMobs) {
        this.targetMobs = targetMobs;
    }
    
    public boolean isTargetAnimals() {
        return targetAnimals;
    }
    
    public void setTargetAnimals(boolean targetAnimals) {
        this.targetAnimals = targetAnimals;
    }
    
    public int getAttackDelay() {
        return attackDelay;
    }
    
    public void setAttackDelay(int attackDelay) {
        this.attackDelay = attackDelay;
    }
}