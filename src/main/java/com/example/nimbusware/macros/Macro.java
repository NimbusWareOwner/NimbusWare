package com.example.nimbusware.macros;

import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Macro system for automating complex sequences of actions
 */
public class Macro {
    private final String name;
    private final String description;
    private final List<MacroAction> actions = new ArrayList<>();
    private final ScheduledExecutorService executor = Executors.newSingleThreadScheduledExecutor(r -> {
        Thread t = new Thread(r, "Macro-" + name + "-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private boolean running = false;
    private boolean loop = false;
    private int currentAction = 0;
    private long startTime = 0;
    private int executionCount = 0;
    
    public Macro(String name, String description) {
        this.name = name;
        this.description = description;
    }
    
    /**
     * Add an action to the macro
     * @param action Macro action
     */
    public void addAction(MacroAction action) {
        actions.add(action);
    }
    
    /**
     * Add a delay action
     * @param delayMs Delay in milliseconds
     */
    public void addDelay(long delayMs) {
        addAction(new DelayAction(delayMs));
    }
    
    /**
     * Add a key press action
     * @param key Key code
     * @param duration Duration in milliseconds
     */
    public void addKeyPress(int key, long duration) {
        addAction(new KeyPressAction(key, duration));
    }
    
    /**
     * Add a mouse click action
     * @param button Mouse button (1=left, 2=right, 3=middle)
     * @param duration Duration in milliseconds
     */
    public void addMouseClick(int button, long duration) {
        addAction(new MouseClickAction(button, duration));
    }
    
    /**
     * Add a module toggle action
     * @param moduleName Module name
     */
    public void addModuleToggle(String moduleName) {
        addAction(new ModuleToggleAction(moduleName));
    }
    
    /**
     * Add a command execution action
     * @param command Command to execute
     */
    public void addCommand(String command) {
        addAction(new CommandAction(command));
    }
    
    /**
     * Start the macro
     * @param loop Whether to loop the macro
     */
    public void start(boolean loop) {
        if (running) {
            Logger.warn("Macro '" + name + "' is already running");
            return;
        }
        
        if (actions.isEmpty()) {
            Logger.warn("Macro '" + name + "' has no actions");
            return;
        }
        
        this.loop = loop;
        this.running = true;
        this.currentAction = 0;
        this.startTime = System.currentTimeMillis();
        this.executionCount = 0;
        
        Logger.info("Started macro: " + name + (loop ? " (looping)" : ""));
        executeNextAction();
    }
    
    /**
     * Stop the macro
     */
    public void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        executor.shutdown();
        
        long duration = System.currentTimeMillis() - startTime;
        Logger.info("Stopped macro: " + name + " (executed " + executionCount + " times, duration: " + duration + "ms)");
    }
    
    /**
     * Pause the macro
     */
    public void pause() {
        if (running) {
            running = false;
            Logger.info("Paused macro: " + name);
        }
    }
    
    /**
     * Resume the macro
     */
    public void resume() {
        if (!running && currentAction < actions.size()) {
            running = true;
            Logger.info("Resumed macro: " + name);
            executeNextAction();
        }
    }
    
    private void executeNextAction() {
        if (!running || currentAction >= actions.size()) {
            if (loop && running) {
                // Restart macro
                currentAction = 0;
                executionCount++;
                executeNextAction();
            } else {
                // Macro finished
                running = false;
                long duration = System.currentTimeMillis() - startTime;
                Logger.info("Macro '" + name + "' finished (duration: " + duration + "ms)");
            }
            return;
        }
        
        MacroAction action = actions.get(currentAction);
        currentAction++;
        
        try {
            action.execute();
            
            // Schedule next action
            long delay = action.getDelay();
            if (delay > 0) {
                executor.schedule(this::executeNextAction, delay, TimeUnit.MILLISECONDS);
            } else {
                executeNextAction();
            }
        } catch (Exception e) {
            Logger.error("Error executing macro action: " + action.getClass().getSimpleName(), e);
            executeNextAction(); // Continue with next action
        }
    }
    
    /**
     * Get macro statistics
     * @return Macro statistics
     */
    public MacroStatistics getStatistics() {
        return new MacroStatistics(
            name,
            running,
            currentAction,
            actions.size(),
            executionCount,
            System.currentTimeMillis() - startTime
        );
    }
    
    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public boolean isRunning() { return running; }
    public boolean isLoop() { return loop; }
    public int getCurrentAction() { return currentAction; }
    public int getTotalActions() { return actions.size(); }
    public int getExecutionCount() { return executionCount; }
    public List<MacroAction> getActions() { return new ArrayList<>(actions); }
    
    /**
     * Macro action interface
     */
    public interface MacroAction {
        void execute() throws Exception;
        long getDelay();
    }
    
    /**
     * Delay action
     */
    public static class DelayAction implements MacroAction {
        private final long delay;
        
        public DelayAction(long delay) {
            this.delay = delay;
        }
        
        @Override
        public void execute() throws Exception {
            // Delay is handled by the executor
        }
        
        @Override
        public long getDelay() {
            return delay;
        }
    }
    
    /**
     * Key press action
     */
    public static class KeyPressAction implements MacroAction {
        private final int key;
        private final long duration;
        
        public KeyPressAction(int key, long duration) {
            this.key = key;
            this.duration = duration;
        }
        
        @Override
        public void execute() throws Exception {
            // Simulate key press
            System.out.println("Macro: Pressing key " + key + " for " + duration + "ms");
        }
        
        @Override
        public long getDelay() {
            return duration;
        }
    }
    
    /**
     * Mouse click action
     */
    public static class MouseClickAction implements MacroAction {
        private final int button;
        private final long duration;
        
        public MouseClickAction(int button, long duration) {
            this.button = button;
            this.duration = duration;
        }
        
        @Override
        public void execute() throws Exception {
            // Simulate mouse click
            System.out.println("Macro: Clicking mouse button " + button + " for " + duration + "ms");
        }
        
        @Override
        public long getDelay() {
            return duration;
        }
    }
    
    /**
     * Module toggle action
     */
    public static class ModuleToggleAction implements MacroAction {
        private final String moduleName;
        
        public ModuleToggleAction(String moduleName) {
            this.moduleName = moduleName;
        }
        
        @Override
        public void execute() throws Exception {
            // Toggle module
            if (com.example.nimbusware.NimbusWare.INSTANCE != null) {
                var module = com.example.nimbusware.NimbusWare.INSTANCE.getModuleManager().getModule(moduleName);
                if (module != null) {
                    module.toggle();
                    System.out.println("Macro: Toggled module " + moduleName);
                }
            }
        }
        
        @Override
        public long getDelay() {
            return 100; // 100ms delay after module toggle
        }
    }
    
    /**
     * Command execution action
     */
    public static class CommandAction implements MacroAction {
        private final String command;
        
        public CommandAction(String command) {
            this.command = command;
        }
        
        @Override
        public void execute() throws Exception {
            // Execute command
            System.out.println("Macro: Executing command: " + command);
        }
        
        @Override
        public long getDelay() {
            return 50; // 50ms delay after command
        }
    }
    
    /**
     * Macro statistics
     */
    public static class MacroStatistics {
        private final String name;
        private final boolean running;
        private final int currentAction;
        private final int totalActions;
        private final int executionCount;
        private final long duration;
        
        public MacroStatistics(String name, boolean running, int currentAction, int totalActions, int executionCount, long duration) {
            this.name = name;
            this.running = running;
            this.currentAction = currentAction;
            this.totalActions = totalActions;
            this.executionCount = executionCount;
            this.duration = duration;
        }
        
        public String getName() { return name; }
        public boolean isRunning() { return running; }
        public int getCurrentAction() { return currentAction; }
        public int getTotalActions() { return totalActions; }
        public int getExecutionCount() { return executionCount; }
        public long getDuration() { return duration; }
        
        @Override
        public String toString() {
            return String.format("Macro[%s]: %s, Action %d/%d, Executions: %d, Duration: %dms",
                name, running ? "Running" : "Stopped", currentAction, totalActions, executionCount, duration);
        }
    }
}