package io.github.mxiwbr.capturebioms;

import io.github.mxiwbr.capturebioms.listener.XpBottleListener;
import org.bukkit.plugin.java.JavaPlugin;

public final class CaptureBioms extends JavaPlugin {

    @Override
    public void onEnable() {

        getLogger().info("Enabled!");
        // Register XpBottleListener() event
        getServer().getPluginManager().registerEvents(new XpBottleListener(), this);
    }

}
