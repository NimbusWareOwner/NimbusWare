package com.example.cheatclient.gui;

import com.example.cheatclient.CheatClient;
import com.example.cheatclient.core.Module;
import com.example.cheatclient.core.ModuleManager;
import com.example.cheatclient.utils.Logger;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class AdvancedMainMenu {
    private CheatClient client;
    private boolean isOpen = false;
    private Scanner scanner;
    private String currentTheme = "Matrix";
    private int currentPage = 0;
    private int selectedOption = 0;
    private boolean showAnimations = true;
    private boolean showParticles = true;
    private boolean showEffects = true;
    private boolean showRainbow = false;
    private boolean showMatrix = false;
    private boolean showGlow = true;
    private boolean showOutline = true;
    private boolean showShadow = true;
    private boolean showGradient = true;
    private boolean showPulse = false;
    private boolean showBounce = false;
    private boolean showSlide = true;
    private boolean showFade = true;
    
    // Menu pages
    private List<MenuPage> pages = new ArrayList<>();
    private Map<String, MenuEffect> effects = new ConcurrentHashMap<>();
    private List<MenuParticle> particles = new ArrayList<>();
    private Random random = new Random();
    
    // Animation data
    private long lastUpdate = 0;
    private int animationFrame = 0;
    private boolean animating = false;
    private String animationText = "";
    private int animationIndex = 0;
    
    public AdvancedMainMenu(CheatClient client) {
        this.client = client;
        this.scanner = new Scanner(System.in);
        initializeMenu();
        initializeEffects();
    }
    
    private void initializeMenu() {
        // Main page
        MenuPage mainPage = new MenuPage("Main Menu", Arrays.asList(
            new MenuOption("🎮 Module Manager", "Manage all cheat modules", "modules"),
            new MenuOption("🛒 AutoBuy System", "Advanced AutoBuy features", "autobuy"),
            new MenuOption("👥 Account Manager", "Manage multiple accounts", "accounts"),
            new MenuOption("💰 Money Making", "Automated money making", "money"),
            new MenuOption("🎨 Visual Settings", "Customize appearance", "visual"),
            new MenuOption("🖥️ HUD Settings", "Configure HUD display", "hud"),
            new MenuOption("🎭 Theme Settings", "Change visual themes", "themes"),
            new MenuOption("⚙️ Advanced Settings", "Advanced configuration", "advanced"),
            new MenuOption("📊 Statistics", "View performance stats", "stats"),
            new MenuOption("❓ Help & Support", "Get help and support", "help"),
            new MenuOption("🚪 Exit", "Exit the application", "exit")
        ));
        pages.add(mainPage);
        
        // Module Manager page
        MenuPage modulePage = new MenuPage("Module Manager", Arrays.asList(
            new MenuOption("🔧 Movement Modules", "Configure movement cheats", "movement"),
            new MenuOption("👁️ Render Modules", "Configure visual cheats", "render"),
            new MenuOption("⚔️ Combat Modules", "Configure combat cheats", "combat"),
            new MenuOption("👤 Player Modules", "Configure player cheats", "player"),
            new MenuOption("🌍 World Modules", "Configure world cheats", "world"),
            new MenuOption("🔧 Misc Modules", "Configure miscellaneous cheats", "misc"),
            new MenuOption("🔙 Back to Main", "Return to main menu", "back")
        ));
        pages.add(modulePage);
        
        // Visual Settings page
        MenuPage visualPage = new MenuPage("Visual Settings", Arrays.asList(
            new MenuOption("🎨 Color Settings", "Configure color scheme", "colors"),
            new MenuOption("✨ Effect Settings", "Configure visual effects", "effects"),
            new MenuOption("🎬 Animation Settings", "Configure animations", "animations"),
            new MenuOption("🔤 Font Settings", "Configure text appearance", "fonts"),
            new MenuOption("📐 Layout Settings", "Configure interface layout", "layout"),
            new MenuOption("🔙 Back to Main", "Return to main menu", "back")
        ));
        pages.add(visualPage);
        
        // Theme Settings page
        MenuPage themePage = new MenuPage("Theme Settings", Arrays.asList(
            new MenuOption("🌙 Dark Theme", "Apply dark theme", "dark"),
            new MenuOption("☀️ Light Theme", "Apply light theme", "light"),
            new MenuOption("🌈 Rainbow Theme", "Apply rainbow theme", "rainbow"),
            new MenuOption("🔮 Matrix Theme", "Apply matrix theme", "matrix"),
            new MenuOption("🎉 Funtime Theme", "Apply funtime theme", "funtime"),
            new MenuOption("🎨 Custom Theme", "Create custom theme", "custom"),
            new MenuOption("🔙 Back to Main", "Return to main menu", "back")
        ));
        pages.add(themePage);
        
        // HUD Settings page
        MenuPage hudPage = new MenuPage("HUD Settings", Arrays.asList(
            new MenuOption("📊 Display Elements", "Configure HUD elements", "elements"),
            new MenuOption("📍 Positioning", "Configure element positions", "positioning"),
            new MenuOption("🎨 Styling", "Configure HUD appearance", "styling"),
            new MenuOption("✨ Effects", "Configure HUD effects", "hud_effects"),
            new MenuOption("🔙 Back to Main", "Return to main menu", "back")
        ));
        pages.add(hudPage);
    }
    
    private void initializeEffects() {
        effects.put("matrix", new MatrixEffect());
        effects.put("rainbow", new RainbowEffect());
        effects.put("glow", new GlowEffect());
        effects.put("outline", new OutlineEffect());
        effects.put("shadow", new ShadowEffect());
        effects.put("gradient", new GradientEffect());
        effects.put("pulse", new PulseEffect());
        effects.put("bounce", new BounceEffect());
        effects.put("slide", new SlideEffect());
        effects.put("fade", new FadeEffect());
    }
    
    public void open() {
        isOpen = true;
        currentPage = 0;
        selectedOption = 0;
        
        // Start animation
        startAnimation();
        
        // Add welcome notification
        if (client.getHUDManager() != null) {
            client.getHUDManager().addNotification("§aWelcome to CheatClient!", 3000);
        }
        
        showMenu();
    }
    
    public void close() {
        isOpen = false;
        animating = false;
        particles.clear();
    }
    
    private void showMenu() {
        while (isOpen) {
            clearScreen();
            printHeader();
            printMenu();
            printFooter();
            handleInput();
        }
    }
    
    private void printHeader() {
        String header = getAnimatedHeader();
        System.out.println(header);
        System.out.println();
    }
    
    private void printMenu() {
        MenuPage page = pages.get(currentPage);
        
        // Print page title
        String title = applyEffects("§l§b" + page.getTitle() + "§r");
        System.out.println(centerText(title));
        System.out.println();
        
        // Print options
        for (int i = 0; i < page.getOptions().size(); i++) {
            MenuOption option = page.getOptions().get(i);
            String optionText = option.getIcon() + " " + option.getName();
            
            if (i == selectedOption) {
                optionText = "§l§a> " + optionText + " <§r";
            } else {
                optionText = "§7  " + optionText;
            }
            
            optionText = applyEffects(optionText);
            System.out.println(optionText);
            
            // Print description if selected
            if (i == selectedOption && !option.getDescription().isEmpty()) {
                String desc = "§8    " + option.getDescription();
                desc = applyEffects(desc);
                System.out.println(desc);
            }
        }
        
        System.out.println();
    }
    
    private void printFooter() {
        String footer = "§7Use ↑↓ to navigate, Enter to select, ESC to go back";
        footer = applyEffects(footer);
        System.out.println(centerText(footer));
        
        // Print particles
        if (showParticles) {
            printParticles();
        }
    }
    
    private String getAnimatedHeader() {
        if (!showAnimations) {
            return "§l§bCheatClient §7v1.0.0";
        }
        
        String[] headers = {
            "§l§bCheatClient §7v1.0.0",
            "§l§aCheatClient §7v1.0.0",
            "§l§cCheatClient §7v1.0.0",
            "§l§eCheatClient §7v1.0.0",
            "§l§dCheatClient §7v1.0.0",
            "§l§bCheatClient §7v1.0.0"
        };
        
        if (showRainbow) {
            return headers[animationFrame % headers.length];
        } else if (showMatrix) {
            return "§a" + generateMatrixString() + " §l§bCheatClient §7v1.0.0";
        } else {
            return headers[0];
        }
    }
    
    private String applyEffects(String text) {
        if (!showEffects) return text;
        
        // Apply glow effect
        if (showGlow) {
            text = "§k" + text + "§r";
        }
        
        // Apply outline effect
        if (showOutline) {
            text = "§l" + text;
        }
        
        // Apply shadow effect
        if (showShadow) {
            text = "§0" + text + "§r";
        }
        
        // Apply gradient effect
        if (showGradient) {
            text = applyGradient(text);
        }
        
        // Apply pulse effect
        if (showPulse && random.nextDouble() < 0.1) {
            text = "§k" + text + "§r";
        }
        
        // Apply bounce effect
        if (showBounce && random.nextDouble() < 0.1) {
            text = "§l" + text + "§r";
        }
        
        return text;
    }
    
    private String applyGradient(String text) {
        String[] colors = {"§c", "§6", "§e", "§a", "§b", "§d"};
        StringBuilder sb = new StringBuilder();
        
        for (int i = 0; i < text.length(); i++) {
            char c = text.charAt(i);
            if (c == '§') {
                sb.append(c);
                if (i + 1 < text.length()) {
                    sb.append(text.charAt(i + 1));
                    i++;
                }
            } else {
                sb.append(colors[i % colors.length]).append(c);
            }
        }
        
        return sb.toString();
    }
    
    private String generateMatrixString() {
        String chars = "01";
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < 20; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }
    
    private void printParticles() {
        if (particles.isEmpty()) return;
        
        StringBuilder sb = new StringBuilder();
        for (MenuParticle particle : particles) {
            sb.append(particle.toString()).append(" ");
        }
        System.out.println(sb.toString());
    }
    
    private String centerText(String text) {
        int width = 80; // Terminal width
        int textLength = text.length();
        int padding = (width - textLength) / 2;
        
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < padding; i++) {
            sb.append(" ");
        }
        sb.append(text);
        
        return sb.toString();
    }
    
    private void handleInput() {
        try {
            String input = scanner.nextLine().trim().toLowerCase();
            
            switch (input) {
                case "w":
                case "up":
                case "arrow_up":
                    moveUp();
                    break;
                case "s":
                case "down":
                case "arrow_down":
                    moveDown();
                    break;
                case "enter":
                case "":
                    selectOption();
                    break;
                case "esc":
                case "back":
                    goBack();
                    break;
                case "1":
                case "2":
                case "3":
                case "4":
                case "5":
                case "6":
                case "7":
                case "8":
                case "9":
                case "0":
                    int option = Integer.parseInt(input) - 1;
                    if (option >= 0 && option < pages.get(currentPage).getOptions().size()) {
                        selectedOption = option;
                        selectOption();
                    }
                    break;
                case "help":
                    showHelp();
                    break;
                case "themes":
                    showThemes();
                    break;
                case "effects":
                    showEffects();
                    break;
                case "animations":
                    toggleAnimations();
                    break;
                case "particles":
                    toggleParticles();
                    break;
                case "rainbow":
                    toggleRainbow();
                    break;
                case "matrix":
                    toggleMatrix();
                    break;
                case "glow":
                    toggleGlow();
                    break;
                case "outline":
                    toggleOutline();
                    break;
                case "shadow":
                    toggleShadow();
                    break;
                case "gradient":
                    toggleGradient();
                    break;
                case "pulse":
                    togglePulse();
                    break;
                case "bounce":
                    toggleBounce();
                    break;
                case "slide":
                    toggleSlide();
                    break;
                case "fade":
                    toggleFade();
                    break;
                case "reset":
                    resetEffects();
                    break;
                case "quit":
                case "exit":
                    close();
                    break;
                default:
                    Logger.warn("Unknown command: " + input);
                    break;
            }
        } catch (Exception e) {
            Logger.error("Input error: " + e.getMessage());
        }
    }
    
    private void moveUp() {
        if (selectedOption > 0) {
            selectedOption--;
        } else {
            selectedOption = pages.get(currentPage).getOptions().size() - 1;
        }
    }
    
    private void moveDown() {
        if (selectedOption < pages.get(currentPage).getOptions().size() - 1) {
            selectedOption++;
        } else {
            selectedOption = 0;
        }
    }
    
    private void selectOption() {
        MenuPage page = pages.get(currentPage);
        MenuOption option = page.getOptions().get(selectedOption);
        
        // Add selection effect
        if (showEffects) {
            addParticle("§a✓", "§a");
        }
        
        // Handle option
        switch (option.getAction()) {
            case "modules":
                showModuleManager();
                break;
            case "autobuy":
                showAutoBuySystem();
                break;
            case "accounts":
                showAccountManager();
                break;
            case "money":
                showMoneyMaking();
                break;
            case "visual":
                currentPage = 2; // Visual Settings page
                break;
            case "hud":
                currentPage = 4; // HUD Settings page
                break;
            case "themes":
                currentPage = 3; // Theme Settings page
                break;
            case "advanced":
                showAdvancedSettings();
                break;
            case "stats":
                showStatistics();
                break;
            case "help":
                showHelp();
                break;
            case "back":
                goBack();
                break;
            case "exit":
                close();
                break;
            case "dark":
                applyTheme("Dark");
                break;
            case "light":
                applyTheme("Light");
                break;
            case "rainbow":
                applyTheme("Rainbow");
                break;
            case "matrix":
                applyTheme("Matrix");
                break;
            case "funtime":
                applyTheme("Funtime");
                break;
            case "custom":
                showCustomTheme();
                break;
            default:
                Logger.info("Selected: " + option.getName());
                break;
        }
    }
    
    private void goBack() {
        if (currentPage > 0) {
            currentPage = 0;
            selectedOption = 0;
        } else {
            close();
        }
    }
    
    private void showModuleManager() {
        Logger.info("Opening Module Manager...");
        // Implementation for module manager
    }
    
    private void showAutoBuySystem() {
        Logger.info("Opening AutoBuy System...");
        // Implementation for AutoBuy system
    }
    
    private void showAccountManager() {
        Logger.info("Opening Account Manager...");
        // Implementation for account manager
    }
    
    private void showMoneyMaking() {
        Logger.info("Opening Money Making...");
        // Implementation for money making
    }
    
    private void showAdvancedSettings() {
        Logger.info("Opening Advanced Settings...");
        // Implementation for advanced settings
    }
    
    private void showStatistics() {
        Logger.info("Opening Statistics...");
        // Implementation for statistics
    }
    
    private void showHelp() {
        Logger.info("Opening Help...");
        // Implementation for help
    }
    
    private void showThemes() {
        Logger.info("Available themes: Dark, Light, Rainbow, Matrix, Funtime, Custom");
    }
    
    private void showEffects() {
        Logger.info("Available effects: Glow, Outline, Shadow, Gradient, Pulse, Bounce, Slide, Fade");
    }
    
    private void showCustomTheme() {
        Logger.info("Opening Custom Theme Creator...");
        // Implementation for custom theme creator
    }
    
    private void applyTheme(String themeName) {
        currentTheme = themeName;
        Logger.info("Applied theme: " + themeName);
        
        // Apply theme to HUD manager if available
        if (client.getHUDManager() != null) {
            // Apply theme colors
            switch (themeName) {
                case "Dark":
                    client.getHUDManager().setPrimaryColor("§f");
                    client.getHUDManager().setSecondaryColor("§8");
                    client.getHUDManager().setAccentColor("§d");
                    break;
                case "Light":
                    client.getHUDManager().setPrimaryColor("§0");
                    client.getHUDManager().setSecondaryColor("§7");
                    client.getHUDManager().setAccentColor("§9");
                    break;
                case "Rainbow":
                    client.getHUDManager().setPrimaryColor("§c");
                    client.getHUDManager().setSecondaryColor("§7");
                    client.getHUDManager().setAccentColor("§d");
                    break;
                case "Matrix":
                    client.getHUDManager().setPrimaryColor("§a");
                    client.getHUDManager().setSecondaryColor("§2");
                    client.getHUDManager().setAccentColor("§a");
                    break;
                case "Funtime":
                    client.getHUDManager().setPrimaryColor("§e");
                    client.getHUDManager().setSecondaryColor("§7");
                    client.getHUDManager().setAccentColor("§6");
                    break;
            }
        }
    }
    
    private void startAnimation() {
        animating = true;
        animationFrame = 0;
        animationText = "CheatClient";
        animationIndex = 0;
    }
    
    private void addParticle(String symbol, String color) {
        particles.add(new MenuParticle(symbol, color));
    }
    
    private void clearScreen() {
        System.out.print("\033[H\033[2J");
        System.out.flush();
    }
    
    // Toggle methods
    private void toggleAnimations() {
        showAnimations = !showAnimations;
        Logger.info("Animations: " + (showAnimations ? "ON" : "OFF"));
    }
    
    private void toggleParticles() {
        showParticles = !showParticles;
        Logger.info("Particles: " + (showParticles ? "ON" : "OFF"));
    }
    
    private void toggleRainbow() {
        showRainbow = !showRainbow;
        Logger.info("Rainbow: " + (showRainbow ? "ON" : "OFF"));
    }
    
    private void toggleMatrix() {
        showMatrix = !showMatrix;
        Logger.info("Matrix: " + (showMatrix ? "ON" : "OFF"));
    }
    
    private void toggleGlow() {
        showGlow = !showGlow;
        Logger.info("Glow: " + (showGlow ? "ON" : "OFF"));
    }
    
    private void toggleOutline() {
        showOutline = !showOutline;
        Logger.info("Outline: " + (showOutline ? "ON" : "OFF"));
    }
    
    private void toggleShadow() {
        showShadow = !showShadow;
        Logger.info("Shadow: " + (showShadow ? "ON" : "OFF"));
    }
    
    private void toggleGradient() {
        showGradient = !showGradient;
        Logger.info("Gradient: " + (showGradient ? "ON" : "OFF"));
    }
    
    private void togglePulse() {
        showPulse = !showPulse;
        Logger.info("Pulse: " + (showPulse ? "ON" : "OFF"));
    }
    
    private void toggleBounce() {
        showBounce = !showBounce;
        Logger.info("Bounce: " + (showBounce ? "ON" : "OFF"));
    }
    
    private void toggleSlide() {
        showSlide = !showSlide;
        Logger.info("Slide: " + (showSlide ? "ON" : "OFF"));
    }
    
    private void toggleFade() {
        showFade = !showFade;
        Logger.info("Fade: " + (showFade ? "ON" : "OFF"));
    }
    
    private void resetEffects() {
        showAnimations = true;
        showParticles = true;
        showEffects = true;
        showRainbow = false;
        showMatrix = false;
        showGlow = true;
        showOutline = true;
        showShadow = true;
        showGradient = true;
        showPulse = false;
        showBounce = false;
        showSlide = true;
        showFade = true;
        Logger.info("Effects reset to default");
    }
    
    // Inner classes
    public static class MenuPage {
        private String title;
        private List<MenuOption> options;
        
        public MenuPage(String title, List<MenuOption> options) {
            this.title = title;
            this.options = options;
        }
        
        public String getTitle() { return title; }
        public List<MenuOption> getOptions() { return options; }
    }
    
    public static class MenuOption {
        private String icon;
        private String name;
        private String description;
        private String action;
        
        public MenuOption(String icon, String name, String description, String action) {
            this.icon = icon;
            this.name = name;
            this.description = description;
            this.action = action;
        }
        
        public String getIcon() { return icon; }
        public String getName() { return name; }
        public String getDescription() { return description; }
        public String getAction() { return action; }
    }
    
    public static class MenuEffect {
        // Base class for menu effects
    }
    
    public static class MatrixEffect extends MenuEffect {
        // Matrix effect implementation
    }
    
    public static class RainbowEffect extends MenuEffect {
        // Rainbow effect implementation
    }
    
    public static class GlowEffect extends MenuEffect {
        // Glow effect implementation
    }
    
    public static class OutlineEffect extends MenuEffect {
        // Outline effect implementation
    }
    
    public static class ShadowEffect extends MenuEffect {
        // Shadow effect implementation
    }
    
    public static class GradientEffect extends MenuEffect {
        // Gradient effect implementation
    }
    
    public static class PulseEffect extends MenuEffect {
        // Pulse effect implementation
    }
    
    public static class BounceEffect extends MenuEffect {
        // Bounce effect implementation
    }
    
    public static class SlideEffect extends MenuEffect {
        // Slide effect implementation
    }
    
    public static class FadeEffect extends MenuEffect {
        // Fade effect implementation
    }
    
    public static class MenuParticle {
        private String symbol;
        private String color;
        private int life;
        private int maxLife;
        
        public MenuParticle(String symbol, String color) {
            this.symbol = symbol;
            this.color = color;
            this.life = 0;
            this.maxLife = 30;
        }
        
        public void update() {
            life++;
        }
        
        public boolean isDead() {
            return life >= maxLife;
        }
        
        @Override
        public String toString() {
            return color + symbol;
        }
    }
}