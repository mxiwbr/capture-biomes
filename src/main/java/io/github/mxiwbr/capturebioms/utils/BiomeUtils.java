package io.github.mxiwbr.capturebioms.utils;

import org.bukkit.Color;

public class BiomeUtils {

    /**
     * Get a matching color for a biome
     * @param biomeKey
     * @return Bukkit Color or null, if no color could be found
     */
    public static Color getBiomeColor(String biomeKey) {

        // set the potion color depending on the biome
        return (switch (biomeKey) {

            // ice and snow biomes
            case "snowy_tundra", "snowy_plains", "snowy_slopes", "snowy_beach",
                 "snowy_taiga", "ice_spikes", "frozen_peaks" -> Color.AQUA;

            // hot biomes
            case "desert", "savanna", "savanna_plateau", "badlands", "eroded_badlands", "windswept_savanna" -> Color.YELLOW;

            // forest and plains / meadow
            case "forest", "birch_forest", "dark_forest", "flower_forest",
                 "old_growth_birch_forest", "old_growth_pine_taiga",
                 "old_growth_spruce_taiga", "windswept_forest", "grove", "taiga", "meadow", "cherry_grove", "sunflower_plains", "plains" -> Color.GREEN;

            // pale
            case "pale_garden" -> Color.GRAY;

            // jungle
            case "jungle", "bamboo_jungle", "sparse_jungle" -> Color.LIME;

            // oceans and rivers
            case "ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean",
                 "cold_ocean", "deep_lukewarm_ocean", "deep_cold_ocean",
                 "frozen_ocean", "frozen_river", "deep_frozen_ocean", "river" -> Color.BLUE;

            // swamp
            case "swamp", "mangrove_swamp" -> Color.fromRGB(105, 78, 70);

            // stony biomes and mountains
            case "stony_peaks", "stony_shore", "jagged_peaks", "windswept_gravelly_hills", "windswept_hills" -> Color.GRAY;

            // fallback color
            default -> null;

        });

    }

}
