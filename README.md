# Capture Biomes

[![Java](https://img.shields.io/badge/Java-21-brightgreen)](https://www.java.com/)
[![Minecraft](https://img.shields.io/badge/Minecraft-1.21.11-blue)](https://www.minecraft.net/)
[![API](https://img.shields.io/badge/API-PaperMC-white)](https://papermc.io/)
[![Plugin](https://img.shields.io/badge/Type-Plugin-yellow)](#)
[![License](https://img.shields.io/badge/License-GPLv3-lightgrey)](https://www.gnu.org/licenses/gpl-3.0.en.html#license-text)
[![Open Source](https://img.shields.io/badge/Open%20Source-Yes-brightgreen)](https://opensource.org/)

A PaperMC plugin that lets players capture a biome in a bottle and release it elsewhere.

## Features
- **Capture Biomes** allows you to capture a biome in a bottle as a **Biome Potion** by throwing XP Bottles on a **Beacon** and release it elsewhere
- Potion colors, names and description match the captured biome
- Get more than one Biome Potion at a time depending on the size of the Beacon pyramid
- **Full control**: Edit the plugin's config to adapt it to your preferences

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

## Potion Size
The size of a Biome Potion depends on the tier of the beacon used to create it.  
When thrown, the potion affects a square of `size` × `size` blocks, `size` being the matching value for the Beacon tier (configurable in the `config.yml`).   

**Example:** A potion created on a tier 2 Beacon affects an area of 8 × 8 blocks, with the location where the potion lands as the center.

**Height behavior:**
- If there is a block above the center coordinate, the biome extends up to **five blocks above that block**.
- If there is no block above the center, the biome extends up to the **world's maximum height** (y = 320).
- The biome always extends **five blocks below the point where the potion lands**.

## Supported Versions
- PaperMC on Minecraft Version 1.21.11

## Config
Edit the `config.yml` in the plugin's folder to adapt it to your preferences:
```
# Capture Biomes Plugin Configuration

# --- Beacon ritual items ---
beacon:
  required-xp-bottles:                # XP bottles required for every beacon level (cannot be more than 64)
    tier-1: 16
    tier-2: 32
    tier-3: 48
    tier-4: 64
  biome-potions-size:                 # Effect range of the Biome Potion when used (squared → 4 = 4x4 blocks)
                                      # See https://github.com/mxiwbr/capture-biomes/blob/main/README.md to understand how the size works
    tier-1: 4
    tier-2: 8
    tier-3: 16
    tier-4: 32
  trigger_item: EXPERIENCE_BOTTLE     # The item that must be thrown on the beacon to trigger the biome capture
                                      # See https://hub.spigotmc.org/javadocs/spigot/org/bukkit/Material.html for the item names
  biome-potions-amount: 1             #  default: 1 | amount of Biome Potions to get each time

# --- Biome Potions ---
potion-cooldown:                      # cooldown of the biome potions
                                      # recommended to leave enabled to prevent players from crashing / lagging the server by throwing too many potions at once

  enabled: true                       # default: true
                                      # boolean (true / false)

  length: 30                          # default: 30
                                      # full seconds
                                      # only relevant if enabled is true

# --- Advanced settings – only change if you know what you're doing ---

# --- Item checking (for XP bottles on beacon) ---
item-check:
  timeout-ticks: 200                  # How long the plugin checks for each item being on ground in ticks (20 ticks = 1 second)
  interval-ticks: 2                   # Interval between each check in ticks (20 ticks = 1 second)
```

## Supported Biomes and Dimensions
This plugin only supports following **overworld biomes**.
Nether, End and Cave biomes as well as custom biomes and dimensions are currently not supported and will be ignored.
- 🔵 **Cold:**  
  `Deep Cold Ocean`  
  `Cold Ocean`  
  `Snowy Tundra`  
  `Snowy Plains`  
  `Snowy Slopes`  
  `Snowy Beach`  
  `Snowy Taiga`  
  `Ice Spikes`  
  `Frozen Peaks`  
  `Frozen Ocean`  
  `Deep Frozen Ocean`
- 🟡 **Hot:**  
  `Desert`  
  `Savanna`  
  `Savanna Plateau`  
  `Badlands`  
  `Eroded Badlands`  
  `Windswept Savanna`
- 🟢 **Forest / Plains:**  
  `Forest`  
  `Birch Forest`  
  `Dark Forest`  
  `Flower Forest`  
  `Old Growth Birch Forest`  
  `Old Growth Pine Taiga`  
  `Old Growth Spruce Taiga`  
  `Windswept Forest`  
  `Grove`  
  `Taiga`  
  `Meadow`  
  `Cherry Grove`  
  `Sunflower Plains`  
  `Plains`
- 🟢 **Jungle:**  
  `Jungle`  
  `Bamboo Jungle`  
  `Sparse Jungle`
- 🔵 **Oceans / Rivers:**  
  `Ocean`  
  `Deep Ocean`  
  `Warm Ocean`  
  `Lukewarm Ocean`  
  `Deep Lukewarm Ocean`  
  `River`
- 🟤 **Swamps:**  
  `Swamp`  
  `Mangrove Swamp`
- ⚪ **Mountains / Stony:**  
  `Stony Peaks`  
  `Stony Shore`  
  `Jagged Peaks`  
  `Windswept Gravelly Hills`  
  `Windswept Hills`
- ⚪ **Other:**  
  `Pale Garden`

## Help
If you need any help, please feel free to open an issue: [Open an issue](https://github.com/mxiwbr/capture-biomes/issues)

## License
This plugin is licensed under the **GNU General Public License v3**.  
- You are free to **use, modify, and redistribute** the plugin **under the same GPLv3 license**
- You must always provide **credit to the original author**.  
- **Commercial use is strictly prohibited without explicit permission from the original author.**  
- For more information, see the full license: [GNU GPL v3](https://www.gnu.org/licenses/gpl-3.0.en.html)

## Credits

Developed by **mxiwbr** (https://github.com/mxiwbr)