package io.github.mxiwbr.capturebiomes.config;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.exceptions.ConfigLoadingException;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;

import static io.github.mxiwbr.capturebiomes.CaptureBiomes.CONFIG;
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
    private boolean enableMushroomFieldsBiome;
    private boolean enableDeepDarkBiome;
    private boolean enableConsoleLogging;
    private boolean enableAdditionalConsoleLogging;
    private int timeoutTicks;
    private int intervalTicks;
    private boolean bstatsEnabled;

    // Is set to true if the config couldn't be loaded and the default values where used
    private boolean loadFailed;

    /**
     * Contains all configuration settings as private variables with getters. The constructor gets all values from the config.yml file or uses defaults, if none could be found.
     */
    public Config() {

        final FileConfiguration config = CaptureBiomes.INSTANCE.getConfig();

        try {

            // enabled status
            this.pluginEnabled = config.getBoolean("enabled");

            try {

                this.triggerItem = Material.valueOf(config.getString("beacon.trigger_item"));

            } catch (IllegalArgumentException e) {

                throw new  ConfigLoadingException("Invalid trigger item: " + config.getString("beacon.trigger_item"));
            }

            // Required items per tier
            this.requiredItemCount = new int[] {config.getInt("beacon.required-item-count.tier-1"),
                    config.getInt("beacon.required-item-count.tier-2"),
                    config.getInt("beacon.required-item-count.tier-3"),
                    config.getInt("beacon.required-item-count.tier-4")};
            // Use default config if amount of required items is more than allowed (max 64)
            for (int itemCount : this.requiredItemCount) {
                if (itemCount > triggerItem.getMaxStackSize() || itemCount < 1) {
                    throw new ConfigLoadingException("Error when loading an item of beacon.required-item-count (value: " + itemCount + "). Values must not be more than " + triggerItem.getMaxStackSize() + " or less than 1.");
                }

            }

            // Size of biome potions per tier
            this.biomePotionSize = new int[]  {config.getInt("beacon.biome-potions-size.tier-1"),
                    config.getInt("beacon.biome-potions-size.tier-2"),
                    config.getInt("beacon.biome-potions-size.tier-3"),
                    config.getInt("beacon.biome-potions-size.tier-4")};
            // Use default config if odd numbers occur
            for (int number : this.biomePotionSize) {
                if (number % 2 != 0) {
                    throw new ConfigLoadingException("Error when loading an item of beacon.biome-potions-size (value: " + number + "). Values must not be odd numbers.");
                }
                else if (number < 1) {
                    throw new ConfigLoadingException("Error when loading an item of beacon.biome-potions-size (value: " + number + "). Values must not be less than 1.");
                }
            }

            this.biomePotionsAmount = config.getInt("beacon.biome-potions-amount");
            if (biomePotionsAmount < 1) {
                throw new ConfigLoadingException("Error when loading beacon.biome-potions-amount. Value must be greater than 0.");
            }

            if (!config.isBoolean("potion-cooldown.enabled")) {
                throw new ConfigLoadingException("Error when loading potion-cooldown.enabled. Has to be either true or false.");
            }
            this.enablePotionCooldown = config.getBoolean("potion-cooldown.enabled");

            this.potionCooldown = config.getInt("potion-cooldown.length");
            if (potionCooldown < 1) {
                throw new ConfigLoadingException("Error when loading potion-cooldown.length. Value must be greater than 0. To disable it completely, please set potion-cooldown.enabled to false.");
            }

            if (!config.isBoolean("biomes.enable-mushroom_fields")) {
                throw new ConfigLoadingException("Error when loading biomes.enable-mushroom_fields. Has to be either true or false.");
            }
            this.enableMushroomFieldsBiome = config.getBoolean("biomes.enable-mushroom_fields");

            if (!config.isBoolean("biomes.enable-deep_dark")) {
                throw new ConfigLoadingException("Error when loading biomes.enable-deep_dark. Has to be either true or false.");
            }
            this.enableDeepDarkBiome = config.getBoolean("biomes.enable-deep_dark");

            if (!config.isBoolean("console.enable-logging")) {
                throw new ConfigLoadingException("Error when loading console.enable-logging. Has to be either true or false.");
            }
            this.enableConsoleLogging = config.getBoolean("console.enable-logging");

            if (!config.isBoolean("console.enable-additional-logging")) {
                throw new ConfigLoadingException("Error when loading console.enable-additional-logging. Has to be either true or false.");
            }
            this.enableAdditionalConsoleLogging = config.getBoolean("console.enable-additional-logging");

            this.timeoutTicks = config.getInt("item-check.timeout-ticks");
            if (timeoutTicks < 1) {
                throw new ConfigLoadingException("Error when loading item-check.timeout-ticks. Value must be greater than 0.");
            }

            this.intervalTicks = config.getInt("item-check.interval-ticks");
            if (intervalTicks < 1) {
                throw new ConfigLoadingException("Error when loading item-check.interval-ticks. Value must be greater than 0.");
            }

            if (!config.isBoolean("bstats.enabled")) {
                throw new ConfigLoadingException("Error when loading bstats.enabled. Has to be either true or false.");
            }
            this.bstatsEnabled = config.getBoolean("bstats.enabled");

            this.loadFailed = false;

        // Set to defaults if config couldn't be loaded
        } catch (Exception e) {

            CaptureBiomes.INSTANCE.getLogger().severe("Failed to load config.yml, using default config: " + e.getMessage());
            CaptureBiomes.INSTANCE.getLogger().severe("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-bioms/issues");

            this.pluginEnabled = true;
            // Required items per tier
            this.requiredItemCount = new int[] { 16, 32, 48, 64 };
            // Amount of biome potions to get per tier
            this.biomePotionSize = new int[] { 4, 8, 16, 32 };
            this.triggerItem = Material.EXPERIENCE_BOTTLE;
            this.biomePotionsAmount = 1;
            this.enablePotionCooldown = true;
            this.potionCooldown = 15;
            this.enableMushroomFieldsBiome = false;
            this.enableDeepDarkBiome = false;
            this.enableConsoleLogging = true;
            this.enableAdditionalConsoleLogging = false;
            this.timeoutTicks = 200;
            this.intervalTicks = 2;
            this.bstatsEnabled = true;

            this.loadFailed = true;

        }

    }

    /**
     * Resets the config.yml to default and automatically applies the new values
     */
    public static void resetConfigFile() {

        CaptureBiomes.CONFIG.pluginEnabled = true;
        // Required items per tier
        CaptureBiomes.CONFIG.requiredItemCount = new int[] { 16, 32, 48, 64 };
        // Size of biome potions per tier
        CaptureBiomes.CONFIG.biomePotionSize = new int[] { 4, 8, 16, 32 };
        CaptureBiomes.CONFIG.triggerItem = Material.EXPERIENCE_BOTTLE;
        CaptureBiomes.CONFIG.biomePotionsAmount = 1;
        CaptureBiomes.CONFIG.enablePotionCooldown = true;
        CaptureBiomes.CONFIG.potionCooldown = 15;
        CaptureBiomes.CONFIG.enableMushroomFieldsBiome = false;
        CaptureBiomes.CONFIG.enableDeepDarkBiome = false;
        CaptureBiomes.CONFIG.enableConsoleLogging = true;
        CaptureBiomes.CONFIG.enableAdditionalConsoleLogging = false;
        CaptureBiomes.CONFIG.timeoutTicks = 200;
        CaptureBiomes.CONFIG.intervalTicks = 2;
        CaptureBiomes.CONFIG.bstatsEnabled = true;
        CaptureBiomes.CONFIG.loadFailed = false;

        CaptureBiomes.INSTANCE.getConfig().set("enabled", true);
        // Required items per tier
        CaptureBiomes.INSTANCE.getConfig().set("beacon.required-item-count.tier-1", 16);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.required-item-count.tier-2", 32);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.required-item-count.tier-3", 48);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.required-item-count.tier-4", 64);
        // Size of biome potions per tier
        CaptureBiomes.INSTANCE.getConfig().set("beacon.biome-potions-size.tier-1", 4);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.biome-potions-size.tier-2", 8);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.biome-potions-size.tier-3", 16);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.biome-potions-size.tier-4", 32);
        CaptureBiomes.INSTANCE.getConfig().set("beacon.trigger_item", "EXPERIENCE_BOTTLE");
        CaptureBiomes.INSTANCE.getConfig().set("beacon.biome-potions-amount", 1);
        CaptureBiomes.INSTANCE.getConfig().set("potion-cooldown.enabled", true);
        CaptureBiomes.INSTANCE.getConfig().set("potion-cooldown.length", 15);
        CaptureBiomes.INSTANCE.getConfig().set("biomes.enable-mushroom_fields", false);
        CaptureBiomes.INSTANCE.getConfig().set("biomes.enable-deep_dark", false);
        CaptureBiomes.INSTANCE.getConfig().set("console.enable-logging", true);
        CaptureBiomes.INSTANCE.getConfig().set("console.enable-additional-logging", false);
        CaptureBiomes.INSTANCE.getConfig().set("item-check.timeout-ticks", 200);
        CaptureBiomes.INSTANCE.getConfig().set("item-check.interval-ticks", 2);
        CaptureBiomes.INSTANCE.getConfig().set("bstats.enabled", true);

        CaptureBiomes.INSTANCE.saveConfig();

    }

}
