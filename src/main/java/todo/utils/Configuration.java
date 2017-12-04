package todo.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import todo.exception.ConfigurationLoadException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Configuration {

    private static final Logger LOGGER = LoggerFactory.getLogger(Configuration.class);

    private static Configuration INSTANCE = null;

    private final Properties properties = new Properties();

    private Configuration(String fileName) {
        if (fileName == null || fileName.length() == 0) {
            throw new IllegalArgumentException("Properties file name cannot be NULL or EMPTY");
        }
        try (InputStream stream = this.getClass().getClassLoader().getResourceAsStream(fileName)) {
            if (stream == null) {
                throw new ConfigurationLoadException("Cannot find file: " + fileName);
            }
            properties.load(stream);
        } catch (IOException e) {
            LOGGER.error(e.getMessage(), e);
            throw new ConfigurationLoadException(e);
        }
    }

    public static Configuration getInstance(String fileName) {
        if (INSTANCE == null) {
            INSTANCE = new Configuration(fileName);
        }
        return INSTANCE;
    }

    public String getDriver() {
        return properties.getProperty("db.driver");
    }

    public String getUrl() {
        return properties.getProperty("db.url");
    }

    public String getUser() {
        return properties.getProperty("db.user");
    }

    public String getPassword() {
        return properties.getProperty("db.pass");
    }
}
