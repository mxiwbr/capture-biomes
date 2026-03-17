package io.github.mxiwbr.capturebioms.utils;


import io.github.mxiwbr.capturebioms.CaptureBioms;

public class ConsoleUtils {

    public enum logType {

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
    public static void logConsole(String message, logType type) {

        if (CaptureBioms.CONFIG.isEnableConsoleLogging()) {

            switch (type) {

                case INFO:
                    CaptureBioms.LOGGER.info(message);
                    break;
                case ADDITIONAL_INFO:
                    if (CaptureBioms.CONFIG.isEnableAdditionalConsoleLogging()) {
                        CaptureBioms.LOGGER.info(message);
                    }
                    break;
                case WARNING:
                    CaptureBioms.LOGGER.warning(message);
                    break;
                case SEVERE:
                    CaptureBioms.LOGGER.severe(message);
                    break;

            }

        }

    }

}
