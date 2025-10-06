package com.example.cheatclient.core;

import com.example.cheatclient.features.*;
import com.example.cheatclient.utils.Logger;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ModuleManager {
    private final List<Module> modules = new ArrayList<>();
    private final Map<String, Module> moduleMap = new HashMap<>();
    
    public void registerModules() {
        Logger.info("Registering modules...");
        
        // Movement modules
        register(new Sprint());
        register(new WaterSpeed());
        register(new Spider());
        
        // Render modules
        register(new XRay());
        register(new ESP());
        register(new Tracers());
        register(new Fullbright());
        register(new NoRender());
        register(new GPS());
        
        // Combat modules
        register(new KillAura());
        register(new AutoClicker());
        register(new AutoEat());
        register(new AutoTool());
        register(new TriggerBot());
        register(new ClickFriends());
        register(new NoFriendDamage());
        register(new ClickPearl());
        
        // Player modules
        register(new AutoFish());
        register(new AutoMine());
        register(new AutoFarm());
        register(new AutoBuild());
        register(new AutoCreeperFarm());
        register(new ChestStealer());
        register(new ChestStealerAdvanced());
        register(new SwordCraft());
        
        // World modules
        register(new AutoWalk());
        register(new AutoJump());
        
        // Misc modules
        register(new AutoLog());
        register(new AutoRespawn());
        register(new AutoReconnect());
        register(new ChatBot());
        register(new AntiAFK());
        register(new AutoBuy());
        register(new AutoBuyAdvanced());
        register(new AccountConnector());
        
        Logger.info("Registered " + modules.size() + " modules");
    }
    
    private void register(Module module) {
        modules.add(module);
        moduleMap.put(module.getName().toLowerCase(), module);
    }
    
    public List<Module> getModules() {
        return new ArrayList<>(modules);
    }
    
    public List<Module> getModulesByCategory(Module.Category category) {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.getCategory() == category) {
                result.add(module);
            }
        }
        return result;
    }
    
    public Module getModule(String name) {
        return moduleMap.get(name.toLowerCase());
    }
    
    public Module getModule(Class<? extends Module> clazz) {
        for (Module module : modules) {
            if (module.getClass() == clazz) {
                return module;
            }
        }
        return null;
    }
    
    public List<Module> getEnabledModules() {
        List<Module> result = new ArrayList<>();
        for (Module module : modules) {
            if (module.isEnabled()) {
                result.add(module);
            }
        }
        return result;
    }
}