package io.github.mxiwbr.capturebioms;

import io.github.mxiwbr.capturebioms.utils.ItemFactory;
import io.github.mxiwbr.capturebioms.utils.ItemUtils;
import org.bukkit.*;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class BeaconRitual {

    /**
     * Starts and manages the whole beacon ritual
     * @param location location of the "source" items
     * @param item the item which is dropped on the beacon (xp bottles)
     * @param requiredBottleAmount the amount of required bottles for the ritual
     */
    public static void startBeaconRitual(Location location, Item item, int requiredBottleAmount, int beaconTier) {

        // the world in which the ritual takes place
        // not supported: nether, end, custom
        var world = location.getWorld();
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {
            CaptureBioms.LOGGER.warning("Creation of biome bottle at " + location.toString() + " failed: the dimension is either not supported or not found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");
            return;
        }

        // Get biome for biome bottle creation
        Biome biome = location.getBlock().getBiome();

        // Cancel if biome is not supported or
        switch (biome.getKey().getKey()) {

            // ice and snow biomes (except waters)
            case "snowy_tundra", "snowy_plains", "snowy_slopes", "snowy_beach",
                 "snowy_taiga", "ice_spikes", "frozen_peaks",

            // hot biomes
            "desert", "savanna", "savanna_plateau", "badlands", "eroded_badlands", "windswept_savanna",

            // forest and plains / meadow
            "forest", "birch_forest", "dark_forest", "flower_forest",
            "old_growth_birch_forest", "old_growth_pine_taiga",
            "old_growth_spruce_taiga", "windswept_forest", "grove", "taiga", "meadow", "cherry_grove", "sunflower_plains", "plains",

            // pale
            "pale_garden",

            // jungle
            "jungle", "bamboo_jungle", "sparse_jungle",

            // oceans and rivers
            "ocean", "deep_ocean", "warm_ocean", "lukewarm_ocean",
                 "cold_ocean", "deep_lukewarm_ocean", "deep_cold_ocean",
                 "frozen_ocean", "frozen_river", "deep_frozen_ocean", "river",

            // swamp
            "swamp", "mangrove_swamp",

            // stony biomes and mountains
            "stony_peaks", "stony_shore", "jagged_peaks", "windswept_gravelly_hills", "windswept_hills" -> {}

            // if not supported, cancel
            // for example caves, deep dark or custom bioms
            default -> {
                CaptureBioms.LOGGER.warning("Creation of biome bottle at " + location.toString() + " failed: the biome is either not supported or could not be found. " +
                                                "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");
                return;
            }

        }

        // Play sound and particles
        world.playSound(location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
        world.spawnParticle(Particle.PORTAL, location, 50);

        // Remove the items that are for the beacon ritual
        ItemUtils.removeItemsFromStack(item, requiredBottleAmount);

        // create potion from ItemFactory
        ItemStack potion = ItemFactory.createBiomePotion(biome);
        if (potion == null) {

            CaptureBioms.LOGGER.warning("Creation of biome bottle at " + location.toString() + " failed: the biome is either not supported or could not be found. " +
                                            "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");
            return;
        }

        // set the amount of biome potions to get
        // beacon tier 1 -> 1x
        // beacon tier 2 -> 2x
        // ...
        potion.setAmount(beaconTier);
        // drop potion on location and play sound
        world.dropItemNaturally(location, potion);
        world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);

    }

}
