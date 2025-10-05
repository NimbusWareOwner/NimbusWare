package com.example.minecraftcheat;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.BoolArgumentType;
import com.mojang.brigadier.arguments.FloatArgumentType;
import com.mojang.brigadier.context.CommandContext;
import net.fabricmc.fabric.api.client.command.v2.ClientCommandManager;
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource;
import net.minecraft.text.Text;

public class CheatCommand {
    
    public static void register(CommandDispatcher<FabricClientCommandSource> dispatcher) {
        dispatcher.register(ClientCommandManager.literal("cheat")
            .then(ClientCommandManager.literal("fly")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(CheatCommand::toggleFly)))
            .then(ClientCommandManager.literal("speed")
                .then(ClientCommandManager.argument("multiplier", FloatArgumentType.floatArg(0.1f, 10.0f))
                    .executes(CheatCommand::setSpeed)))
            .then(ClientCommandManager.literal("xray")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(CheatCommand::toggleXray)))
            .then(ClientCommandManager.literal("nofall")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(CheatCommand::toggleNoFall)))
            .then(ClientCommandManager.literal("autosprint")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(CheatCommand::toggleAutoSprint)))
            .then(ClientCommandManager.literal("killaura")
                .then(ClientCommandManager.argument("enabled", BoolArgumentType.bool())
                    .executes(CheatCommand::toggleKillAura)))
            .then(ClientCommandManager.literal("save")
                .executes(CheatCommand::saveConfig))
            .then(ClientCommandManager.literal("status")
                .executes(CheatCommand::showStatus)));
    }
    
    private static int toggleFly(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        MinecraftCheatMod.config.flyEnabled = enabled;
        context.getSource().sendFeedback(Text.literal("Fly " + (enabled ? "enabled" : "disabled")));
        return 1;
    }
    
    private static int setSpeed(CommandContext<FabricClientCommandSource> context) {
        float multiplier = FloatArgumentType.getFloat(context, "multiplier");
        MinecraftCheatMod.config.speedMultiplier = multiplier;
        MinecraftCheatMod.config.speedEnabled = true;
        context.getSource().sendFeedback(Text.literal("Speed set to " + multiplier + "x"));
        return 1;
    }
    
    private static int toggleXray(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        MinecraftCheatMod.config.xrayEnabled = enabled;
        context.getSource().sendFeedback(Text.literal("X-Ray " + (enabled ? "enabled" : "disabled")));
        return 1;
    }
    
    private static int toggleNoFall(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        MinecraftCheatMod.config.noFallEnabled = enabled;
        context.getSource().sendFeedback(Text.literal("No Fall " + (enabled ? "enabled" : "disabled")));
        return 1;
    }
    
    private static int toggleAutoSprint(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        MinecraftCheatMod.config.autoSprintEnabled = enabled;
        context.getSource().sendFeedback(Text.literal("Auto Sprint " + (enabled ? "enabled" : "disabled")));
        return 1;
    }
    
    private static int toggleKillAura(CommandContext<FabricClientCommandSource> context) {
        boolean enabled = BoolArgumentType.getBool(context, "enabled");
        MinecraftCheatMod.config.killAuraEnabled = enabled;
        context.getSource().sendFeedback(Text.literal("Kill Aura " + (enabled ? "enabled" : "disabled")));
        return 1;
    }
    
    private static int saveConfig(CommandContext<FabricClientCommandSource> context) {
        MinecraftCheatMod.config.save();
        context.getSource().sendFeedback(Text.literal("Configuration saved"));
        return 1;
    }
    
    private static int showStatus(CommandContext<FabricClientCommandSource> context) {
        context.getSource().sendFeedback(Text.literal("=== Cheat Status ==="));
        context.getSource().sendFeedback(Text.literal("Fly: " + (MinecraftCheatMod.config.flyEnabled ? "ON" : "OFF")));
        context.getSource().sendFeedback(Text.literal("Speed: " + (MinecraftCheatMod.config.speedEnabled ? "ON (" + MinecraftCheatMod.config.speedMultiplier + "x)" : "OFF")));
        context.getSource().sendFeedback(Text.literal("X-Ray: " + (MinecraftCheatMod.config.xrayEnabled ? "ON" : "OFF")));
        context.getSource().sendFeedback(Text.literal("No Fall: " + (MinecraftCheatMod.config.noFallEnabled ? "ON" : "OFF")));
        context.getSource().sendFeedback(Text.literal("Auto Sprint: " + (MinecraftCheatMod.config.autoSprintEnabled ? "ON" : "OFF")));
        context.getSource().sendFeedback(Text.literal("Kill Aura: " + (MinecraftCheatMod.config.killAuraEnabled ? "ON" : "OFF")));
        return 1;
    }
}