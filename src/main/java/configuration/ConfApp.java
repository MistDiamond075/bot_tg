package configuration;

import exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.util.Properties;

public class ConfApp {
    private static final Properties properties = new Properties();

    public static void load() throws ConfigurationException {
        try (InputStream input = ObjectInputFilter.Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new ConfigurationException("application.properties not found");
            }
            properties.clear();
            properties.load(input);
        } catch (IOException e) {
            throw new ConfigurationException("File processing error", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
