# Capture Biomes

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.java.com/)
[![Minecraft](https://img.shields.io/badge/Minecraft_Java_Edition-1.21.11-blue)](https://www.minecraft.net/de-de/article/minecraft-java-edition-1-21-11)
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
When thrown, the potion affects a square of `size` × `size` blocks, `size` being the matching value for the Beacon tier (configurable in the `config.yml`).   

**Example:** A potion created on a tier 2 Beacon affects an area of 8 × 8 blocks, with the location where the potion lands as the center.

**Height behavior:**
- If there is a solid block above the center coordinate (e.g. in a cave), the biome extends up to **five blocks above that block**.
- If there is no solid block above the center, the biome extends up to the **world's maximum height**.
- The biome always extends **five blocks below the point where the potion lands**.

## Commands

| Command | Description                                                                                                    | Permission |
|---------|----------------------------------------------------------------------------------------------------------------|------------|
| `/capturebiomes disable` | Disables the plugin.                                                                                           | OP |
| `/capturebiomes enable` | Enables the plugin.                                                                                            | OP |
| `/capturebiomes givebiomepotion <biome> <tier>` | Gives a Biome Potion of the specified biome and tier.<br>**Example:** `/capturebiomes givebiomepotion snowy_taiga 3` | OP |
| `/capturebiomes reload` | Reloads the plugin’s configuration (`config.yml`).                                                             | OP |
| `/capturebiomes help` | Shows this command overview in the ingame chat.                                                                | OP |
| `/biome` | Shows the biome you are currently standing in.                                                                 | Everyone |

## Supported Versions
- PaperMC on Minecraft Version 1.21.11

## Config
Edit the `config.yml` in the plugin's folder to adapt it to your preferences:
```
# Capture Biomes Plugin Configuration

enabled: true                         # enable or disable the plugin's basic functionalities
                                      # commands as well as update services will still work
                                      # boolean (true / false)
                                      # can also be changed in game via /capturebiomes enable or /capturebiomes disable

# --- Beacon ritual items ---
beacon:
  required-item-count:                # items required for every beacon level (cannot be more than the max stack size of the trigger_item)
                                      # Have to be > 0
    tier-1: 16
    tier-2: 32
    tier-3: 48
    tier-4: 64
  biome-potions-size:                 # Effect range of the Biome Potion when used (squared → 4 = 4x4 blocks)
                                      # Have to be whole and even numbers
                                      # Have to be > 0
                                      # See https://github.com/mxiwbr/capture-biomes/blob/main/README.md to understand how the size works
    tier-1: 4
    tier-2: 8
    tier-3: 16
    tier-4: 32
  trigger_item: EXPERIENCE_BOTTLE     # The item that must be thrown on the beacon to trigger the biome capture
                                      # See https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for the item names
  biome-potions-amount: 1             # amount of Biome Potions to get each time
                                      # default: 1
                                      # Has to be > 0

# --- Biome Potions ---
potion-cooldown:                      # cooldown of the biome potions
                                      # this affects all potions created after changing this value - old items will still have the same cooldown settings!
                                      # recommended to leave enabled to prevent players from crashing / lagging the server by throwing too many potions at once

  enabled: true                       # default: true
                                      # boolean (true / false)

  length: 15                          # default: 15
                                      # full seconds
                                      # Has to be > 0. To disable it completely, please set potion-cooldown.enabled to false
                                      # only relevant if enabled is true

# --- Biomes ---
biomes:                               # enable or disable specific biomes (potions of these biomes that were already created will still work!)
                                      # boolean (true / false)
  enable-mushroom_fields: false       # default: false
  enable-deep_dark: false             # default: false

### ADVANCED SETTINGS – only change if you know what you're doing! ###

# --- Console Logging ---
console:
  enable-logging: true                # default: true
                                      # boolean (true / false)
                                      # if you set this to false, all console logging will be disabled. This includes all info logs as well as warnings and severe errors

  enable-additional-logging: false    # default: false
                                      # will enable additional logging whenever an action is done by the plugin, like creating or releasing a biome potion
                                      # only relevant if enable-logging is true

# --- Item checking (for XP bottles on beacon) ---
item-check:
  timeout-ticks: 200                  # How long the plugin checks for each item being on ground in ticks (20 ticks = 1 second)
                                      # Has to be > 0
  interval-ticks: 2                   # Interval between each check in ticks (20 ticks = 1 second)
                                      # Has to be > 0

# --- bStats ---
bstats:
  enabled: true                       # default: true
                                      # enables / disables sending anonymous data like amount of servers using the plugin to bStats
                                      # see README.md section "bStats" for more information
```

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
`Lush Caves`, `Dripstone Caves`, `Deep Dark`(disabled by default, configurable in config)

#### 🔘 Other
`Mushroom Fields`(disabled by default, configurable in config)

## Help
If you need any help, please feel free to open an issue: [Open an issue](https://github.com/mxiwbr/capture-biomes/issues)

## bStats

This plugin uses [bStats](https://bstats.org/) to collect **anonymous statistics** like player counts and server versions.  
All data collected is **anonymous and secure**, helping to improve the plugin.  
If you don't want the plugin to send data, disable bStats in the config.yml.

## License
This plugin is licensed under the **GNU General Public License v3**.  
- You are free to **use, modify, and redistribute** the plugin **under the same GPLv3 license**
- You must always provide **credit to the original author**.  
- **Commercial use is strictly prohibited without explicit permission from the original author.**  
- For more information, see the full license: [GNU GPL v3](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Credits

Developed by **mxiwbr** (https://github.com/mxiwbr)