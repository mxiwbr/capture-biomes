# Capture Biomes

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.java.com/)
[![Minecraft](https://img.shields.io/badge/Minecraft_Java_Edition-1.21.5_to_26.1.1-blue)](https://www.minecraft.net/de-de/article/minecraft-java-edition-26-1-1)
[![API](https://img.shields.io/badge/API-PaperMC-white)](https://papermc.io/)
[![bStats](https://img.shields.io/badge/bStats-Active-blue)](https://bstats.org/plugin/bukkit/CaptureBiomes/30340)
[![Plugin](https://img.shields.io/badge/Type-Plugin-yellow)](#)
[![License](https://img.shields.io/badge/License-GPLv3-lightgrey)](https://www.gnu.org/licenses/gpl-3.0.en.html#license-text)
[![Open Source](https://img.shields.io/badge/Open%20Source-Yes-brightgreen)](https://opensource.org/)
[![Modrinth](https://img.shields.io/badge/Available%20on-Modrinth-00AF5C?logo=modrinth)](https://modrinth.com/project/capture-biomes)

Ever dreamed of bringing a snowy tundra to your sunny desert world? Or maybe turn your forest into a swampy wonderland?  
With **Capture Biomes**, you can bottle up any biome and unleash it anywhere –
reshape your own Minecraft world exactly the way you want!

## Features
🧪 Capture any biome in a **Biome Potion** using a Beacon  
🎨 Potion colors, names, and descriptions match the captured biome  
📏 Bigger Biome Potions depending on the Beacon pyramid size  
⚙️ Full configuration support in the `config.yml`

## Usage
1. Place a Beacon and power it with the usual pyramid
2. Drop the required amount of XP bottles on top of the beacon:
   - Tier 1 Beacon + 16 XP bottles → 4x4 Biome Potion
   - Tier 2 Beacon + 32 XP bottles → 8x8 Biome Potion
   - Tier 3 Beacon + 48 XP bottles → 16x16 Biome Potion
   - Tier 4 Beacon + 64 XP bottles → 32x32 Biome Potion
3. The beacon automatically creates a **Biome Potion** of the biome in which the beacon is located
4. Throw the potion on the ground in another biome to change the biome in the area  

The tier of the Beacon depends on the size of the pyramid:
- 1 layer → Tier 1
- 2 layers → Tier 2
- 3 layers → Tier 3
- 4 layers → Tier 4

## Installation
1. Download the latest .jar from [Modrinth](https://modrinth.com/project/capture-biomes/versions) or [GitHub](https://github.com/mxiwbr/capture-biomes/releases)
2. Place it in your server's `plugins` folder
3. (Optional) Configure the plugin via `config.yml` and restart the server or use the plugin's reload command

## Potion Size
The size of a Biome Potion depends on the tier of the beacon used to create it.  
When thrown, the potion affects a cuboid of `size` × `size` × `size` blocks, `size` being the matching value for the Beacon tier (configurable in the `config.yml`).   

**Example:** A potion created on a tier 2 Beacon affects an area of 8 × 8 × 8 blocks, with the location where the potion lands as the center.

**Height behavior:**
- If there is a solid block above the center coordinate (e.g. in a cave), the biome extends up to **`size` blocks above that block**.
- If there is no solid block above the center, the biome extends up to the **world's maximum height**.
- The biome always extends **`size` blocks below the point where the potion lands**.
- The amount of blocks to extend up- and downwards is **configurable in the config.yml** by modifying the `y-offset-multiplier`.

## Commands

| Command                                         | Description                                                                                                         | Permission |
|-------------------------------------------------|---------------------------------------------------------------------------------------------------------------------|------------|
| `/capturebiomes disable`                        | Disables the plugin.                                                                                                | OP |
| `/capturebiomes enable`                         | Enables the plugin.                                                                                                 | OP |
| `/capturebiomes givebiomepotion <biome> <tier>` | Gives a Biome Potion of the specified biome and tier.<br>**Example:** `/capturebiomes givebiomepotion snowy_taiga 3` | OP |
| `/capturebiomes reloadconfig`                   | Reloads the plugin’s configuration (`config.yml`).                                                                  | OP |
| `/capturebiomes resetconfig`                    | Resets the plugin’s configuration (`config.yml`) and automatically reloads it.                                      | OP |
| `/capturebiomes help`                           | Shows this command overview in the ingame chat.                                                                     | OP |
| `/biome`                                        | Shows the biome you are currently standing in.                                                                      | Everyone |

## Supported Versions
- PaperMC on Minecraft Versions 1.21.5 - 26.1.2
- PurpurMC on Minecraft Versions 1.21.5 - 26.1.2

## Config
Edit the `config.yml` in the plugin's folder to adapt it to your preferences:
```
# =========================================
# Capture Biomes - Configuration
# =========================================

enabled: true # Enable/disable plugin functionality

# =========================================
# Beacon Settings
# =========================================
beacon:

  # Required items per beacon tier
  # Must be > 0 and <= max stack size of trigger_item
  required-item-count:
    tier-1: 16
    tier-2: 32
    tier-3: 48
    tier-4: 64

  # Size of biome potions (NxN area)
  # Must be > 0
  biome-potions-size:
    tier-1: 4
    tier-2: 8
    tier-3: 16
    tier-4: 32

    # Vertical range of biome spread (multiplier)
    # Determines how many blocks the biome extends:
    # - BELOW the impact point
    # - ABOVE the first solid block at the center's y-axis
    # The actual range (up and down) is calculated by multiplying the base size of the potion by this multiplier
    # Must be a floating-point number >= 0.0 and <= 5.0
    # Setting this to 0.0 will completely disable the vertical offset
    y-offset-multiplier: 1.0

  trigger_item: EXPERIENCE_BOTTLE # Item used to trigger the ritual
  biome-potions-amount: 1         # Amount of potions to get each time (> 0)

# =========================================
# Biome Potion Settings
# =========================================
potion-cooldown:
  enabled: true  # Enable cooldown
  length: 15     # Seconds (> 0)

# =========================================
# Biome Toggles
# =========================================
# Already created potions are NOT affected
biomes:
  enable-mushroom_fields: false
  enable-deep_dark: false

# =========================================
# Advanced Settings – only modify if you know what you're doing!
# Misconfiguring these could potentially break the plugin or cause unexpected behavior.
# =========================================

console:
  enable-logging: true
  enable-additional-logging: false # Extra debug logs

item-check:
  timeout-ticks: 200  # Total duration (20 ticks = 1s)
  interval-ticks: 2   # Check interval

bstats:
  enabled: true
```

#### Resetting the config
In order to reset the plugin's config to its default values, either use the `/capturebiomes resetconfig` command or delete the config.yml in the plugin's folder and restart the server.

## Supported Biomes and Dimensions
This plugin supports most **overworld biomes** (listed below).  
Nether and End biomes as well as custom biomes and dimensions are currently not supported and will be ignored.
#### 🔵 Cold
`Deep Cold Ocean`, `Cold Ocean`, `Snowy Tundra`, `Snowy Plains`, `Snowy Slopes`, `Snowy Beach`, `Snowy Taiga`, `Ice Spikes`, `Frozen Peaks`, `Frozen Ocean`, `Frozen River`, `Deep Frozen Ocean`

#### 🟡 Hot
`Desert`, `Savanna`, `Savanna Plateau`, `Badlands`, `Eroded Badlands`, `Wooded Badlands`, `Windswept Savanna`, `Beach`

#### 🟢 Forest / Plains
`Forest`, `Birch Forest`, `Dark Forest`, `Flower Forest`, `Old Growth Birch Forest`, `Old Growth Pine Taiga`, `Old Growth Spruce Taiga`, `Windswept Forest`, `Grove`, `Taiga`, `Meadow`, `Cherry Grove`, `Sunflower Plains`, `Plains`, `Pale Garden`

#### 🟢 Jungle
`Jungle`, `Bamboo Jungle`, `Sparse Jungle`

#### 🔵 Oceans / Rivers
`Ocean`, `Deep Ocean`, `Warm Ocean`, `Lukewarm Ocean`, `Deep Lukewarm Ocean`, `River`

#### 🟤 Swamps
`Swamp`, `Mangrove Swamp`

#### ⚪ Mountains / Stony
`Stony Peaks`, `Stony Shore`, `Jagged Peaks`, `Windswept Gravelly Hills`, `Windswept Hills`

#### ⚫ Caves / Underground
`Lush Caves`, `Dripstone Caves`, `Deep Dark` (disabled by default, configurable in config)

#### 🔘 Other
`Mushroom Fields` (disabled by default, configurable in config)

## Help
If you need any help, please feel free to open an issue: [Open an issue](https://github.com/mxiwbr/capture-biomes/issues)

## bStats

This plugin uses [bStats](https://bstats.org/) to collect **anonymous statistics** like player counts and server versions.  
All data collected is **anonymous and secure**, helping to improve the plugin.  
If you don't want the plugin to send data, disable bStats in the config.yml.

## License
This plugin is licensed under the **GNU General Public License v3**.  
- You are free to **use, modify, and redistribute** the plugin **under the same GPLv3 license**.
- **Uploading** or sharing the plugin **without proper modifications** is **strictly prohibited**.
- You must always provide **credit to the original author**.  
- For more information, see the full license: [GNU GPL v3](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Credits

Developed by [**mxiwbr**](https://github.com/mxiwbr)
