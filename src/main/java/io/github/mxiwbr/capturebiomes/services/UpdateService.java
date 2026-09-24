package io.github.mxiwbr.capturebiomes.services;

import com.google.gson.JsonArray;
import io.github.mxiwbr.capturebiomes.CaptureBiomes;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import io.github.mxiwbr.capturebiomes.exceptions.UpdateException;
import io.github.mxiwbr.capturebiomes.utils.ConsoleUtils;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.event.ClickEvent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import org.bukkit.entity.Player;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.concurrent.CompletableFuture;

import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.log;
import static io.github.mxiwbr.capturebiomes.utils.ConsoleUtils.logCreateIssueMessage;

public class UpdateService {

    private static String cachedLatestVersion;

    /**
     * Scans the GitHub page for new releases
     * @return true or false whether a new update is available
     */
    public static Boolean checkForUpdates() {

        final String pluginVersion = CaptureBiomes.INSTANCE.getPluginMeta().getVersion();

        log("Checking for updates...", ConsoleUtils.LogType.INFO);

        try {

            cachedLatestVersion = getLatestVersion().get("version_number").getAsString();

            // Check if new version is available and log it
            if (!pluginVersion.equals(cachedLatestVersion)) {

                log("A new plugin version is available: " + cachedLatestVersion + ", you're on: " + pluginVersion, ConsoleUtils.LogType.INFO);

                return true;
            }

            log("You're up to date!", ConsoleUtils.LogType.INFO);

        } catch (Exception e) {

            log("An error occurred while checking for updates:", ConsoleUtils.LogType.WARNING);
            log(e.getClass().getSimpleName() + " - " + e.getMessage(), ConsoleUtils.LogType.WARNING);
            if (CaptureBiomes.CONFIG.isEnableConsoleLogging()) {

                e.printStackTrace();

            }
            logCreateIssueMessage(ConsoleUtils.LogType.WARNING);
        }

        return false;

    }

    /**
     * Gets latest plugin version info from Modrinth (Modrinth API) and returns it as JsonObject
     * @throws IOException
     * @throws InterruptedException
     */
    public static JsonObject getLatestVersion() throws IOException, InterruptedException {

        HttpClient httpClient = HttpClient.newHttpClient();
        HttpRequest httpRequest = HttpRequest.newBuilder()
                .uri(URI.create("https://api.modrinth.com/v2/project/capture-biomes/version?featured=true&include_changelog=false"))
                .header("User-Agent", "CaptureBiomes " + CaptureBiomes.INSTANCE.getPluginMeta().getVersion())
                .build();
        HttpResponse<String> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofString());
        httpClient.close();

        if (httpResponse.statusCode() != 200) {

            throw new IOException("Modrinth API returned status code " + httpResponse.statusCode() + ".");

        }

        String jsonString = httpResponse.body();
        var jsonElement = JsonParser.parseString(jsonString);
        if (!jsonElement.isJsonArray()) {

            throw new IOException("Modrinth API response is not a JSON array.");

        }

        JsonArray jsonArray = jsonElement.getAsJsonArray();
        if (jsonArray.isEmpty()) {

            throw new IOException("Modrinth API returned an empty version array.");

        }

