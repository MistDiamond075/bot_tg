package configuration;

import java.io.IOException;
import java.io.InputStream;
import java.io.ObjectInputFilter;
import java.util.Properties;

public class ConfApp {
    private static final Properties properties = new Properties();

    static {
        try (InputStream input = ObjectInputFilter.Config.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (input == null) {
                throw new RuntimeException("Не найден config.properties");
            }
            properties.load(input);
        } catch (IOException e) {
            throw new RuntimeException("Ошибка загрузки конфигурации", e);
        }
    }

    public static String get(String key) {
        return properties.getProperty(key);
    }
}
