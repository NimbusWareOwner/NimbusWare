package com.example.nimbusware.plugins;

import com.example.nimbusware.NimbusWare;

/**
 * Base interface for all NimbusWare plugins
 */
public interface Plugin {
    
    /**
     * Get the plugin name
     * @return Plugin name
     */
    String getName();
    
    /**
     * Get the plugin version
     * @return Plugin version
     */
    String getVersion();
    
    /**
     * Get the plugin author
     * @return Plugin author
     */
    String getAuthor();
    
    /**
     * Get the plugin description
     * @return Plugin description
     */
    String getDescription();
    
    /**
     * Initialize the plugin
     * @param nimbusWare NimbusWare instance
     * @return true if initialization successful
     */
    boolean initialize(NimbusWare nimbusWare);
    
    /**
     * Called when the plugin is enabled
     */
    void onEnable();
    
    /**
     * Called when the plugin is disabled
     */
    void onDisable();
    
    /**
     * Called when the plugin is unloaded
     */
    void onUnload();
    
    /**
     * Check if the plugin is enabled
     * @return true if enabled
     */
    boolean isEnabled();
    
    /**
     * Get plugin dependencies
     * @return Array of required plugin names
     */
    default String[] getDependencies() {
        return new String[0];
    }
    
    /**
     * Get plugin configuration
     * @return Plugin configuration object
     */
    default PluginConfig getConfig() {
        return new PluginConfig();
    }
    
    /**
     * Handle plugin command
     * @param command Command name
     * @param args Command arguments
     * @return Command result
     */
    default String handleCommand(String command, String[] args) {
        return "Command not implemented";
    }
    
    /**
     * Get available commands
     * @return Array of command names
     */
    default String[] getCommands() {
        return new String[0];
    }
}