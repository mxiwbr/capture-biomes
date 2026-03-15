package io.github.mxiwbr.capturebioms.utils;

import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.BoundingBox;

public class BiomeUtils {

    /**
     * Get a matching color for a biome
     * @param biomeKey
     * @return Bukkit Color or null, if no color could be found
     */
    public static Color getBiomeColor(String biomeKey) {

        // set the potion color depending on the biome
        return (switch (biomeKey) {

            // snowy biomes
            case "snowy_tundra", "snowy_plains", "snowy_slopes", "snowy_beach", "snowy_taiga", "jagged_peaks" -> Color.WHITE;
            // icy biomes
            case "ice_spikes", "frozen_peaks", "frozen_ocean", "frozen_river", "deep_frozen_ocean" -> Color.AQUA;

            // hot biomes
            case "desert" -> Color.YELLOW;
            case "badlands", "eroded_badlands" -> Color.fromRGB(187, 101, 33);
            case "savanna", "savanna_plateau", "windswept_savanna" -> Color.ORANGE;

            // forest and plains / meadow
            case "forest", "birch_forest", "dark_forest", "flower_forest",
                 "old_growth_birch_forest", "old_growth_pine_taiga",
                 "old_growth_spruce_taiga", "windswept_forest", "grove", "taiga", "meadow", "cherry_grove", "sunflower_plains", "plains" -> Color.GREEN;

            // pale
            case "pale_garden" -> Color.GRAY;

            // jungle
            case "jungle", "bamboo_jungle", "sparse_jungle" -> Color.LIME;

            // oceans and rivers
            case "warm_ocean" -> Color.fromRGB(37, 105, 116);
            case "ocean", "deep_ocean", "lukewarm_ocean",
                 "cold_ocean", "deep_lukewarm_ocean", "deep_cold_ocean", "river" -> Color.fromRGB(35, 63, 124);

            // swamp
            case "swamp", "mangrove_swamp" -> Color.fromRGB(105, 78, 70);

            // stony biomes and mountains
            case "stony_peaks", "stony_shore", "windswept_gravelly_hills", "windswept_hills" -> Color.GRAY;

            default -> null;

        });

    }

    public static void fillBiome(World world, BoundingBox box, Biome biome) {

        for (int x = (int) box.getMinX(); x <= box.getMaxX(); x++) {

            for (int z = (int) box.getMinZ(); z <= box.getMaxZ(); z++) {

                for (int y = (int) box.getMinY(); y <= box.getMaxY(); y++) {

                    world.setBiome(x, y, z, biome);

                }

            }

        }
    }

}
