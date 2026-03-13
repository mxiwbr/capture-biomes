package io.github.mxiwbr.capturebioms.listener;

import io.github.mxiwbr.capturebioms.BeaconRitual;
import io.github.mxiwbr.capturebioms.CaptureBioms;
import io.github.mxiwbr.capturebioms.utils.ItemUtils;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Beacon;
import org.bukkit.block.Block;
import org.bukkit.entity.Item;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ItemSpawnEvent;

/**
 * Listener Class for Item Events
 */
public class ItemListener implements Listener {

    /**
     * Called when an item entity is created
     * @param event
     */
    @EventHandler
    public void onItemSpawn(ItemSpawnEvent event) {

        // The item that triggered this event
        Item item = event.getEntity();

        // Cancel if the item is not an experience bottle
        if (item.getItemStack().getType() != Material.EXPERIENCE_BOTTLE) { return; }

        ItemUtils.checkItemOnGround(item, 200, 2, () -> {

            // The item's location
            Location location = item.getLocation();

            // The block on which the item is placed
            Block blockBelow = location.getBlock().getRelative(0, -1, 0);

            // Ensure the block below is a working Beacon
            if (blockBelow.getType() != Material.BEACON) { return; }
            if (!(blockBelow.getState() instanceof Beacon beacon) || beacon.getTier() == 0) { return; }

            // Get the beacon tier (1 - 4) to determine how much biome bottles the player should get (1 - 4)
            int tier  = beacon.getTier();

            // Required amount of xp-bottles per tier
            int requiredBottleAmount = switch (tier) {
                case 1 -> 16;
                case 2 -> 32;
                case 3 -> 48;
                default -> 64;
            };

            // Get amount of items on the beacon
            int itemAmount = item.getItemStack().getAmount();

            // Cancel if there are not enough xp-bottles
            if (itemAmount < requiredBottleAmount) { return; }

            BeaconRitual.startBeaconRitual(location, item, requiredBottleAmount, tier);

        });
    }

}
