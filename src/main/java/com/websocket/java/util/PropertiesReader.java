package com.websocket.java.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by anton
 */
public class PropertiesReader {
    private static final Properties properties;
    private static final InputStream INPUT_STREAM;

    static {
        String fileName = "database.properties";
        INPUT_STREAM = PropertiesReader.class.getClassLoader().getResourceAsStream(fileName);

        if (INPUT_STREAM == null)
            throw new IllegalStateException("No " + fileName + " file in classpath!");

        properties = new Properties();
        try {
            properties.load(INPUT_STREAM);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                INPUT_STREAM.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private PropertiesReader() {
    }

    public static String get(String propertyName) {
        return properties.getProperty(propertyName);
    }

}
