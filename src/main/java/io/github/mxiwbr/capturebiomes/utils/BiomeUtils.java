package io.github.mxiwbr.capturebiomes.utils;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import org.bukkit.Color;
import org.bukkit.World;
import org.bukkit.block.Biome;
import org.bukkit.util.BoundingBox;

public class BiomeUtils {

    /**
     * Get a matching color for a biome or null, if it couldn't be found
     * @param biomeKey
     * @return Bukkit Color / null
     */
    public static Color getBiomeColor(String biomeKey) {

        // set the potion color depending on the biome
        return (switch (biomeKey) {

            // snowy biomes
            case "snowy_tundra", "snowy_plains", "snowy_slopes", "snowy_beach", "snowy_taiga", "jagged_peaks", "grove" -> Color.WHITE;
            // icy biomes
            case "ice_spikes", "frozen_peaks", "frozen_ocean", "frozen_river", "deep_frozen_ocean" -> Color.AQUA;

            // hot biomes
            case "desert", "beach" -> Color.YELLOW;
            case "badlands", "eroded_badlands", "wooded_badlands" -> Color.fromRGB(187, 101, 33);
            case "savanna", "savanna_plateau", "windswept_savanna" -> Color.ORANGE;
            case "pale_garden" -> Color.GRAY;

            // forest and plains / meadow
            case "forest", "birch_forest", "dark_forest", "flower_forest",
                 "old_growth_birch_forest", "old_growth_pine_taiga",
                 "old_growth_spruce_taiga", "windswept_forest", "taiga", "meadow", "sunflower_plains", "plains" -> Color.GREEN;
            case "cherry_grove" -> Color.fromRGB(182, 152, 172);

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

            // caves / underground
            case "lush_caves" -> Color.fromRGB(199, 158, 33);
            case "dripstone_caves" -> Color.fromRGB(129, 98, 85);
            // deep dark, only if enabled in config
            case "deep_dark" -> {
                if (CaptureBiomes.CONFIG.isEnableDeepDarkBiome()) yield Color.BLACK;
                else yield null;
            }

            // other
            // mushroom fields, only if enabled in config
            case "mushroom_fields" -> {
                if (CaptureBiomes.CONFIG.isEnableMushroomFieldsBiome()) yield Color.fromRGB(129, 98, 85);
                else yield null;
            }

            default -> null;

        });

    }

    /**
     * Fills a given area in a given world with a given biome
     * @param world
     * @param box the area which should be filled as BoundingBox
     * @param biome
     */
    public static void fillBiome(World world, BoundingBox box, Biome biome) {

        if (world == null || box == null || biome == null) {
            return;
        }

        int minX = (int) Math.floor(box.getMinX());
        int maxX = (int) Math.floor(box.getMaxX());
        int minZ = (int) Math.floor(box.getMinZ());
        int maxZ = (int) Math.floor(box.getMaxZ());
        int minY = Math.max((int) Math.floor(box.getMinY()), world.getMinHeight());
        int maxY = Math.min((int) Math.floor(box.getMaxY()), world.getMaxHeight() - 1);

        for (int x = minX; x <= maxX; x++) {

            for (int z = minZ; z <= maxZ; z++) {

                for (int y = minY; y <= maxY; y++) {

                    world.setBiome(x, y, z, biome);

                }

            }

        }
    }

}
