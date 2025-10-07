package com.example.nimbusware.web;

import com.example.nimbusware.NimbusWare;
import com.example.nimbusware.utils.Logger;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.*;
import java.net.*;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.HashMap;
import java.util.Map;

/**
 * Web server for remote management of NimbusWare
 */
public class WebServer {
    private static volatile WebServer instance;
    private final int port;
    private final NimbusWare nimbusWare;
    private ServerSocket serverSocket;
    private final ScheduledExecutorService scheduler = Executors.newScheduledThreadPool(4, r -> {
        Thread t = new Thread(r, "WebServer-Thread");
        t.setDaemon(true);
        return t;
    });
    private volatile boolean running = false;
    private final Gson gson = new GsonBuilder().setPrettyPrinting().create();
    
    public WebServer(int port, NimbusWare nimbusWare) {
        this.port = port;
        this.nimbusWare = nimbusWare;
    }
    
    public static WebServer getInstance(int port, NimbusWare nimbusWare) {
        if (instance == null) {
            synchronized (WebServer.class) {
                if (instance == null) {
                    instance = new WebServer(port, nimbusWare);
                }
            }
        }
        return instance;
    }
    
    /**
     * Start the web server
     */
    public void start() {
        if (running) {
            Logger.warn("Web server is already running");
            return;
        }
        
        try {
            serverSocket = new ServerSocket(port);
            running = true;
            
            Logger.info("Web server started on port " + port);
            Logger.info("Access the web interface at: http://localhost:" + port);
            
            // Start accepting connections
            scheduler.submit(this::acceptConnections);
            
        } catch (IOException e) {
            Logger.error("Failed to start web server on port " + port, e);
        }
    }
    
    /**
     * Stop the web server
     */
    public void stop() {
        if (!running) {
            return;
        }
        
        running = false;
        
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            Logger.warn("Error closing server socket", e);
        }
        
        scheduler.shutdown();
        try {
            if (!scheduler.awaitTermination(5, TimeUnit.SECONDS)) {
                scheduler.shutdownNow();
            }
        } catch (InterruptedException e) {
            scheduler.shutdownNow();
            Thread.currentThread().interrupt();
        }
        
