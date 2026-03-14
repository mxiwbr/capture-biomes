package io.github.mxiwbr.capturebioms;

import io.github.mxiwbr.capturebioms.utils.BiomeUtils;
import io.github.mxiwbr.capturebioms.utils.ItemFactory;
import io.github.mxiwbr.capturebioms.utils.ItemUtils;
import io.github.mxiwbr.capturebioms.utils.ParticleUtils;
import org.bukkit.*;
import org.bukkit.block.Beacon;
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
    public static void startBeaconRitual(Location location, Item item, int requiredBottleAmount, Beacon beacon, int beaconTier) {

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
        if (BiomeUtils.getBiomeColor(biome.getKey().getKey()) == null) {

            CaptureBioms.LOGGER.warning("Creation of biome bottle at " + location.toString() + " failed: the biome is either not supported or could not be found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");
            return;

        }

        // Play sound and particles
        world.playSound(location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
        world.spawnParticle(Particle.PORTAL, location, 50);

        // Remove the items that are for the beacon ritual
        ItemUtils.removeItemsFromStack(item, requiredBottleAmount);

        // create potion from ItemFactory
        ItemStack potion = ItemFactory.createBiomePotion(biome, beaconTier);
        if (potion == null) {

            CaptureBioms.LOGGER.warning("Creation of biome bottle at " + location.toString() + " failed: the biome is either not supported or could not be found. " +
                                            "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");
            return;
        }

        // get the amount of potions set in the config (default 1)
        potion.setAmount(CaptureBioms.CONFIG.getBiomePotionsAmount());
        ParticleUtils.spawnParticleSpiral(beacon.getLocation(), BiomeUtils.getBiomeColor(biome.getKey().getKey()));
        // drop potion on location and play sound
        world.dropItemNaturally(location, potion);
        world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);

    }

}
