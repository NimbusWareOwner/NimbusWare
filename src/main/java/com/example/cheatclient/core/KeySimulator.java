package com.example.cheatclient.core;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.utils.Logger;

import java.util.Scanner;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class KeySimulator {
    private CheatClient client;
    private KeyManager keyManager;
    private Scanner scanner;
    private ScheduledExecutorService executor;
    private boolean running = false;
    
    public KeySimulator(CheatClient client) {
        this.client = client;
        this.keyManager = client.getKeyManager();
        this.scanner = new Scanner(System.in);
        this.executor = Executors.newScheduledThreadPool(1);
    }
    
    public void start() {
        if (running) return;
        
        running = true;
        Logger.info("Key simulator started. Press 'h' for help, 'q' to quit");
        
        // Start key simulation thread
        executor.scheduleAtFixedRate(this::simulateKeyInput, 0, 100, TimeUnit.MILLISECONDS);
        
        // Start console input thread
        executor.submit(this::handleConsoleInput);
    }
    
    public void stop() {
        if (!running) return;
        
        running = false;
        executor.shutdown();
        try {
            if (!executor.awaitTermination(1, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        Logger.info("Key simulator stopped");
    }
    
    private void simulateKeyInput() {
        // Simulate random key presses for demonstration
        // In a real implementation, this would capture actual key events
        
        // Simulate Right Shift press every 5 seconds for demo
        if (System.currentTimeMillis() % 5000 < 100) {
            keyManager.onKeyPress(KeyManager.KEY_RIGHT_SHIFT);
        }
    }
    
    private void handleConsoleInput() {
        while (running) {
            try {
                if (scanner.hasNextLine()) {
                    String input = scanner.nextLine().trim().toLowerCase();
                    handleInput(input);
                }
            } catch (Exception e) {
                Logger.error("Input error: " + e.getMessage());
                break;
            }
        }
    }
    
    private void handleInput(String input) {
        switch (input) {
            case "h":
            case "help":
                showHelp();
                break;
            case "q":
            case "quit":
                stop();
                System.exit(0);
                break;
            case "menu":
                keyManager.onKeyPress(KeyManager.KEY_RIGHT_SHIFT);
                break;
            case "esc":
                keyManager.onKeyPress(KeyManager.KEY_ESCAPE);
                break;
            case "1":
                keyManager.onKeyPress(KeyManager.KEY_1);
                break;
            case "2":
                keyManager.onKeyPress(KeyManager.KEY_2);
                break;
            case "3":
                keyManager.onKeyPress(KeyManager.KEY_3);
                break;
            case "4":
                keyManager.onKeyPress(KeyManager.KEY_4);
                break;
            case "5":
                keyManager.onKeyPress(KeyManager.KEY_5);
                break;
            case "6":
                keyManager.onKeyPress(KeyManager.KEY_6);
                break;
            case "7":
                keyManager.onKeyPress(KeyManager.KEY_7);
                break;
            case "8":
                keyManager.onKeyPress(KeyManager.KEY_8);
                break;
            case "9":
                keyManager.onKeyPress(KeyManager.KEY_9);
                break;
            case "0":
                keyManager.onKeyPress(KeyManager.KEY_0);
                break;
            default:
                Logger.warn("Unknown command: " + input + ". Type 'h' for help");
                break;
        }
    }
    
    private void showHelp() {
        Logger.info("=== NimbusWare Key Simulator ===");
        Logger.info("Commands:");
        Logger.info("  menu  - Open main menu (Right Shift)");
        Logger.info("  esc   - Close menus");
        Logger.info("  1-0   - Quick module toggle");
        Logger.info("  h     - Show this help");
        Logger.info("  q     - Quit");
        Logger.info("================================");
    }
}