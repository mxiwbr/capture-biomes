package io.github.mxiwbr.capturebiomes.registries;

import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import io.github.mxiwbr.capturebiomes.utils.BiomeUtils;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import io.papermc.paper.command.brigadier.CommandSourceStack;
import io.papermc.paper.command.brigadier.Commands;
import io.papermc.paper.plugin.lifecycle.event.types.LifecycleEvents;
import io.papermc.paper.registry.RegistryAccess;
import io.papermc.paper.registry.RegistryKey;
import io.papermc.paper.registry.keys.BiomeKeys;
import io.papermc.paper.registry.keys.tags.BiomeTagKeys;
import io.papermc.paper.registry.tag.Tag;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.NamespacedKey;
import org.bukkit.Registry;
import org.bukkit.block.Biome;
import org.bukkit.entity.Player;
import io.github.mxiwbr.capturebiomes.commands.*;

import java.util.Locale;
import java.util.stream.Stream;

import static com.mojang.brigadier.arguments.IntegerArgumentType.integer;
import static com.mojang.brigadier.arguments.StringArgumentType.word;
import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;

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

            // Lets a user get the current plugin version and informs about updates
            rootCommand.then(Commands.literal("version")
                    .executes(ctx -> {

                        Player player = (Player) ctx.getSource().getSender();
                        CommandActions.commandVersion(player);
                        return 1;

                    }));

            // Updates the plugin and restarts the server (only if the user prompted the restart)
            rootCommand.then(Commands.literal("update")
                    .executes(ctx -> {

                        CommandActions.commandUpdatePlugin((Player) ctx.getSource().getSender(), false);
                        return 1;
                    })
                    .then(Commands.literal("restart")
                            .executes(ctx -> {

                                CommandActions.commandUpdatePlugin((Player) ctx.getSource().getSender(), true);
                                return 1;
                            })
                    )
            );

            // Reloads the plugin's config
            rootCommand.then(Commands.literal("reloadconfig")
                    .executes(ctx -> {

                        Player player = (Player) ctx.getSource().getSender();
                        CommandActions.commandReloadConfig(player);
                        return 1;

                    }));

            // Load supported minecraft biomes for autocomplete
            Registry<Biome> biomesReg = RegistryAccess.registryAccess().getRegistry(RegistryKey.BIOME);

            // Gives the executing player a biome potion: /capturebiomes givebiomepotion
            rootCommand.then(Commands.literal("givebiomepotion")
                    .then(Commands.argument("biome", word())
                            .suggests((context, builder) -> {
                                Tag<Biome> netherTag = biomesReg.getTag(BiomeTagKeys.IS_NETHER);
                                Tag<Biome> endTag = biomesReg.getTag(BiomeTagKeys.IS_END);

                                biomesReg.stream()
                                        .filter(biome -> !netherTag.contains(BiomeKeys.create(biome.getKey())))
                                        .filter(biome -> !endTag.contains(BiomeKeys.create(biome.getKey())))
                                        .map(biome -> biome.getKey().getKey())
                                        .filter(entry -> entry.toLowerCase(Locale.ROOT).startsWith(builder.getRemainingLowerCase()))
                                        .forEach(builder::suggest);

                                return builder.buildFuture();
                            })
                            .then(Commands.argument("tier", integer(1, 4))
                                .suggests((context, builder) -> {
                                    for (int i = 1; i <= 4; i++) {
                                        builder.suggest(i);
                                    }
                                    return builder.buildFuture();
                                })
                                .executes(ctx -> {

                                    Player player = (Player) ctx.getSource().getSender();

                                    // check if biome is supported by the plugin
                                    String biomeName = StringArgumentType.getString(ctx, "biome");
                                    if (BiomeUtils.getBiomeColor(biomeName) == null) {
                                        player.sendMessage(Component.text("Biome '" + biomeName + "' invalid or not supported!", NamedTextColor.RED));
                                        return 0;
                                    }

                                    NamespacedKey biomeNamespacedKey = NamespacedKey.fromString(biomeName);
                                    Biome biome = biomeNamespacedKey != null ? biomesReg.get(biomeNamespacedKey) : null;

                                    if (biome == null) {

                                        log("Failed to give biome potion to " + player.getName() + ": BiomeBiome '" + biomeName + "' either invalid or not supported", ConsoleUtils.LogType.ADDITIONAL_INFO);
                                        player.sendMessage(Component.text("[CaptureBiomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                                                .append(Component.text("Biome '" + biomeName + "' either invalid or not supported!", NamedTextColor.RED)
                                                        .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

                                        return 0;
                                    }

                                    CommandActions.commandGivebiomepotion(biome, IntegerArgumentType.getInteger(ctx, "tier"), player);

                                    return 1;

                                })

                            )

                    )

            );

            rootCommand.then(Commands.literal("resetconfig")
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
            event.registrar().register(rootCommand.build());
            event.registrar().register(biomeCommand.build());

        });

    }

}
