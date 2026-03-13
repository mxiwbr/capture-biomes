package io.github.mxiwbr.capturebioms.utils;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;

public class ItemUtils {

    /**
     * Counts all items of a type on a specific location
     * @param location item location
     * @param material material to search for
     * @return number of items
     */
    public int countItems(Location location, Material material) {

        int amount = 0;
        for (Entity entity : location.getWorld().getNearbyEntities(location, 1, 1, 1)) {

            if (entity instanceof Item item) {

                ItemStack itemStack = item.getItemStack();
                if (itemStack.getType() == material) {

                    amount  += itemStack.getAmount();

                }

            }

        }

        return amount;

    }

}
