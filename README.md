# Minecraft Cheat Mod

A cheat mod for Minecraft 1.16.5 built with Fabric Loader.

## ⚠️ Disclaimer

This mod is for educational purposes only. Use at your own risk and only on servers where cheating is allowed.

## Features

- **Fly** - Toggle creative flight mode
- **Speed** - Increase movement speed (configurable multiplier)
- **X-Ray** - See through blocks to find ores
- **No Fall** - Take no fall damage
- **Auto Sprint** - Automatically sprint when moving
- **Kill Aura** - Automatically attack nearby entities
- **Configuration** - Save/load settings

## Installation

1. Install [Fabric Loader](https://fabricmc.net/use/) for Minecraft 1.16.5
2. Install [Fabric API](https://www.curseforge.com/minecraft/mc-mods/fabric-api)
3. Download the mod JAR file from the releases page
4. Place it in your `.minecraft/mods` folder
5. Launch Minecraft

## Usage

### Key Bindings

- **F** - Toggle Fly
- **G** - Toggle Speed  
- **X** - Toggle X-Ray
- **N** - Toggle No Fall
- **R** - Toggle Auto Sprint
- **K** - Toggle Kill Aura
- **C** - Save Configuration

### Configuration

The mod automatically saves your settings to `config/minecraft-cheat.json`. You can edit this file to change default values:

```json
{
  "flyEnabled": false,
  "speedEnabled": false,
  "xrayEnabled": false,
  "noFallEnabled": false,
  "autoSprintEnabled": false,
  "killAuraEnabled": false,
  "speedMultiplier": 2.0,
  "killAuraRange": 4.0,
  "killAuraDelay": 20
}
```

## Building from Source

1. Clone this repository
2. Run `./gradlew build`
3. Find the JAR file in `build/libs/`

## Requirements

- Java 8 or higher
- Minecraft 1.16.5
- Fabric Loader 0.11.3+
- Fabric API 0.42.0+

## License

MIT License - see LICENSE file for details.

## Contributing

Feel free to submit issues and pull requests!