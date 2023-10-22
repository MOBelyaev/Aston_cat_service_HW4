package ru.aston.learn.cat.service;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class PropertyService {

    private static PropertyService instance;

    public static PropertyService getInstance() {
        if (instance == null) {
            instance = new PropertyService();
        }
        return instance;
    }

    private final Properties properties;

    private PropertyService() {
        properties = new Properties();

        InputStream is = getClass().getResourceAsStream("/application.properties");
        if (is == null) {
            throw new RuntimeException("Can't load application properties");
        }
        try {
            properties.load(is);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        return properties.getProperty(key);
    }
}
