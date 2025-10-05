# CheatClient - Advanced Minecraft Cheat Client

A powerful standalone cheat client for Minecraft 1.16.5 with advanced anti-detection and comprehensive features.

## ⚠️ Disclaimer

This software is for educational purposes only. Use at your own risk and only on servers where cheating is allowed. The developers are not responsible for any consequences of using this software.

## 🚀 Features

### Movement
- **Sprint** - Automatic sprinting
- **WaterSpeed** - Increased swimming speed with Funtime bypass
- **Spider** - Climb walls like a spider with Funtime bypass

### Render
- **XRay** - 2 modes: Normal (configurable ores) & Ancient (ancient debris only)
- **ESP** - 3 types: Block ESP, Item ESP, Ancient ESP
- **Tracers** - Draw lines to entities
- **Fullbright** - 2 modes: Potion effect & Custom brightness
- **NoRender** - Disable certain rendering

### Combat
- **KillAura** - Advanced killaura with Matrix & Funtime bypasses
- **AutoClicker** - Tape mouse with configurable CPS
- **AutoEat** - Automatically eat food
- **AutoTool** - Select best tool with Funtime bypass
- **TriggerBot** - Auto-attack when crosshair is on target

### Player
- **AutoFish** - Fast fishing with custom enchant support
- **AutoMine** - Mine ores at spawn coordinates
- **AutoFarm** - Auto-farm with instant replanting
- **AutoBuild** - Build farms from schematics
- **AutoCreeperFarm** - Manage creeper farms at coordinates

### World
- **AutoWalk** - Baritone pathfinding integration
- **AutoJump** - Configurable jump intervals
- **AutoLog** - Auto-register with /reg command
- **AutoRespawn** - Automatic respawn
- **AutoReconnect** - Automatic reconnection
- **AntiAFK** - Prevent AFK detection with random actions

### Misc
- **ChatBot** - Automatic chat responses

## 🛡️ Anti-Detection Features

### Bypass Systems
- **Matrix Bypass** - Advanced bypass for Matrix anticheat
- **Funtime Bypass** - Specialized bypass for Funtime servers
- **Tape Mouse** - Prevents click pattern detection
- **Randomization** - Random delays and variations to avoid detection
- **Silent Rotation** - Smooth rotation to avoid detection

### Advanced Features
- **Custom Enchant Support** - Works with custom enchantments
- **Coordinate-based Farming** - Manage farms at specific coordinates
- **Baritone Integration** - Advanced pathfinding with Baritone
- **Configurable Delays** - Customizable timing for all actions
- **Anti-AFK System** - Prevents AFK detection with random actions

## Installation

### Prerequisites
- Java 8 or higher
- Minecraft 1.16.5

### Building from Source
1. Clone this repository
2. Run `./gradlew build`
3. Find the JAR file in `build/libs/`

### Running
```bash
java -jar cheatclient-1.0.0.jar
```

## Usage

### GUI Controls
- **Right Shift** - Open/Close GUI
- **WASD** - Navigate through modules
- **Enter** - Toggle selected module
- **Escape** - Close GUI

### Configuration
The client automatically saves your configuration to `cheatclient_config.json`. You can edit this file to change default settings.

### Key Bindings
Each module can be bound to a key for quick toggling. Use the GUI to configure key bindings.

## Architecture

### Core Components
- **CheatClient** - Main client class
- **EventManager** - Event system for modules
- **ModuleManager** - Manages all cheat modules
- **GuiManager** - Handles the user interface
- **ConfigManager** - Configuration management

### Module System
All cheat features are implemented as modules that extend the base `Module` class. This provides a clean, extensible architecture.

### Event System
The client uses a custom event system that allows modules to listen for game events like ticks, rendering, and key presses.

## Development

### Adding New Modules
1. Create a new class extending `Module`
2. Implement `onEnable()` and `onDisable()` methods
3. Register the module in `ModuleManager.registerModules()`

### Example Module
```java
public class MyModule extends Module {
    public MyModule() {
        super("MyModule", "Description", Category.MISC, 0);
    }
    
    @Override
    protected void onEnable() {
        // Module enabled logic
    }
    
    @Override
    protected void onDisable() {
        // Module disabled logic
    }
}
```

## Security Features

- **Anti-Detection** - Basic protection against server-side detection
- **Configurable** - All features can be customized
- **Safe Defaults** - Conservative default settings

## License

This project is licensed under the MIT License - see the LICENSE file for details.

## Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Submit a pull request

## Support

For support, please open an issue on GitHub.

## Changelog

### Version 1.0.0
- Initial release
- Complete module system
- GUI interface
- Configuration system
- 25+ cheat modules