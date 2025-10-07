package com.example.nimbusware.plugins;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * Advanced plugin management system
 */
public class PluginManager {
    private static volatile PluginManager instance;
    private final Map<String, Plugin> loadedPlugins = new ConcurrentHashMap<>();
    private final Map<String, PluginInfo> pluginInfo = new ConcurrentHashMap<>();
    private final File pluginsDirectory;
    private final NimbusWare nimbusWare;
    private final List<URLClassLoader> classLoaders = new ArrayList<>();
    
    private PluginManager(NimbusWare nimbusWare) {
        this.nimbusWare = nimbusWare;
        this.pluginsDirectory = new File("plugins");
        
        if (!pluginsDirectory.exists()) {
            pluginsDirectory.mkdirs();
        }
    }
    
    public static PluginManager getInstance(NimbusWare nimbusWare) {
        if (instance == null) {
            synchronized (PluginManager.class) {
                if (instance == null) {
                    instance = new PluginManager(nimbusWare);
                }
            }
        }
        return instance;
    }
    
    /**
     * Load all plugins from the plugins directory
     */
    public void loadAllPlugins() {
        Logger.info("Loading plugins from: " + pluginsDirectory.getAbsolutePath());
        
        File[] jarFiles = pluginsDirectory.listFiles((dir, name) -> name.endsWith(".jar"));
        if (jarFiles == null || jarFiles.length == 0) {
            Logger.info("No plugin JAR files found");
            return;
        }
        
        for (File jarFile : jarFiles) {
            try {
                loadPlugin(jarFile);
            } catch (Exception e) {
                Logger.error("Failed to load plugin: " + jarFile.getName(), e);
            }
        }
        
        Logger.info("Loaded " + loadedPlugins.size() + " plugins");
    }
    
    /**
     * Load a specific plugin
     * @param jarFile Plugin JAR file
     * @throws Exception if loading fails
     */
    public void loadPlugin(File jarFile) throws Exception {
        Logger.info("Loading plugin: " + jarFile.getName());
        
        // Create class loader for the plugin
        URLClassLoader classLoader = new URLClassLoader(
            new URL[]{jarFile.toURI().toURL()},
            getClass().getClassLoader()
        );
        classLoaders.add(classLoader);
        
        // Find main plugin class
        String mainClass = findMainClass(jarFile);
        if (mainClass == null) {
            throw new Exception("No main plugin class found in " + jarFile.getName());
        }
        
        // Load and instantiate the plugin
        Class<?> pluginClass = classLoader.loadClass(mainClass);
        if (!Plugin.class.isAssignableFrom(pluginClass)) {
            throw new Exception("Class " + mainClass + " does not implement Plugin interface");
        }
        
        Plugin plugin = (Plugin) pluginClass.getDeclaredConstructor().newInstance();
        
        // Check dependencies
        if (!checkDependencies(plugin)) {
            throw new Exception("Plugin " + plugin.getName() + " has unmet dependencies");
        }
        
        // Initialize the plugin
        if (!plugin.initialize(nimbusWare)) {
            throw new Exception("Plugin " + plugin.getName() + " failed to initialize");
        }
        
        // Store plugin info
        PluginInfo info = new PluginInfo(plugin, jarFile, classLoader);
        pluginInfo.put(plugin.getName(), info);
        loadedPlugins.put(plugin.getName(), plugin);
        
        Logger.info("Successfully loaded plugin: " + plugin.getName() + " v" + plugin.getVersion());
    }
    
    /**
     * Enable a plugin
     * @param pluginName Plugin name
     * @return true if enabled successfully
     */
    public boolean enablePlugin(String pluginName) {
        Plugin plugin = loadedPlugins.get(pluginName);
        if (plugin == null) {
            Logger.warn("Plugin not found: " + pluginName);
            return false;
        }
        
        if (plugin.isEnabled()) {
            Logger.info("Plugin already enabled: " + pluginName);
            return true;
        }
        
        try {
            plugin.onEnable();
            Logger.info("Enabled plugin: " + pluginName);
            return true;
        } catch (Exception e) {
            Logger.error("Failed to enable plugin: " + pluginName, e);
            return false;
        }
    }
    
    /**
     * Disable a plugin
     * @param pluginName Plugin name
     * @return true if disabled successfully
     */
    public boolean disablePlugin(String pluginName) {
        Plugin plugin = loadedPlugins.get(pluginName);
        if (plugin == null) {
            Logger.warn("Plugin not found: " + pluginName);
            return false;
        }
        
        if (!plugin.isEnabled()) {
            Logger.info("Plugin already disabled: " + pluginName);
            return true;
        }
        
        try {
            plugin.onDisable();
            Logger.info("Disabled plugin: " + pluginName);
            return true;
        } catch (Exception e) {
            Logger.error("Failed to disable plugin: " + pluginName, e);
            return false;
        }
    }
    
    /**
     * Unload a plugin
     * @param pluginName Plugin name
     * @return true if unloaded successfully
     */
    public boolean unloadPlugin(String pluginName) {
        Plugin plugin = loadedPlugins.get(pluginName);
        if (plugin == null) {
            Logger.warn("Plugin not found: " + pluginName);
            return false;
        }
        
        try {
            // Disable if enabled
            if (plugin.isEnabled()) {
                plugin.onDisable();
            }
            
            // Unload
            plugin.onUnload();
            
            // Remove from loaded plugins
            loadedPlugins.remove(pluginName);
            pluginInfo.remove(pluginName);
            
            Logger.info("Unloaded plugin: " + pluginName);
            return true;
        } catch (Exception e) {
            Logger.error("Failed to unload plugin: " + pluginName, e);
            return false;
        }
    }
    
