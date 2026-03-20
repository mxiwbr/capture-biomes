package io.github.mxiwbr.capturebiomes;

import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.mxiwbr.capturebiomes.listener.EntityListener;
import io.github.mxiwbr.capturebiomes.listener.ItemListener;
import io.github.mxiwbr.capturebiomes.services.UpdateService;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import lombok.Getter;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.logging.Logger;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logConsole;

public final class CaptureBiomes extends JavaPlugin {

    // Console logger
    public static Logger LOGGER;
    public static CaptureBiomes INSTANCE;
    public static Config CONFIG;
    public static Boolean newVersionAvailable;

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

        newVersionAvailable = UpdateService.checkForUpdates();

        // Register ItemListener
        getServer().getPluginManager().registerEvents(new ItemListener(), this);
        // Register EntityListener
        getServer().getPluginManager().registerEvents(new EntityListener(), this);

        this.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {

            LiteralArgumentBuilder<CommandSourceStack> rootCommand = Commands.literal("capturebiomes");

            rootCommand.then(Commands.literal("disable")
                    .executes(ctx -> {
                        
                        return 1;

                    }));

            rootCommand.then(Commands.literal("enable")
                    .executes(ctx -> {
                        
                        return 1;

                    }));

            rootCommand.then(Commands.literal("help")
                    .executes(ctx -> {
                        
                        return 1;

                    }));

            rootCommand.then(Commands.literal("settings")
                    .executes(ctx -> {
                        
                        return 1;

                    }));
            
            rootCommand.then(Commands.literal("givebiomepotion")
                    .executes(ctx -> {
                        
                        return 1;

                    }));

            event.registrar().register(rootCommand.build());

        });
    }

}
