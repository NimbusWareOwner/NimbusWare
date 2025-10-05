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
        register(new Fly());
        register(new Speed());
        register(new Sprint());
        register(new NoFall());
        register(new Step());
        register(new Jesus());
        
        // Render modules
        register(new XRay());
        register(new ESP());
        register(new Tracers());
        register(new Fullbright());
        register(new NoRender());
        
        // Combat modules
        register(new KillAura());
        register(new AutoClicker());
        register(new AutoArmor());
        register(new AutoEat());
        register(new AutoTool());
        
        // Player modules
        register(new AutoFish());
        register(new AutoMine());
        register(new AutoFarm());
        register(new AutoBuild());
        
        // World modules
        register(new AutoWalk());
        register(new AutoJump());
        register(new AutoPlace());
        register(new AutoBreak());
        
        // Misc modules
        register(new AutoLog());
        register(new AutoRespawn());
        register(new AutoReconnect());
        register(new ChatBot());
        
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