        return jsonArray.get(0).getAsJsonObject();

    }

    /**
     * Sends the update available message to a player in the in-game chat
     * @param player
     */
    public static void sendUpdateMessageToPlayer(Player player) {

        try {

            String version = cachedLatestVersion != null ? cachedLatestVersion : getLatestVersion().get("version_number").getAsString();

            player.sendMessage(Component.text("[CaptureBiomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                    .append(Component.text("There is a newer plugin version available: "
                                    + version
                                    + ", you're on: "
                                    + CaptureBiomes.INSTANCE.getPluginMeta().getVersion(), NamedTextColor.GREEN)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

        }
        catch (Exception e) {

            CaptureBiomes.LOGGER.severe(e.getMessage());

        }

    }

    public static void update(Player player, boolean restart) {

        try {

            final JsonObject latestVersion = getLatestVersion();
            final JsonObject latestVersionFile = latestVersion.getAsJsonArray("files").get(0).getAsJsonObject();
            final BigDecimal updateSizeMB = latestVersionFile.get("size").getAsBigDecimal().divide(BigDecimal.valueOf(1000000), 2, RoundingMode.HALF_UP);

            final Path pluginsFolder = CaptureBiomes.INSTANCE.getDataFolder().getParentFile().toPath();
            final Path targetPath = pluginsFolder.resolve(latestVersionFile.get("filename").getAsString());

            if (Files.notExists(pluginsFolder)) {

                throw new UpdateException("The server's plugin folder could not be found.");

            }

            player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                    .append(Component.text("Downloading version " + latestVersion.get("version_number").getAsString() + " of CaptureBiomes (" + updateSizeMB + "MB)...", NamedTextColor.GREEN)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

            CompletableFuture.supplyAsync(() -> {

                try {

                    HttpClient httpClient = HttpClient.newHttpClient();
                    HttpRequest httpRequest = HttpRequest.newBuilder().uri(URI.create(latestVersionFile.get("url").getAsString())).GET().build();
                    HttpResponse<InputStream> httpResponse = httpClient.send(httpRequest, HttpResponse.BodyHandlers.ofInputStream());

                    if (httpResponse.statusCode() != 200) {

                        throw new UpdateException("Modrinth API returned status code " + httpResponse.statusCode() + ".");

                    }

                    try (InputStream inputStream = httpResponse.body()) {

                        Files.copy(inputStream, targetPath, StandardCopyOption.REPLACE_EXISTING);

                    }

                    return true;

                } catch (Exception e) {

                    throw new UpdateException(e.getMessage());

                }

            }).thenAcceptAsync(success -> {

                // Delete the current plugin file on server shutdown
                final URL jarLocation = CaptureBiomes.INSTANCE.getClass().getProtectionDomain().getCodeSource().getLocation();
                final String path = jarLocation.getPath();
                final String fileName = path.substring(path.lastIndexOf('/') + 1);

                try {

                    new File(jarLocation.toURI()).deleteOnExit();

                    player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.GREEN, TextDecoration.BOLD)
                            .append(Component.text("Update successful! " +
                                            (restart ? "The server will now be restarted automatically." : "Please restart the server to apply it."),
                                    NamedTextColor.GREEN).decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

                    if (restart) {

                        CaptureBiomes.INSTANCE.getServer().restart();

                    }

                } catch (URISyntaxException e) {

                    Component warningMessage = Component.text("[Capture Biomes] ", NamedTextColor.RED, TextDecoration.BOLD)
                            .append(Component.text("Warning: The old plugin file ", NamedTextColor.RED)
                                    .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE))
                            .append(Component.text(fileName, NamedTextColor.YELLOW)
                                    .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE))
                            .append(Component.text(" could not be scheduled for removal. Please delete it manually to avoid conflicts on restart.", NamedTextColor.RED)
                                    .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE));

                    // Cancel server restart to avoid potential conflicts
                    if (restart) {

                        warningMessage = warningMessage.append(Component.text(" Your scheduled restart was canceled.", NamedTextColor.RED)
                                .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE));

                    }

                    player.sendMessage(warningMessage);

                }

            }, runnable -> CaptureBiomes.INSTANCE.getServer().getScheduler().runTask(CaptureBiomes.INSTANCE, runnable))
            .exceptionallyAsync(throwable -> {

                Throwable cause = throwable.getCause() != null ? throwable.getCause() : throwable;

                try {

                    Files.deleteIfExists(targetPath);

                } catch (IOException ioException) {

                    log("Could not remove (partially) downloaded update file: " + ioException.getMessage(), ConsoleUtils.LogType.WARNING);

                }


                Component failureMessage = Component.text("[Capture Biomes] ", NamedTextColor.RED, TextDecoration.BOLD)
                        .append(Component.text("The plugin update failed. Please check the server logs for more detailed information.", NamedTextColor.RED)
                                .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE));

                // Cancel server restart to avoid potential conflicts
                if (restart) {

                    failureMessage = failureMessage.append(Component.text(" Your scheduled server restart was canceled.", NamedTextColor.YELLOW)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE));

                }

                player.sendMessage(failureMessage);

                log("A plugin update triggered by " + player.getName() + " failed: " + cause.getMessage(), ConsoleUtils.LogType.SEVERE);

                return null;

            }, runnable -> CaptureBiomes.INSTANCE.getServer().getScheduler().runTask(CaptureBiomes.INSTANCE, runnable));

        }
        catch (Exception e) {

            player.sendMessage(Component.text("[Capture Biomes] ", NamedTextColor.RED, TextDecoration.BOLD)
                    .append(Component.text("The plugin update failed. Please check the server logs for more detailed information.", NamedTextColor.RED)
                            .decorationIfAbsent(TextDecoration.BOLD, TextDecoration.State.FALSE)));

            log("A plugin update triggered by " + player.getName() + " failed: " + e.getMessage(), ConsoleUtils.LogType.SEVERE);

        }
    }

}
