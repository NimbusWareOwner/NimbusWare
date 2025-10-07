package com.example.nimbusware.testing;

import com.example.nimbusware.core.Module;
import com.example.nimbusware.core.ModuleManager;
import com.example.nimbusware.utils.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Automated testing system for modules
 */
public class AutoTester {
    private static volatile AutoTester instance;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(2, r -> {
        Thread t = new Thread(r, "AutoTester-Thread");
        t.setDaemon(true);
        return t;
    });
    
    private final List<TestResult> testResults = new ArrayList<>();
    private volatile boolean testingEnabled = false;
    
    private AutoTester() {
        startTesting();
    }
    
    public static AutoTester getInstance() {
        if (instance == null) {
            synchronized (AutoTester.class) {
                if (instance == null) {
                    instance = new AutoTester();
                }
            }
        }
        return instance;
    }
    
    /**
     * Run all module tests
     * @param moduleManager Module manager instance
     */
    public void runAllTests(ModuleManager moduleManager) {
        if (!testingEnabled) {
            Logger.info("Testing is disabled");
            return;
        }
        
        Logger.info("Starting automated testing...");
        
        for (Module module : moduleManager.getModules()) {
            runModuleTest(module);
        }
        
        generateTestReport();
    }
    
    /**
     * Run test for a specific module
     * @param module Module to test
     */
    public void runModuleTest(Module module) {
        TestResult result = new TestResult(module.getName());
        
        try {
            // Test module initialization
            testModuleInitialization(module, result);
            
            // Test module enable/disable
            testModuleToggle(module, result);
            
            // Test module settings
            testModuleSettings(module, result);
            
            // Test module statistics
            testModuleStatistics(module, result);
            
            result.setSuccess(true);
            Logger.info("Module test passed: " + module.getName());
            
        } catch (Exception e) {
            result.setSuccess(false);
            result.setErrorMessage(e.getMessage());
            Logger.error("Module test failed: " + module.getName(), e);
        }
        
        testResults.add(result);
    }
    
    private void testModuleInitialization(Module module, TestResult result) {
        // Test that module can be created
        if (module == null) {
            throw new RuntimeException("Module is null");
        }
        
        // Test basic properties
        if (module.getName() == null || module.getName().isEmpty()) {
            throw new RuntimeException("Module name is null or empty");
        }
        
        if (module.getDescription() == null || module.getDescription().isEmpty()) {
            throw new RuntimeException("Module description is null or empty");
        }
        
        result.addTest("initialization", true, "Module initialized successfully");
    }
    
    private void testModuleToggle(Module module, TestResult result) {
        boolean initialState = module.isEnabled();
        
        try {
            // Test enable
            if (!initialState) {
                module.enable();
                if (!module.isEnabled()) {
                    throw new RuntimeException("Module did not enable");
                }
            }
            
            // Test disable
            module.disable();
            if (module.isEnabled()) {
                throw new RuntimeException("Module did not disable");
            }
            
            // Restore original state
            if (initialState) {
                module.enable();
            }
            
            result.addTest("toggle", true, "Module toggle works correctly");
            
        } catch (Exception e) {
            result.addTest("toggle", false, "Module toggle failed: " + e.getMessage());
            throw e;
        }
    }
    
    private void testModuleSettings(Module module, TestResult result) {
        try {
            // Test settings access
            com.example.nimbusware.core.ModuleSettings settings = module.getSettings();
            if (settings == null) {
                throw new RuntimeException("Module settings are null");
            }
            
            // Test settings operations
            settings.set("test_key", "test_value");
            String value = settings.getString("test_key", "default");
            if (!"test_value".equals(value)) {
                throw new RuntimeException("Settings set/get failed");
            }
            
            result.addTest("settings", true, "Module settings work correctly");
            
        } catch (Exception e) {
            result.addTest("settings", false, "Module settings failed: " + e.getMessage());
            throw e;
        }
    }
    
