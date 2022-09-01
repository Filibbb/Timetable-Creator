package ch.zhaw.pm2.napp.logger;

import java.io.IOException;
import java.io.InputStream;
import java.util.Locale;
import java.util.logging.LogManager;
import java.util.logging.Logger;

import static java.util.Locale.ROOT;
import static java.util.logging.Level.WARNING;

/**
 * A utility class that helps reading the logger configuration and is used for configuring the logger of java util.
 * Reads the log properties in the resources root for its configuration
 *
 * @author buechad1
 */
public class LogConfiguration {
    private static final Logger logger = Logger.getLogger(LogConfiguration.class.getCanonicalName());

    private LogConfiguration() {
    }

    /*
     * Static class configuration.
     * Only executed once when class is loaded.
     * Load Java logger configuration from config file
     */
    static {
        Locale.setDefault(ROOT);
        String logConfigFile = System.getProperty("java.util.logging.config.file", "log.properties");
        try (InputStream configFileStream = ClassLoader.getSystemClassLoader().getResourceAsStream("log.properties")) {
            if (configFileStream != null) {
                LogManager.getLogManager().readConfiguration(configFileStream);
                logger.fine("Log configuration read from " + logConfigFile);
            } else {
                logger.warning("No log configuration found. Using system default settings.");
            }
        } catch (IOException e) {
            logger.log(WARNING, "Error loading log configuration", e);
        }
    }

}
