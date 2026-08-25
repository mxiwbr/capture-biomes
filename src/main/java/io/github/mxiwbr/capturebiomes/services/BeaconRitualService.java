package io.github.mxiwbr.capturebiomes.services;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.factories.ItemFactory;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.github.mxiwbr.capturebiomes.utils.ItemUtils;
import io.github.mxiwbr.capturebiomes.factories.ParticleFactory;
import org.bukkit.*;
import org.bukkit.block.Beacon;
import org.bukkit.block.Biome;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.util.Vector;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

public class BeaconRitualService {

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

            log("Creation of biome bottle at " + location + " failed: the dimension is either not supported or not found.", ConsoleUtils.LogType.WARNING);
            log("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.LogType.WARNING);
            return;
        }

        // Get biome for biome bottle creation
        Biome biome = location.getBlock().getBiome();

        // Cancel if biome is not supported or
        if (BiomeUtils.getBiomeColor(biome.getKey().getKey()) == null) {

            log("Creation of biome bottle at " + location + " failed: the biome is either not supported or could not be found.", ConsoleUtils.LogType.WARNING);
            log("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.LogType.WARNING);
            return;

        }

        // Play sound and particles
        world.playSound(location, Sound.BLOCK_AMETHYST_BLOCK_RESONATE, 1, 1);
        world.spawnParticle(Particle.PORTAL, location, 50);

        // Remove the items that are for the beacon ritual
        ItemUtils.removeItemsFromStack(item, requiredBottleAmount);

        // the center location above the beacon
        Location center = beacon.getLocation().add(0.5, 1, 0.5);

        world.spawnParticle(Particle.EXPLOSION, center, 1);
        world.playSound(center, Sound.ENTITY_GENERIC_EXPLODE, 1, 1);

        // boost all items away from the beacon
        for (Entity entity : world.getNearbyEntities(center, 2, 2, 2)) {

            if (!(entity instanceof Item itemEntity)) continue;

            Vector direction = itemEntity.getLocation().toVector()
                    .subtract(center.toVector());

            if (direction.lengthSquared() < 1e-6) {
                direction = new Vector(Math.random() - 0.5, 0, Math.random() - 0.5);
            }

            direction.normalize();
            direction.multiply(0.7);
            direction.setY(0.6);

            itemEntity.setVelocity(direction);
        }

        // create potion from ItemFactory
        ItemStack potion = ItemFactory.createBiomePotion(biome, beaconTier);
        if (potion == null) {

            log("Creation of biome bottle at " + location + " failed: the biome is either not supported or could not be found.", ConsoleUtils.LogType.WARNING);
            log("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.LogType.WARNING);
            return;
        }

        // get the amount of potions set in the config (default 1)
        potion.setAmount(CaptureBiomes.CONFIG.getBiomePotionsAmount());
        ParticleFactory.createParticleSpiral(beacon.getLocation(), BiomeUtils.getBiomeColor(biome.getKey().getKey()));
        // drop potion on location and play sound
        world.dropItem(location, potion).setVelocity(new Vector(0, 0, 0));
        world.playSound(location, Sound.ENTITY_ITEM_PICKUP, 1, 1);

        // fake block change to disable the beacon beam for a short time
        for (Player player : world.getPlayers()) {

            if (player.getLocation().distance(center) < 70) {

                player.sendBlockChange(beacon.getLocation(), Material.GLASS.createBlockData());
                player.sendBlockChange(beacon.getLocation(), Material.BEACON.createBlockData());
            }
        }

    }

}
