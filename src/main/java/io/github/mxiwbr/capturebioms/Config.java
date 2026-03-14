package io.github.mxiwbr.capturebioms;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class Config {

    // Required items per tier
    private int[] requiredItemCount;
    // Amount of biome potions to get per tier
    private int[] biomePotionSize;
    private Material triggerItem;
    private int biomePotionsAmount;
    private boolean enablePotionCooldown;
    private int potionCooldown;
    private int timeoutTicks;
    private int intervalTicks;

    /**
     * Contains all configuration settings as private variables with getters
     */
    public Config() {

        final FileConfiguration config = CaptureBioms.INSTANCE.getConfig();

        try {

            // Required items per tier
            this.requiredItemCount = new int[] { config.getInt("beacon.required-xp-bottles.tier-1"),
                    config.getInt("beacon.required-xp-bottles.tier-2"),
                    config.getInt("beacon.required-xp-bottles.tier-3"),
                    config.getInt("beacon.required-xp-bottles.tier-4") };
            // Amount of biome potions to get per tier
            this.biomePotionSize = new int[] { config.getInt("beacon.biome-potions-size.tier-1"),
                    config.getInt("beacon.biome-potions-size.tier-2"),
                    config.getInt("beacon.biome-potions-size.tier-3"),
                    config.getInt("beacon.biome-potions-size.tier-4") };
            this.triggerItem = Material.valueOf(config.getString("beacon.trigger_item"));
            this.biomePotionsAmount = config.getInt("beacon.biome-potions-amount");
            this.enablePotionCooldown = config.getBoolean("potion-cooldown.enabled");
            this.potionCooldown = config.getInt("potion-cooldown.length");
            this.timeoutTicks = config.getInt("item-check.timeout-ticks");
            this.intervalTicks = config.getInt("item-check.interval-ticks");

            // Use default config if amount of required items is more than allowed (max 64)
            for (int itemCount : this.requiredItemCount) { if (itemCount > 64) { throw new Exception(); } }

        // Set to defaults if config couldn't be loaded
        } catch (Exception e) {

            CaptureBioms.LOGGER.warning("Failed to load config.yml, using default config.");

            // Required items per tier
            this.requiredItemCount = new int[] { 16, 32, 48, 64 };
            // Amount of biome potions to get per tier
            this.biomePotionSize = new int[] { 1, 2, 3, 4 };
            this.triggerItem = Material.EXPERIENCE_BOTTLE;
            this.biomePotionsAmount = 1;
            this.enablePotionCooldown = true;
            this.potionCooldown = 30;
            this.timeoutTicks = 200;
            this.intervalTicks = 2;

        }

    }

}