    private void testModuleStatistics(Module module, TestResult result) {
        try {
            // Test statistics access
            java.util.Map<String, Object> stats = module.getStatistics();
            if (stats == null) {
                throw new RuntimeException("Module statistics are null");
            }
            
            // Test that statistics contain expected keys
            if (!stats.containsKey("enabled")) {
                throw new RuntimeException("Statistics missing 'enabled' key");
            }
            
            result.addTest("statistics", true, "Module statistics work correctly");
            
        } catch (Exception e) {
            result.addTest("statistics", false, "Module statistics failed: " + e.getMessage());
            throw e;
        }
    }
    
    private void generateTestReport() {
        int totalTests = testResults.size();
        int passedTests = (int) testResults.stream().filter(TestResult::isSuccess).count();
        int failedTests = totalTests - passedTests;
        
        Logger.info("=== TEST REPORT ===");
        Logger.info("Total Tests: " + totalTests);
        Logger.info("Passed: " + passedTests);
        Logger.info("Failed: " + failedTests);
        Logger.info("Success Rate: " + String.format("%.2f%%", (double) passedTests / totalTests * 100));
        
        if (failedTests > 0) {
            Logger.info("Failed Tests:");
            testResults.stream()
                .filter(result -> !result.isSuccess())
                .forEach(result -> Logger.info("  - " + result.getModuleName() + ": " + result.getErrorMessage()));
        }
        
        Logger.info("==================");
    }
    
    private void startTesting() {
        // Run tests every 10 minutes
        scheduler.scheduleAtFixedRate(() -> {
            try {
                if (testingEnabled) {
                    // Run basic system tests
                    runSystemTests();
                }
            } catch (Exception e) {
                Logger.error("Error in automated testing", e);
            }
        }, 10, 10, TimeUnit.MINUTES);
        
        Logger.info("Automated testing started");
    }
    
    private void runSystemTests() {
        // Test basic system functionality
        Logger.debug("Running system tests...");
        
        // Test memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        double memoryUsage = (double) usedMemory / maxMemory;
        
        if (memoryUsage > 0.9) {
            Logger.warn("High memory usage detected: " + String.format("%.2f%%", memoryUsage * 100));
        }
        
        // Test thread count
        int threadCount = Thread.activeCount();
        if (threadCount > 100) {
            Logger.warn("High thread count detected: " + threadCount);
        }
    }
    
    /**
     * Set testing enabled
     * @param enabled Whether testing is enabled
     */
    public void setTestingEnabled(boolean enabled) {
        this.testingEnabled = enabled;
        Logger.info("Automated testing " + (enabled ? "enabled" : "disabled"));
    }
    
    /**
     * Get test results
     * @return List of test results
     */
    public List<TestResult> getTestResults() {
        return new ArrayList<>(testResults);
    }
    
    /**
     * Clear test results
     */
    public void clearTestResults() {
        testResults.clear();
        Logger.info("Test results cleared");
    }
    
    /**
     * Shutdown the auto tester
     */
    public void shutdown() {
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
    
    /**
     * Test result container
     */
    public static class TestResult {
        private final String moduleName;
        private final List<Test> tests = new ArrayList<>();
        private boolean success = false;
        private String errorMessage = "";
        
        public TestResult(String moduleName) {
            this.moduleName = moduleName;
        }
        
        public void addTest(String testName, boolean passed, String message) {
            tests.add(new Test(testName, passed, message));
        }
        
        public String getModuleName() { return moduleName; }
        public List<Test> getTests() { return tests; }
        public boolean isSuccess() { return success; }
        public void setSuccess(boolean success) { this.success = success; }
        public String getErrorMessage() { return errorMessage; }
        public void setErrorMessage(String errorMessage) { this.errorMessage = errorMessage; }
    }
    
    /**
     * Individual test container
     */
    public static class Test {
        private final String name;
        private final boolean passed;
        private final String message;
        
        public Test(String name, boolean passed, String message) {
            this.name = name;
            this.passed = passed;
            this.message = message;
        }
        
        public String getName() { return name; }
        public boolean isPassed() { return passed; }
        public String getMessage() { return message; }
    }
}