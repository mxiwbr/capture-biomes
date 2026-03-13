package io.github.mxiwbr.capturebioms.utils;

import io.github.mxiwbr.capturebioms.CaptureBioms;
import org.bukkit.Bukkit;
import org.bukkit.entity.Item;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;

public class ItemUtils {

    /**
     * Removes a given amount of items from a stack
     * @param item
     * @param amount
     */
    public static void removeItemsFromStack(Item item, int amount) {

        // get stack of given item
        ItemStack itemStack = item.getItemStack();

        if (itemStack.getAmount() > amount) {

            itemStack.setAmount(itemStack.getAmount() - amount);
        }
        else if (itemStack.getAmount() == amount) {

            item.remove();
        }

    }

    // Callback for when the item is valid and on ground
    public interface ItemGroundCallback {
        void onItemGround();
    }

    /**
     * Checks every [checkInterval] ticks if an item is on the ground.
     * Timeout after [timeoutTicks] ticks.
     * @param item
     * @param timeoutTicks how long the method should run (check) in ticks
     * @param checkInterval
     * @param callback
     */
    public static void checkItemOnGround(Item item, int timeoutTicks, int checkInterval, ItemGroundCallback callback) {

        // A new Bukkit Task (BukkitRunnable)
        new BukkitRunnable() {

            // timer in ticks
            int elapsed = 0;

            @Override
            public void run() {

                // if the item is not there anymore or the elapsed time is more than it should be, cancel the task and return
                if (!item.isValid() || elapsed >= timeoutTicks) {

                    this.cancel();

                    return;
                }
                // if the item is now on the ground, cancel the task and call the callback
                else if (item.isOnGround()) {

                    this.cancel();
                    callback.onItemGround();

                    return;
                }

                // increase the timer
                elapsed += checkInterval;
            }

            // run Task (directly after 0 Ticks, loop every [checkInterval] ticks
        }.runTaskTimer(CaptureBioms.INSTANCE, 0L, checkInterval);
    }

}
