package fr.skillup.core.config;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;

public class Config {

    private static Properties properties;

    /**
     * Charge le fichier de configuration
     */
    public static void load() {
        Config.properties = new Properties();
        try {
            Config.properties.load(Objects.requireNonNull(Config.class.getResource("/config.properties")).openStream());
        } catch (IOException e) {
            Logger.getLogger(Config.class.getName()).severe(e.getMessage());
        }
    }

    public static String get(String key) {
        return Config.properties.getProperty(key);
    }
}
