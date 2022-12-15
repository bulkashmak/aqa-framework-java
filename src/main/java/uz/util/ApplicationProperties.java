package uz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationProperties {

    private static final Properties properties = new Properties();

    static {
        try(InputStream inputStream = ApplicationProperties.class.getClassLoader().getResourceAsStream("application.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing application.properties", e);
        }
    }

    public static String getProperty(String nameProperty) {
        return properties.getProperty(nameProperty);
    }
}
