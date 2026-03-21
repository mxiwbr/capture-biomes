package io.github.mxiwbr.capturebiomes.commands;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.config.Config;
import io.github.mxiwbr.capturebiomes.factories.ItemFactory;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

public class CaptureBiomesCommands {

    /**
     * Action of the /capturebiomes givebiomepotion command: Gives the executing player a biome potion
     * @param biome
     * @param tier
     * @param player
     */
    public static void commandGivebiomepotion(Biome biome, int tier, Player player) {

        try {
            player.getInventory().addItem(ItemFactory.createBiomePotion(biome, tier));
        }
        catch (Exception e) {

            log("Failed to give biome potion to " + player.getName() + ": " + e.getMessage(), ConsoleUtils.LogType.SEVERE);
            player.sendMessage(Component.text("Oops! Something went wrong and your biome potion could not be delivered.", NamedTextColor.RED));

        }

    }

    /**
     * Actions of the /capturebiomes help command
     * @param player
     */
    public static void commandHelp(Player player) {

        player.sendMessage(Component.text("=== Capture Biomes Commands ===", NamedTextColor.DARK_AQUA, TextDecoration.BOLD));
        player.sendMessage(Component.text(""));
        player.sendMessage(Component.text("/capturebiomes disable", NamedTextColor.RED)
                .append(Component.text(" - Disables the plugin", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/capturebiomes enable", NamedTextColor.GREEN)
                .append(Component.text(" - Enables the plugin", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/capturebiomes givebiomepotion <biome> <tier>", NamedTextColor.GOLD)
                .append(Component.text(" - Gives a biome potion", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/capturebiomes help", NamedTextColor.GRAY)
                .append(Component.text(" - Writes this help page in the chat", NamedTextColor.WHITE)));
        player.sendMessage(Component.text("/capturebiomes reload", NamedTextColor.BLUE)
                .append(Component.text(" - Reloads the plugin's config", NamedTextColor.WHITE)));

    }

    /**
     * Actions of the /capturebiomes enable command: Enables the plugin
     */
    public static void commandEnable(Player player) {

        CaptureBiomes.CONFIG.setPluginEnabled(true);
        CaptureBiomes.INSTANCE.getConfig().set("enabled", true);
        CaptureBiomes.INSTANCE.saveConfig();

        player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                .append(Component.text("The plugin was enabled.", NamedTextColor.GREEN)
                        .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

        log("The plugin was enabled by " + player.getName(), ConsoleUtils.LogType.ADDITIONAL_INFO);

    }

    /**
     * Actions of the /capturebiomes disable command: Disables the plugin
     */
    public static void commandDisable(Player player) {

        CaptureBiomes.CONFIG.setPluginEnabled(false);
        CaptureBiomes.INSTANCE.getConfig().set("enabled", false);
        CaptureBiomes.INSTANCE.saveConfig();

        player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                .append(Component.text("The plugin was disabled.", NamedTextColor.RED)
                        .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

        log("The plugin was disabled by " + player.getName(), ConsoleUtils.LogType.ADDITIONAL_INFO);

    }

    /**
     * Reload the config: /capturebiomes reload
     * @param player
     */
    public static void commandReload(Player player) {

        player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                .append(Component.text("Reloading config...", NamedTextColor.GREEN)
                        .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

        log("Reloading config...", ConsoleUtils.LogType.ADDITIONAL_INFO);

        CaptureBiomes.INSTANCE.reloadConfig();
        CaptureBiomes.CONFIG = new Config();

        if (CaptureBiomes.CONFIG.isLoadFailed()) {

            player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                    .append(Component.text("Reload of config failed! Please check the server log for more information.", NamedTextColor.RED)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

        }
        else {

            player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                    .append(Component.text("Successfully reloaded the config!", NamedTextColor.GREEN)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));
            log("Successfully reloaded the config!", ConsoleUtils.LogType.ADDITIONAL_INFO);

        }

    }

}
