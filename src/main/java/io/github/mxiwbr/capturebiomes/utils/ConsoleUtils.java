package io.github.mxiwbr.capturebiomes.utils;

import io.github.mxiwbr.capturebiomes.CaptureBiomes;

public class ConsoleUtils {

    /**
     * The types of console logging messages:
     * INFO, ADDITIONAL_INFO, WARNING, SEVERE
     */
    public enum LogType {

        INFO,

        ADDITIONAL_INFO,
        WARNING,
        SEVERE,

    }

    /**
     * Logs something in the console if console logging is enabled in the config.yml
     * @param message the message which should be logged in the server console
     * @param type the type of the log (INFO, ADDITIONAL_INFO, WARNING, SEVERE)
     */
    public static void log(String message, LogType type) {

        if (CaptureBiomes.CONFIG.isEnableConsoleLogging()) {

            switch (type) {

                case INFO:
                    CaptureBiomes.LOGGER.info(message);
                    break;
                case ADDITIONAL_INFO:
                    if (CaptureBiomes.CONFIG.isEnableAdditionalConsoleLogging()) {
                        CaptureBiomes.LOGGER.info(message);
                    }
                    break;
                case WARNING:
                    CaptureBiomes.LOGGER.warning(message);
                    break;
                case SEVERE:
                    CaptureBiomes.LOGGER.severe(message);
                    break;

            }

        }

    }

    /**
     * Sends the "If you think that this is a bug, please create an issue" message in the server console
     */
    public static void logCreateIssueMessage() {

        log("If you think that this is a bug, please create an issue: https://github.com/mxiwbr/capture-biomes/issues", ConsoleUtils.LogType.WARNING);

    }

}
