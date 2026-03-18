package io.github.mxiwbr.capturebiomes;

import io.github.mxiwbr.capturebiomes.listener.EntityListener;
import io.github.mxiwbr.capturebiomes.listener.ItemListener;
import io.github.mxiwbr.capturebiomes.services.UpdateService;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logConsole;

public final class CaptureBiomes extends JavaPlugin {

    // Console logger
    public static Logger LOGGER;
    public static CaptureBiomes INSTANCE;
    public static Config CONFIG;

    // Called when the plugin is enabled
    @Override
    public void onEnable() {

        // Global plugin instance object
        INSTANCE = this;

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
        logConsole("Enabled!", ConsoleUtils.logType.INFO);

        Boolean newVersionAvailable = UpdateService.checkForUpdates();

        // Register ItemListener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        // Register EntityListener
        getServer().getPluginManager().registerEvents(new EntityListener(), this);
    }

}
