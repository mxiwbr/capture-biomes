package io.github.mxiwbr.capturebiomes.listener;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.factories.ParticleFactory;
import io.github.mxiwbr.capturebiomes.services.UpdateService;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.utils.BlockUtils;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import org.bukkit.Color;
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

import static io.github.mxiwbr.capturebiomes.CaptureBiomes.CONFIG;
import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;
import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logCreateIssueMessage;

/**
 * Listener class for entity events
 */
public class EntityListener implements Listener {

    @EventHandler
    public void onLingeringPotionSplash(LingeringPotionSplashEvent event) {

        // cancel if plugin functionalities are disabled in the config
        if (!CONFIG.isPluginEnabled()) { return; }

        // The potion that triggered the event
        ThrownPotion potionEntity = event.getEntity();
        ItemStack potionItem = potionEntity.getItem();
        
        // The world in which the potion was thrown
        World world = potionEntity.getLocation().getWorld();

        // The areaEffectCloud that was spawned by splashing the potion
        AreaEffectCloud areaEffectCloud = event.getAreaEffectCloud();

        if (potionItem == null || !potionItem.hasItemMeta()) { return; }

        PersistentDataContainer pdc = potionItem.getItemMeta().getPersistentDataContainer();
        NamespacedKey key = new NamespacedKey(CaptureBiomes.INSTANCE, "capturebiomes.biomepotion");

        // Cancel if not biome potion (key capturebiomes.biomepotion)
        if (!pdc.has(key, PersistentDataType.INTEGER)) { return; }

        // the tier (level) of the potion (1 - 4)
        final Integer tier = pdc.get(key, PersistentDataType.INTEGER);
        if (tier == null) { return; }

        // Cancel and delete areaEffectCloud if not in overworld
        if (world == null || world.getEnvironment() != World.Environment.NORMAL) {

            log("Creation of biome at " + (world != null ? world.getName() : "unknown") + " failed: the biome / dimension is either not supported or could not be found.", ConsoleUtils.LogType.WARNING);
            logCreateIssueMessage();

            areaEffectCloud.remove();

            return;

        }

        // Get the biome from pdt key "capturebiomes.biomepotion.biome"
        var biomeKey = pdc.get(new NamespacedKey(CaptureBiomes.INSTANCE, "capturebiomes.biomepotion.biome"), PersistentDataType.STRING);
        if (biomeKey == null) {

            areaEffectCloud.remove();
            return;

        }

        NamespacedKey namespacedKey = NamespacedKey.fromString(biomeKey);
        if (namespacedKey == null) {

            areaEffectCloud.remove();
            return;

        }

        var biomes = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);
        var biome = biomes.get(namespacedKey);
        if (biome == null) {

            areaEffectCloud.remove();
            return;

        }

        areaEffectCloud.setRadius(0);

        final int maxHeight = world.getMaxHeight();
        // next block above the location where the potion was thrown
        final int nextBlockY = BlockUtils.getNextSolidBlockY(potionEntity.getLocation());
        final int size = switch (tier) {
            case 2 -> CONFIG.getBiomePotionSize()[1];
            case 3 -> CONFIG.getBiomePotionSize()[2];
            case 4 -> CONFIG.getBiomePotionSize()[3];
            default -> CONFIG.getBiomePotionSize()[0];
        };

        final BoundingBox boundingBox = BlockUtils.getBoundingBox(potionEntity.getLocation(),
                size,
                Math.min(nextBlockY + (CONFIG.getBiomePotionYOffsetMultiplier() * size), maxHeight));

        Color particleColor = BiomeUtils.getBiomeColor(biomeKey);
        if (particleColor == null && potionEntity.getPotionMeta() != null && potionEntity.getPotionMeta().getColor() != null) {

            particleColor = potionEntity.getPotionMeta().getColor();

        }

        if (particleColor == null) {

            particleColor = Color.WHITE;

        }

        // particle effect up to max world height or next block on y coordinate above the block
        ParticleFactory.createSquareRisingEdges(potionEntity.getLocation(),
                particleColor,
                size,
                Math.min(nextBlockY + (CONFIG.getBiomePotionYOffsetMultiplier() * size), maxHeight));

        // get a bounding box representing the particle box and fill biome
        BiomeUtils.fillBiome(world, boundingBox, biome);

        // Refresh affected chunks for players to see the biome change instantly
        BlockUtils.refreshChunksFromBoundingBox(boundingBox, world);

        log("A biome of type " + biome.getKey().getKey() + " with size " + size + " x " + size + " was created at center " + potionEntity.getLocation(), ConsoleUtils.LogType.ADDITIONAL_INFO);

    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent event) {

        Player player = event.getPlayer();
        if (player.isOp() && CaptureBiomes.newVersionAvailable) {
            UpdateService.sendUpdateMessageToPlayer(player);
        }

    }

}
