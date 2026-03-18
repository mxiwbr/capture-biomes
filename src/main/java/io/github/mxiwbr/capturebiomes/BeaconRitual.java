package io.github.mxiwbr.capturebiomes;

import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.factories.ItemFactory;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.github.mxiwbr.capturebiomes.utils.ItemUtils;
import io.github.mxiwbr.capturebiomes.factories.ParticleFactory;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Biome;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logConsole;

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

            logConsole("Creation of biome bottle at " + location + " failed: the dimension is either not supported or not found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.logType.WARNING);
            return;
        }

        // Get biome for biome bottle creation
        Biome biome = location.getBlock().getBiome();

        // Cancel if biome is not supported or
        if (BiomeUtils.getBiomeColor(biome.getKey().getKey()) == null) {

            logConsole("Creation of biome bottle at " + location + " failed: the biome is either not supported or could not be found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.logType.WARNING);
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

            logConsole("Creation of biome bottle at " + location + " failed: the biome is either not supported or could not be found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.logType.WARNING);
            return;
        }

        // get the amount of potions set in the config (default 1)
        potion.setAmount(CaptureBioms.CONFIG.getBiomePotionsAmount());
        ParticleFactory.createParticleSpiral(beacon.getLocation(), BiomeUtils.getBiomeColor(biome.getKey().getKey()));
        // drop potion on location and play sound
        world.dropItemNaturally(location, potion);
        world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);

    }

}
