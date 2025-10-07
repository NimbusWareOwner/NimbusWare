package com.example.nimbusware.plugins.example;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.plugins.Plugin;
import com.example.nimbusware.plugins.PluginConfig;
import com.example.nimbusware.utils.Logger;

/**
 * Example plugin demonstrating the plugin system
 */
public class ExamplePlugin implements Plugin {
    private NimbusWare nimbusWare;
    private boolean enabled = false;
    private PluginConfig config;
    
    @Override
    public String getName() {
        return "ExamplePlugin";
    }
    
    @Override
    public String getVersion() {
        return "1.0.0";
    }
    
    @Override
    public String getAuthor() {
        return "NimbusWare Team";
    }
    
    @Override
    public String getDescription() {
        return "An example plugin demonstrating the plugin system capabilities";
    }
    
    @Override
    public boolean initialize(NimbusWare nimbusWare) {
        this.nimbusWare = nimbusWare;
        this.config = new PluginConfig();
        
        // Set default configuration
        config.set("enabled", true);
        config.set("message", "Hello from ExamplePlugin!");
        config.set("interval", 5000);
        
        Logger.info("ExamplePlugin initialized successfully");
        return true;
    }
    
    @Override
    public void onEnable() {
        enabled = true;
        Logger.info("ExamplePlugin enabled");
        
        // Start a simple background task
        startBackgroundTask();
    }
    
    @Override
    public void onDisable() {
        enabled = false;
        Logger.info("ExamplePlugin disabled");
    }
    
    @Override
    public void onUnload() {
        Logger.info("ExamplePlugin unloaded");
    }
    
    @Override
    public boolean isEnabled() {
        return enabled;
    }
    
    @Override
    public String[] getDependencies() {
        return new String[0]; // No dependencies
    }
    
    @Override
    public PluginConfig getConfig() {
        return config;
    }
    
    @Override
    public String handleCommand(String command, String[] args) {
        switch (command.toLowerCase()) {
            case "hello":
                return "Hello from ExamplePlugin!";
            case "status":
                return "Plugin status: " + (enabled ? "enabled" : "disabled");
            case "config":
                return "Config: " + config.getAll();
            case "info":
                return String.format("Plugin: %s v%s by %s", getName(), getVersion(), getAuthor());
            default:
                return "Unknown command: " + command;
        }
    }
    
    @Override
    public String[] getCommands() {
        return new String[]{"hello", "status", "config", "info"};
    }
    
    private void startBackgroundTask() {
        Thread backgroundThread = new Thread(() -> {
            while (enabled) {
                try {
                    Thread.sleep(config.getInt("interval", 5000));
                    
                    if (enabled) {
                        String message = config.getString("message", "Hello from ExamplePlugin!");
                        Logger.info("[ExamplePlugin] " + message);
                    }
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                    break;
                }
            }
        }, "ExamplePlugin-Background");
        
        backgroundThread.setDaemon(true);
        backgroundThread.start();
    }
}