        Logger.info("Web server stopped");
    }
    
    private void acceptConnections() {
        while (running) {
            try {
                Socket clientSocket = serverSocket.accept();
                scheduler.submit(() -> handleClient(clientSocket));
            } catch (IOException e) {
                if (running) {
                    Logger.error("Error accepting connection", e);
                }
            }
        }
    }
    
    private void handleClient(Socket clientSocket) {
        try (BufferedReader in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
             PrintWriter out = new PrintWriter(clientSocket.getOutputStream(), true)) {
            
            String requestLine = in.readLine();
            if (requestLine == null) return;
            
            String[] requestParts = requestLine.split(" ");
            if (requestParts.length < 2) return;
            
            String method = requestParts[0];
            String path = requestParts[1];
            
            Logger.debug("Web request: " + method + " " + path);
            
            // Handle different endpoints
            if (method.equals("GET")) {
                handleGetRequest(path, out);
            } else if (method.equals("POST")) {
                handlePostRequest(path, in, out);
            } else {
                sendErrorResponse(out, 405, "Method Not Allowed");
            }
            
        } catch (IOException e) {
            Logger.error("Error handling client connection", e);
        } finally {
            try {
                clientSocket.close();
            } catch (IOException e) {
                Logger.warn("Error closing client socket", e);
            }
        }
    }
    
    private void handleGetRequest(String path, PrintWriter out) {
        if (path.equals("/") || path.equals("/index.html")) {
            sendHtmlResponse(out, getMainPage());
        } else if (path.equals("/api/modules")) {
            sendJsonResponse(out, getModulesData());
        } else if (path.equals("/api/status")) {
            sendJsonResponse(out, getStatusData());
        } else if (path.equals("/api/metrics")) {
            sendJsonResponse(out, getMetricsData());
        } else if (path.equals("/api/health")) {
            sendJsonResponse(out, getHealthData());
        } else if (path.equals("/api/profiles")) {
            sendJsonResponse(out, getProfilesData());
        } else if (path.startsWith("/api/module/")) {
            String moduleName = path.substring(12);
            sendJsonResponse(out, getModuleData(moduleName));
        } else {
            sendErrorResponse(out, 404, "Not Found");
        }
    }
    
    private void handlePostRequest(String path, BufferedReader in, PrintWriter out) {
        try {
            StringBuilder body = new StringBuilder();
            String line;
            while ((line = in.readLine()) != null && !line.isEmpty()) {
                body.append(line);
            }
            
            if (path.equals("/api/module/toggle")) {
                Map<String, Object> request = gson.fromJson(body.toString(), Map.class);
                String moduleName = (String) request.get("module");
                boolean enabled = (Boolean) request.get("enabled");
                toggleModule(moduleName, enabled);
                sendJsonResponse(out, Map.of("success", true));
            } else if (path.equals("/api/profile/load")) {
                Map<String, Object> request = gson.fromJson(body.toString(), Map.class);
                String profileName = (String) request.get("profile");
                boolean success = loadProfile(profileName);
                sendJsonResponse(out, Map.of("success", success));
            } else if (path.equals("/api/profile/save")) {
                Map<String, Object> request = gson.fromJson(body.toString(), Map.class);
                String profileName = (String) request.get("profile");
                boolean success = saveProfile(profileName);
                sendJsonResponse(out, Map.of("success", success));
            } else {
                sendErrorResponse(out, 404, "Not Found");
            }
        } catch (Exception e) {
            Logger.error("Error handling POST request", e);
            sendErrorResponse(out, 500, "Internal Server Error");
        }
    }
    
    private void sendHtmlResponse(PrintWriter out, String html) {
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Content-Length: " + html.getBytes().length);
        out.println();
        out.println(html);
    }
    
    private void sendJsonResponse(PrintWriter out, Object data) {
        String json = gson.toJson(data);
        out.println("HTTP/1.1 200 OK");
        out.println("Content-Type: application/json; charset=UTF-8");
        out.println("Content-Length: " + json.getBytes().length);
        out.println();
        out.println(json);
    }
    
    private void sendErrorResponse(PrintWriter out, int code, String message) {
        String html = "<html><body><h1>" + code + " " + message + "</h1></body></html>";
        out.println("HTTP/1.1 " + code + " " + message);
        out.println("Content-Type: text/html; charset=UTF-8");
        out.println("Content-Length: " + html.getBytes().length);
        out.println();
        out.println(html);
    }
    
    private String getMainPage() {
        return """
            <!DOCTYPE html>
            <html>
            <head>
                <title>NimbusWare Remote Control</title>
                <meta charset="UTF-8">
                <meta name="viewport" content="width=device-width, initial-scale=1.0">
                <style>
                    body { font-family: Arial, sans-serif; margin: 0; padding: 20px; background: #1a1a1a; color: #fff; }
                    .container { max-width: 1200px; margin: 0 auto; }
                    .header { text-align: center; margin-bottom: 30px; }
                    .card { background: #2a2a2a; border-radius: 8px; padding: 20px; margin: 10px 0; }
                    .module { display: flex; justify-content: space-between; align-items: center; padding: 10px; border: 1px solid #444; margin: 5px 0; border-radius: 4px; }
                    .module.enabled { border-color: #4CAF50; background: #1a4a1a; }
                    .module.disabled { border-color: #f44336; background: #4a1a1a; }
                    .toggle-btn { padding: 8px 16px; border: none; border-radius: 4px; cursor: pointer; }
                    .toggle-btn.enabled { background: #f44336; color: white; }
                    .toggle-btn.disabled { background: #4CAF50; color: white; }
                    .status { display: grid; grid-template-columns: repeat(auto-fit, minmax(250px, 1fr)); gap: 20px; }
                    .metric { text-align: center; }
                    .metric-value { font-size: 2em; font-weight: bold; color: #4CAF50; }
                    .metric-label { color: #aaa; }
                    .profile-select { margin: 10px 0; }
                    .profile-select select { padding: 8px; border-radius: 4px; background: #333; color: white; border: 1px solid #555; }
                    .profile-select button { padding: 8px 16px; margin-left: 10px; background: #2196F3; color: white; border: none; border-radius: 4px; cursor: pointer; }
                </style>
            </head>
            <body>
                <div class="container">
                    <div class="header">
                        <h1>🎮 NimbusWare Remote Control</h1>
                        <p>Advanced Minecraft Cheat Client Management</p>
                    </div>
                    
                    <div class="card">
                        <h2>📊 System Status</h2>
                        <div class="status" id="status">
                            <div class="metric">
                                <div class="metric-value" id="modules-enabled">-</div>
                                <div class="metric-label">Modules Enabled</div>
                            </div>
                            <div class="metric">
                                <div class="metric-value" id="uptime">-</div>
                                <div class="metric-label">Uptime</div>
                            </div>
                            <div class="metric">
                                <div class="metric-value" id="memory-usage">-</div>
                                <div class="metric-label">Memory Usage</div>
                            </div>
                            <div class="metric">
                                <div class="metric-value" id="threads">-</div>
                                <div class="metric-label">Active Threads</div>
                            </div>
                        </div>
                    </div>
                    
                    <div class="card">
                        <h2>📁 Profile Management</h2>
                        <div class="profile-select">
                            <select id="profile-select">
                                <option value="">Select Profile...</option>
                            </select>
                            <button onclick="loadProfile()">Load Profile</button>
                            <button onclick="saveProfile()">Save Current</button>
                        </div>
                    </div>
                    
                    <div class="card">
                        <h2>🔧 Module Management</h2>
                        <div id="modules-list">
                            Loading modules...
                        </div>
                    </div>
                </div>
                
                <script>
                    function loadStatus() {
                        fetch('/api/status')
                            .then(response => response.json())
                            .then(data => {
                                document.getElementById('modules-enabled').textContent = data.modulesEnabled;
                                document.getElementById('uptime').textContent = data.uptime;
                                document.getElementById('memory-usage').textContent = data.memoryUsage + '%';
                                document.getElementById('threads').textContent = data.threads;
                            });
                    }
                    
                    function loadModules() {
                        fetch('/api/modules')
                            .then(response => response.json())
                            .then(data => {
                                const modulesList = document.getElementById('modules-list');
                                modulesList.innerHTML = '';
                                
                                data.modules.forEach(module => {
                                    const moduleDiv = document.createElement('div');
                                    moduleDiv.className = `module ${module.enabled ? 'enabled' : 'disabled'}`;
                                    
                                    moduleDiv.innerHTML = `
                                        <div>
                                            <strong>${module.name}</strong><br>
                                            <small>${module.description}</small>
                                        </div>
                                        <button class="toggle-btn ${module.enabled ? 'enabled' : 'disabled'}" 
                                                onclick="toggleModule('${module.name}', ${!module.enabled})">
                                            ${module.enabled ? 'Disable' : 'Enable'}
                                        </button>
                                    `;
                                    
                                    modulesList.appendChild(moduleDiv);
                                });
                            });
                    }
                    
                    function loadProfiles() {
                        fetch('/api/profiles')
                            .then(response => response.json())
                            .then(data => {
                                const select = document.getElementById('profile-select');
                                select.innerHTML = '<option value="">Select Profile...</option>';
                                
                                data.profiles.forEach(profile => {
                                    const option = document.createElement('option');
                                    option.value = profile.name;
                                    option.textContent = profile.name + ' - ' + profile.description;
                                    select.appendChild(option);
                                });
                            });
                    }
                    
                    function toggleModule(moduleName, enabled) {
                        fetch('/api/module/toggle', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ module: moduleName, enabled: enabled })
                        })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                loadModules();
                                loadStatus();
                            }
                        });
                    }
                    
                    function loadProfile() {
                        const profileName = document.getElementById('profile-select').value;
                        if (!profileName) return;
                        
                        fetch('/api/profile/load', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ profile: profileName })
                        })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                loadModules();
                                loadStatus();
                                alert('Profile loaded: ' + profileName);
                            } else {
                                alert('Failed to load profile');
                            }
                        });
                    }
                    
                    function saveProfile() {
                        const profileName = prompt('Enter profile name:');
                        if (!profileName) return;
                        
                        fetch('/api/profile/save', {
                            method: 'POST',
                            headers: { 'Content-Type': 'application/json' },
                            body: JSON.stringify({ profile: profileName })
                        })
                        .then(response => response.json())
                        .then(data => {
                            if (data.success) {
                                loadProfiles();
                                alert('Profile saved: ' + profileName);
                            } else {
                                alert('Failed to save profile');
                            }
                        });
                    }
                    
                    // Load data on page load
                    loadStatus();
                    loadModules();
                    loadProfiles();
                    
                    // Refresh data every 5 seconds
                    setInterval(() => {
                        loadStatus();
                        loadModules();
                    }, 5000);
                </script>
            </body>
            </html>
            """;
    }
    
    private Map<String, Object> getModulesData() {
        Map<String, Object> data = new HashMap<>();
        var modules = nimbusWare.getModuleManager().getModules();
        
        data.put("modules", modules.stream().map(module -> {
            Map<String, Object> moduleData = new HashMap<>();
            moduleData.put("name", module.getName());
            moduleData.put("description", module.getDescription());
            moduleData.put("enabled", module.isEnabled());
            moduleData.put("category", module.getCategory().getDisplayName());
            moduleData.put("uptime", module.getTotalUptime());
            return moduleData;
        }).toList());
        
        return data;
    }
    
    private Map<String, Object> getStatusData() {
        Map<String, Object> data = new HashMap<>();
        
        // Module count
        int enabledModules = nimbusWare.getModuleManager().getEnabledModules().size();
        int totalModules = nimbusWare.getModuleManager().getModules().size();
        data.put("modulesEnabled", enabledModules);
        data.put("totalModules", totalModules);
        
        // Uptime
        long uptime = System.currentTimeMillis() - nimbusWare.getStartTime();
        data.put("uptime", formatUptime(uptime));
        
        // Memory usage
        Runtime runtime = Runtime.getRuntime();
        long usedMemory = runtime.totalMemory() - runtime.freeMemory();
        long maxMemory = runtime.maxMemory();
        double memoryUsage = (double) usedMemory / maxMemory * 100;
        data.put("memoryUsage", Math.round(memoryUsage * 10) / 10.0);
        
        // Thread count
        data.put("threads", Thread.activeCount());
        
        return data;
    }
    
    private Map<String, Object> getMetricsData() {
        return nimbusWare.getMetricsCollector().getAllMetrics();
    }
    
    private Map<String, Object> getHealthData() {
        var healthStatus = nimbusWare.getHealthChecker().checkAllHealth();
        Map<String, Object> data = new HashMap<>();
        data.put("overall", healthStatus.isHealthy());
        data.put("message", healthStatus.getMessage());
        data.put("details", nimbusWare.getHealthChecker().getHealthResults());
        return data;
    }
    
    private Map<String, Object> getProfilesData() {
        var profiles = nimbusWare.getProfileManager().getAllProfiles();
        Map<String, Object> data = new HashMap<>();
        data.put("profiles", profiles.values().stream().map(profile -> {
            Map<String, Object> profileData = new HashMap<>();
            profileData.put("name", profile.getName());
            profileData.put("description", profile.getDescription());
            profileData.put("moduleCount", profile.getModuleStates().size());
            return profileData;
        }).toList());
        return data;
    }
    
    private Map<String, Object> getModuleData(String moduleName) {
        var module = nimbusWare.getModuleManager().getModule(moduleName);
        if (module == null) {
            return Map.of("error", "Module not found");
        }
        
        Map<String, Object> data = new HashMap<>();
        data.put("name", module.getName());
        data.put("description", module.getDescription());
        data.put("enabled", module.isEnabled());
        data.put("category", module.getCategory().getDisplayName());
        data.put("settings", module.getSettings().getAll());
        data.put("statistics", module.getStatistics());
        return data;
    }
    
    private void toggleModule(String moduleName, boolean enabled) {
        var module = nimbusWare.getModuleManager().getModule(moduleName);
        if (module != null) {
            if (enabled) {
                module.enable();
            } else {
                module.disable();
            }
        }
    }
    
    private boolean loadProfile(String profileName) {
        return nimbusWare.getProfileManager().loadProfile(profileName);
    }
    
    private boolean saveProfile(String profileName) {
        return nimbusWare.getProfileManager().saveToProfile(profileName);
    }
    
    private String formatUptime(long uptimeMs) {
        long seconds = uptimeMs / 1000;
        long minutes = seconds / 60;
        long hours = minutes / 60;
        
        if (hours > 0) {
            return String.format("%dh %dm", hours, minutes % 60);
        } else if (minutes > 0) {
            return String.format("%dm %ds", minutes, seconds % 60);
        } else {
            return String.format("%ds", seconds);
        }
    }
    
    public boolean isRunning() {
        return running;
    }
}