package io.github.mxiwbr.capturebiomes.listener;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.factories.ParticleFactory;
import io.github.mxiwbr.capturebiomes.services.UpdateService;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.utils.BlockUtils;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.World;
import org.bukkit.entity.AreaEffectCloud;
import org.bukkit.entity.Player;
import org.bukkit.entity.ThrownPotion;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.LingeringPotionSplashEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.util.BoundingBox;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

/**
 * Listener class for entity events
 */
public class EntityListener implements Listener {

    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {

        // cancel if plugin functionalities are disabled in the config
        if (!CaptureBiomes.CONFIG.isPluginEnabled()) { return; }

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

            log("Creation of biome bottle at " + potionEntity.getLocation() + " failed: the biome is either not supported or could not be found. " +
                    "If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.LogType.WARNING);
            areaEffectCloud.remove();
            return;
        }

        // Cancel if not biome potion (key capturebiomes.biomepotion)
        if (!pdc.has(key)) { return; }

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

        log("A biome of type " + biome.getKey().getKey() + " with size " + CaptureBiomes.CONFIG.getBiomePotionSize()[tier - 1] + " x " + CaptureBiomes.CONFIG.getBiomePotionSize()[tier - 1] + " was created at center " + potionEntity.getLocation(), ConsoleUtils.LogType.ADDITIONAL_INFO);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (player.isOp() && CaptureBiomes.newVersionAvailable) {
            UpdateService.sendUpdateMessageToPlayer(player);
        }

    }

}
