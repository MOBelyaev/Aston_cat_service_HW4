package ru.aston.learn.cat.service;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ObjectMapperService {

    private static ObjectMapper instance;

    public static synchronized ObjectMapper getInstance() {
        if (instance == null) {
            instance = new ObjectMapper();
            instance.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        }
        return instance;
    }

    private ObjectMapperService() {
    }
}
