package io.github.mxiwbr.capturebiomes.registries;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.Player;
import io.github.mxiwbr.capturebiomes.commands.*;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;

public class CommandRegistry {

    /**
     * Registers all commands by the plugin
     */
    public static void registerCommands() {

        CaptureBiomes.INSTANCE.getLifecycleManager().registerEventHandler(LifecycleEvents.COMMANDS, event -> {

            // Root Command /capturebiomes
            LiteralArgumentBuilder<CommandSourceStack> rootCommand = Commands.literal("capturebiomes")
                    // require operator permission
                    .requires(src -> src.getSender() instanceof Player player && player.isOp());

            rootCommand.then(Commands.literal("disable")
                    .executes(ctx -> {

                        CommandActions.commandDisable((Player) ctx.getSource().getSender());
                        return 1;

                    }));

            rootCommand.then(Commands.literal("enable")
                    .executes(ctx -> {

                        CommandActions.commandEnable((Player) ctx.getSource().getSender());
                        return 1;

                    }));

            rootCommand.then(Commands.literal("help")
                    .executes(ctx -> {

                        Player player = (Player) ctx.getSource().getSender();
                        CommandActions.commandHelp(player);
                        return 1;

                    }));

            // Reloads the plugin's config
            rootCommand.then(Commands.literal("reload")
                    .executes(ctx -> {

                        Player player = (Player) ctx.getSource().getSender();
                        CommandActions.commandReload(player);
                        return 1;

                    }));

            // Gives the executing player a biome potion: /capturebiomes givebiomepotion
            rootCommand.then(Commands.literal("givebiomepotion")
                    .then(Commands.argument("biome", word())
                            .then(Commands.argument("tier", integer(1, 4))
                                .executes(ctx -> {

                                    Player player = (Player) ctx.getSource().getSender();

                                    // check if biome is supported by the plugin
                                    String biomeName = StringArgumentType.getString(ctx, "biome");
                                    if (BiomeUtils.getBiomeColor(biomeName) == null) {
                                        player.sendMessage(Component.text("Biome '" + biomeName + "' invalid or not supported!", NamedTextColor.RED));
                                        return 0;
                                    }

                                    CommandActions.commandGivebiomepotion(RegistryAccess
                                            .registryAccess()
                                            .getRegistry(RegistryKey.BIOME)
                                            .get(NamespacedKey.fromString(biomeName)), IntegerArgumentType.getInteger(ctx, "tier"), player);

                                    return 1;

                                })

                            )

                    )

            );

            rootCommand.then(Commands.literal("reset")
                    .executes(ctx -> {

                        CommandActions.commandResetConfig((Player) ctx.getSource().getSender(), false);
                        return 1;
                    })

                    .then(Commands.literal("confirm")
                            .executes(ctx -> {

                                CommandActions.commandResetConfig((Player) ctx.getSource().getSender(), true);
                                return 1;
                            })
                    )
            );

            LiteralArgumentBuilder<CommandSourceStack> biomeCommand = Commands.literal("biome")
                    .executes(ctx -> {

                        if (!(ctx.getSource().getSender() instanceof Player player)) {
                            return 0;
                        }

                        CommandActions.commandBiome(player);
                        return 1;

                    });

            // register commands
            event.registrar().register(biomeCommand.build());
            event.registrar().register(rootCommand.build());

        });

    }

}
