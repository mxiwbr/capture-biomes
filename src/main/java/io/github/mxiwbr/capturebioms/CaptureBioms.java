package io.github.mxiwbr.capturebioms;

import io.github.mxiwbr.capturebioms.listener.ItemListener;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

public final class CaptureBioms extends JavaPlugin {

    // Console logger
    public static Logger LOGGER;
    public static CaptureBioms INSTANCE;

    // Called when the plugin is enabled
    @Override
    public void onEnable() {

        // Global plugin instance object
        INSTANCE = this;
        // Set logger object to log from other classes
        LOGGER = getLogger();
        LOGGER.info("Enabled!");
        // Register XpBottleListener() event
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
    }

}
