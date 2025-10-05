# CheatClient - Standalone Minecraft Cheat Client

A powerful standalone cheat client for Minecraft 1.16.5 with a comprehensive set of features.

## ⚠️ Disclaimer

This software is for educational purposes only. Use at your own risk and only on servers where cheating is allowed. The developers are not responsible for any consequences of using this software.

## Features

### Movement
- **Fly** - Creative mode flight
- **Speed** - Increased movement speed
- **Sprint** - Automatic sprinting
- **NoFall** - Prevents fall damage
- **Step** - Step up higher blocks
- **Jesus** - Walk on water

### Render
- **XRay** - See through blocks to find ores
- **ESP** - Highlight entities through walls
- **Tracers** - Draw lines to entities
- **Fullbright** - Bright lighting
- **NoRender** - Disable certain rendering

### Combat
- **KillAura** - Automatically attack nearby entities
- **AutoClicker** - Automatic clicking
- **AutoArmor** - Automatically equip armor
- **AutoEat** - Automatically eat food
- **AutoTool** - Select best tool for the job

### Player
- **AutoFish** - Automatic fishing
- **AutoMine** - Automatic mining
- **AutoFarm** - Automatic farming
- **AutoBuild** - Automatic building

### World
- **AutoWalk** - Automatic walking
- **AutoJump** - Automatic jumping
- **AutoPlace** - Automatic block placing
- **AutoBreak** - Automatic block breaking

### Misc
- **AutoLog** - Automatic logout
- **AutoRespawn** - Automatic respawn
- **AutoReconnect** - Automatic reconnection
- **ChatBot** - Automatic chat responses

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