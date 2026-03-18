package io.github.mxiwbr.capturebiomes.listener;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.factories.ParticleFactory;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.utils.BlockUtils;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logConsole;

/**
 * Listener class for entity events
 */
public class EntityListener implements Listener {

    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {

        // The potion that triggered the event
        ThrownPotion potionEntity = event.getEntity();
        ItemStack potionItem = potionEntity.getItem();
        
        // The world in which the potion was thrown
        World world = potionEntity.getLocation().getWorld();

        // The areaEffectCloud that was spawned by splashing the potion
        AreaEffectCloud areaEffectCloud = event.getAreaEffectCloud();

        PersistentDataContainer pdc = potionItem.getItemMeta().getPersistentDataContainer();

        NamespacedKey key = new NamespacedKey(CaptureBiomes.INSTANCE, "capturebiomes.biomepotion");

        // Get the biome from pdt key "capturebiomes.biomepotion.biome"
        var biomeKey = pdc.get(new NamespacedKey(CaptureBiomes.INSTANCE, "capturebiomes.biomepotion.biome"), PersistentDataType.STRING);
        var biomes = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        var biome = biomes.get(NamespacedKey.fromString(biomeKey));

        // Cancel and delete areaEffectCloud if not in overworld
        if (world.getEnvironment() != World.Environment.NORMAL) {

            logConsole("Creation of biome bottle at " + potionEntity.getLocation() + " failed: the biome is either not supported or could not be found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.logType.WARNING);
            areaEffectCloud.remove();
            return;
        }

        // Cancel if not biome potion (key capturebiomes.biomepotion)
        if (!pdc.has(key)) { return; }

        logConsole("A thrown Biome Potion was detected at: " + potionEntity.getLocation(), ConsoleUtils.logType.ADDITIONAL_INFO);

        // the tier (level) of the potion (1 - 4)
        final int tier = pdc.get(key, PersistentDataType.INTEGER);
        areaEffectCloud.setRadius(0);

        final int maxHeight = world.getMaxHeight();
        // next block above the location where the potion was thrown
        final int nextBlockY = BlockUtils.getNextSolidBlockY(potionEntity.getLocation());

        final BoundingBox boundingBox = BlockUtils.getBoundingBox(potionEntity.getLocation(),
                switch (tier) {
                    case 2 -> CaptureBiomes.CONFIG.getBiomePotionSize()[1];
                    case 3 -> CaptureBiomes.CONFIG.getBiomePotionSize()[2];
                    case 4 -> CaptureBiomes.CONFIG.getBiomePotionSize()[3];
                    default -> CaptureBiomes.CONFIG.getBiomePotionSize()[0];
                },
                Math.min(nextBlockY + 5, maxHeight));

        // particle effect up to max world height or next block on y coordinate above the block
        ParticleFactory.createSquareRisingEdges(potionEntity.getLocation(),
                potionEntity.getPotionMeta().getColor(),
                switch (tier) {
                    case 2 -> CaptureBiomes.CONFIG.getBiomePotionSize()[1];
                    case 3 -> CaptureBiomes.CONFIG.getBiomePotionSize()[2];
                    case 4 -> CaptureBiomes.CONFIG.getBiomePotionSize()[3];
                    default -> CaptureBiomes.CONFIG.getBiomePotionSize()[0];
                },
                Math.min(nextBlockY + 5, maxHeight));

        // get a bounding box representing the particle box and fill biome
        BiomeUtils.fillBiome(world, boundingBox, biome);

        // Refresh affected chunks for players to see the biome change instantly
        BlockUtils.refreshChunksFromBoundingBox(boundingBox, world);

        logConsole("A biome of type " + biome.getKey().getKey() + " with size " + tier + " x " + tier + " was created at center " + potionEntity.getLocation(), ConsoleUtils.logType.ADDITIONAL_INFO);

    }

}
