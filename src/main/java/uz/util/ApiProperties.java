package uz.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApiProperties {

    private static final Properties properties = new Properties();

    static {
        try(InputStream inputStream = ApiProperties.class.getClassLoader().getResourceAsStream("api.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing api.properties", e);
        }
    }

    public static String getProperty(String nameProperty) {
        return properties.getProperty(nameProperty);
    }
}
