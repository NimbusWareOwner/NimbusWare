package com.example.minecraftcheat.mixins;

import com.example.minecraftcheat.MinecraftCheatMod;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.entity.player.PlayerAbilities;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPlayerEntity.class)
public class ClientPlayerEntityMixin {
    
    @Inject(method = "tick", at = @At("HEAD"))
    private void onTick(CallbackInfo ci) {
        ClientPlayerEntity player = (ClientPlayerEntity) (Object) this;
        
        // Apply fly cheat
        if (MinecraftCheatMod.config.flyEnabled) {
            PlayerAbilities abilities = player.getAbilities();
            abilities.allowFlying = true;
            abilities.flying = true;
        }
        
        // Apply speed cheat
        if (MinecraftCheatMod.config.speedEnabled) {
            PlayerAbilities abilities = player.getAbilities();
            abilities.setWalkSpeed(0.1f * MinecraftCheatMod.config.speedMultiplier);
        }
        
        // Apply no fall cheat
        if (MinecraftCheatMod.config.noFallEnabled) {
            player.fallDistance = 0.0f;
        }
        
        // Apply auto sprint cheat
        if (MinecraftCheatMod.config.autoSprintEnabled) {
            player.setSprinting(true);
        }
    }
}