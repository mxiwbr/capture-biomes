package io.github.mxiwbr.capturebiomes;

import io.github.mxiwbr.capturebiomes.config.Config;
import io.github.mxiwbr.capturebiomes.listener.EntityListener;
import io.github.mxiwbr.capturebiomes.listener.ItemListener;
import io.github.mxiwbr.capturebiomes.registries.CommandRegistry;
import io.github.mxiwbr.capturebiomes.services.UpdateService;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import org.bstats.bukkit.Metrics;
import org.bstats.charts.AdvancedPie;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

public final class CaptureBiomes extends JavaPlugin {

    // Console logger
    public static Logger LOGGER;
    public static CaptureBiomes INSTANCE;
    public static Config CONFIG;
    public static boolean newVersionAvailable = false;
    private final Map<String, Integer> capturedBiomeCounts = new ConcurrentHashMap<>();

    public void recordBiomeCapture(String biomeName) {
        capturedBiomeCounts.merge(biomeName, 1, Integer::sum);
    }

    // Called when the plugin is enabled
    @Override
    public void onEnable() {

        // Global plugin instance object
        INSTANCE = this;

        this.getLogger().info("Loading config.yml...");
        // creates a default config.yml if there is none
        this.saveDefaultConfig();
        // Loads config defaults from plugin resource
        getConfig().options().copyDefaults(true);
        // writes missing config options
        saveConfig();
        // Creates a config object to get config values
        CONFIG = new Config();

        // Set logger object to log from other classes
        LOGGER = getLogger();
        log("Enabled!", ConsoleUtils.LogType.INFO);

        // bStats - only if enabled in config (default)
        if (CONFIG.isBstatsEnabled()) {

            try {

                final int bStatsPluginId = 30340;
                Metrics bStatsMetrics = new Metrics(this, bStatsPluginId);

                // custom bStats chart showing the captured biome distribution
                bStatsMetrics.addCustomChart(new AdvancedPie("bottled_biomes", () -> {

                    // snapshot of the current capture counts
                    Map<String, Integer> snapshot = new HashMap<>();

                    capturedBiomeCounts.forEach((biome, count) -> {

                        if (capturedBiomeCounts.remove(biome, count)) {

                            snapshot.put(biome, count);

                        }

                    });

                    return snapshot.isEmpty() ? null : snapshot;

                }));

            }
            catch (Exception e) {

                log("An error occurred while trying to establish bStats connection: " + e.getMessage(), ConsoleUtils.LogType.WARNING);

            }
        }

        newVersionAvailable = UpdateService.checkForUpdates();

        // Register ItemListener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        // Register EntityListener
        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        CommandRegistry.registerCommands();

    }
}
