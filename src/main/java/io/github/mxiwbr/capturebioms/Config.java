package io.github.mxiwbr.capturebioms;

import lombok.Getter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

@Getter
public class Config {

    // Required items per tier
    private int[] requiredItemCount;
    // Amount of biome potions to get per tier
    private int[] biomePotionCount;
    private Material triggerItem;
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
            this.requiredItemCount = new int[] {config.getInt("beacon.required-xp-bottles.tier-1"),
                    config.getInt("beacon.required-xp-bottles.tier-2"),
                    config.getInt("beacon.required-xp-bottles.tier-3"),
                    config.getInt("beacon.required-xp-bottles.tier-4")};

            // Amount of biome potions to get per tier
            this.biomePotionCount = new int[] {config.getInt("beacon.biome-bottles-per-tier.tier-1"),
                    config.getInt("beacon.biome-bottles-per-tier.tier-2"),
                    config.getInt("beacon.biome-bottles-per-tier.tier-3"),
                    config.getInt("beacon.biome-bottles-per-tier.tier-4")};

            this.triggerItem = Material.valueOf(config.getString("beacon.trigger_item"));

            this.enablePotionCooldown = config.getBoolean("potion-cooldown.enabled");

            this.potionCooldown = config.getInt("potion-cooldown.length");

            this.timeoutTicks = config.getInt("item-check.timeout-ticks");

            this.intervalTicks = config.getInt("item-check.interval-ticks");

        } catch (Exception e) {

            CaptureBioms.LOGGER.warning("Failed to load config.yml, using default config.");

            // Required items per tier
            this.requiredItemCount = new int[] { 16, 32, 48, 64 };

            // Amount of biome potions to get per tier
            this.biomePotionCount = new int[] { 1, 2, 3, 4 };

            this.triggerItem = Material.EXPERIENCE_BOTTLE;

            this.timeoutTicks = 200;

            this.intervalTicks = 2;

        }

    }

}
