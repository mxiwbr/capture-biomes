package io.github.mxiwbr.capturebiomes.config;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.exceptions.ConfigLoadingException;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

@Getter
public class Config {

    @Setter
    // enabled status
    private boolean pluginEnabled;
    // Required items per tier
    private int[] requiredItemCount;
    // Amount of biome potions to get per tier
    private int[] biomePotionSize;
    private Material triggerItem;
    private int biomePotionsAmount;
    private boolean enablePotionCooldown;
    private int potionCooldown;
    private boolean enableConsoleLogging;
    private boolean enableAdditionalConsoleLogging;
    private int timeoutTicks;
    private int intervalTicks;
    private boolean bstatsEnabled;

    /**
     * Contains all configuration settings as private variables with getters
     */
    public Config() {

        final FileConfiguration config = CaptureBiomes.INSTANCE.getConfig();

        try {

            // enabled status
            this.pluginEnabled = config.getBoolean("enabled", true);

            this.triggerItem = Material.valueOf(config.getString("beacon.trigger_item", "EXPERIENCE_BOTTLE"));
            this.biomePotionsAmount = config.getInt("beacon.biome-potions-amount", 1);

            this.enablePotionCooldown = config.getBoolean("potion-cooldown.enabled", true);
            this.potionCooldown = config.getInt("potion-cooldown.length", 15);

            this.enableConsoleLogging = config.getBoolean("console.enable-logging", true);
            this.enableAdditionalConsoleLogging = config.getBoolean("console.enable-additional-logging", false);

            this.timeoutTicks = config.getInt("item-check.timeout-ticks", 200);
            this.intervalTicks = config.getInt("item-check.interval-ticks", 2);

            this.bstatsEnabled = config.getBoolean("bstats.enabled", true);

            // Required items per tier
            this.requiredItemCount = new int[] {config.getInt("beacon.required-xp-bottles.tier-1", 16),
                    config.getInt("beacon.required-xp-bottles.tier-2", 32),
                    config.getInt("beacon.required-xp-bottles.tier-3", 48),
                    config.getInt("beacon.required-xp-bottles.tier-4", 64)};
            // Use default config if amount of required items is more than allowed (max 64)
            for (int itemCount : this.requiredItemCount) {
                if (itemCount > 64) {
                    throw new ConfigLoadingException("Error when loading an item of beacon.required-xp-bottles (value: " + itemCount + "). Values must not be more than 64.");
                }
            }

            // Amount of biome potions to get per tier
            this.biomePotionSize = new int[]  {config.getInt("beacon.biome-potions-size.tier-1", 4),
                    config.getInt("beacon.biome-potions-size.tier-2", 8),
                    config.getInt("beacon.biome-potions-size.tier-3", 16),
                    config.getInt("beacon.biome-potions-size.tier-4", 32)};
            // Use default config if odd numbers occur
            for (int number : this.biomePotionSize) {
                if (number % 2 != 0) {
                    throw new ConfigLoadingException("Error when loading an item of beacon.biome-potions-size (value: " + number + "). Values must not be odd numbers.");
                }
            }

        // Set to defaults if config couldn't be loaded
        } catch (Exception e) {

            log("Failed to load config.yml, using default config: " + e.getMessage(), ConsoleUtils.LogType.WARNING);
            log("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues", ConsoleUtils.LogType.WARNING);

            // Required items per tier
            this.requiredItemCount = new int[] { 16, 32, 48, 64 };
            // Amount of biome potions to get per tier
            this.biomePotionSize = new int[] { 4, 8, 16, 32 };

        }

    }

}