    /**
     * Get a loaded plugin
     * @param pluginName Plugin name
     * @return Plugin instance or null
     */
    public Plugin getPlugin(String pluginName) {
        return loadedPlugins.get(pluginName);
    }
    
    /**
     * Get all loaded plugins
     * @return Map of plugin names to plugin instances
     */
    public Map<String, Plugin> getLoadedPlugins() {
        return new HashMap<>(loadedPlugins);
    }
    
    /**
     * Get plugin information
     * @param pluginName Plugin name
     * @return Plugin info or null
     */
    public PluginInfo getPluginInfo(String pluginName) {
        return pluginInfo.get(pluginName);
    }
    
    /**
     * Execute a plugin command
     * @param pluginName Plugin name
     * @param command Command name
     * @param args Command arguments
     * @return Command result
     */
    public String executeCommand(String pluginName, String command, String[] args) {
        Plugin plugin = loadedPlugins.get(pluginName);
        if (plugin == null) {
            return "Plugin not found: " + pluginName;
        }
        
        if (!plugin.isEnabled()) {
            return "Plugin is disabled: " + pluginName;
        }
        
        try {
            return plugin.handleCommand(command, args);
        } catch (Exception e) {
            Logger.error("Error executing plugin command: " + pluginName + ":" + command, e);
            return "Command execution failed: " + e.getMessage();
        }
    }
    
    /**
     * Get available commands for a plugin
     * @param pluginName Plugin name
     * @return Array of command names
     */
    public String[] getPluginCommands(String pluginName) {
        Plugin plugin = loadedPlugins.get(pluginName);
        if (plugin == null) {
            return new String[0];
        }
        
        return plugin.getCommands();
    }
    
    /**
     * Reload all plugins
     */
    public void reloadAllPlugins() {
        Logger.info("Reloading all plugins...");
        
        // Disable all plugins
        for (Plugin plugin : loadedPlugins.values()) {
            if (plugin.isEnabled()) {
                plugin.onDisable();
            }
        }
        
        // Clear loaded plugins
        loadedPlugins.clear();
        pluginInfo.clear();
        
        // Close class loaders
        for (URLClassLoader classLoader : classLoaders) {
            try {
                classLoader.close();
            } catch (IOException e) {
                Logger.warn("Failed to close class loader", e);
            }
        }
        classLoaders.clear();
        
        // Reload all plugins
        loadAllPlugins();
        
        // Re-enable plugins that were enabled
        for (Plugin plugin : loadedPlugins.values()) {
            enablePlugin(plugin.getName());
        }
        
        Logger.info("Plugin reload complete");
    }
    
    private String findMainClass(File jarFile) throws IOException {
        try (JarFile jar = new JarFile(jarFile)) {
            Enumeration<JarEntry> entries = jar.entries();
            
            while (entries.hasMoreElements()) {
                JarEntry entry = entries.nextElement();
                String name = entry.getName();
                
                if (name.endsWith(".class")) {
                    String className = name.replace('/', '.').substring(0, name.length() - 6);
                    
                    try {
                        Class<?> clazz = Class.forName(className);
                        if (Plugin.class.isAssignableFrom(clazz) && !clazz.isInterface()) {
                            return className;
                        }
                    } catch (ClassNotFoundException e) {
                        // Continue searching
                    }
                }
            }
        }
        
        return null;
    }
    
    private boolean checkDependencies(Plugin plugin) {
        String[] dependencies = plugin.getDependencies();
        
        for (String dependency : dependencies) {
            if (!loadedPlugins.containsKey(dependency)) {
                Logger.warn("Plugin " + plugin.getName() + " requires dependency: " + dependency);
                return false;
            }
        }
        
        return true;
    }
    
    /**
     * Shutdown the plugin manager
     */
    public void shutdown() {
        Logger.info("Shutting down plugin manager...");
        
        // Disable all plugins
        for (Plugin plugin : loadedPlugins.values()) {
            if (plugin.isEnabled()) {
                try {
                    plugin.onDisable();
                } catch (Exception e) {
                    Logger.error("Error disabling plugin: " + plugin.getName(), e);
                }
            }
        }
        
        // Unload all plugins
        for (Plugin plugin : loadedPlugins.values()) {
            try {
                plugin.onUnload();
            } catch (Exception e) {
                Logger.error("Error unloading plugin: " + plugin.getName(), e);
            }
        }
        
        // Close class loaders
        for (URLClassLoader classLoader : classLoaders) {
            try {
                classLoader.close();
            } catch (IOException e) {
                Logger.warn("Failed to close class loader", e);
            }
        }
        
        loadedPlugins.clear();
        pluginInfo.clear();
        classLoaders.clear();
        
        Logger.info("Plugin manager shutdown complete");
    }
    
    /**
     * Plugin information container
     */
    public static class PluginInfo {
        private final Plugin plugin;
        private final File jarFile;
        private final URLClassLoader classLoader;
        private final long loadTime;
        
        public PluginInfo(Plugin plugin, File jarFile, URLClassLoader classLoader) {
            this.plugin = plugin;
            this.jarFile = jarFile;
            this.classLoader = classLoader;
            this.loadTime = System.currentTimeMillis();
        }
        
        public Plugin getPlugin() { return plugin; }
        public File getJarFile() { return jarFile; }
        public URLClassLoader getClassLoader() { return classLoader; }
        public long getLoadTime() { return loadTime; }
    }
}