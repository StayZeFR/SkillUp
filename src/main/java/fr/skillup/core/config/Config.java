package fr.skillup.core.config;

import java.io.IOException;
import java.util.Objects;
import java.util.Properties;

public class Config {

    private static Properties properties;

    public static void load() {
        Config.properties = new Properties();
        try {
            Config.properties.load(Objects.requireNonNull(Config.class.getResource("/config.properties")).openStream());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return Config.properties.getProperty(key);
    }
}
