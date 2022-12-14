package uz.gateway;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class B2bProperties {

    private static Properties properties = new Properties();

    static {
        try(InputStream inputStream = B2bProperties.class.getClassLoader().getResourceAsStream("b2b.properties")) {
            properties.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException("Error while parsing b2b.properties", e);
        }
    }

    public static String getProperty(String nameProperty) {
        return properties.getProperty(nameProperty);
    }
}
