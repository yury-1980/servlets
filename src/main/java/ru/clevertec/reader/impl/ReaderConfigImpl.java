package ru.clevertec.reader.impl;

import ru.clevertec.reader.ReaderConfig;

import java.io.IOException;
import java.util.Properties;

public class ReaderConfigImpl implements ReaderConfig {

    private final Properties properties = new Properties();

    @Override
    public Properties readerFileYaml(String name) {
        try {
            properties.load(ReaderConfigImpl.class.getClassLoader().getResourceAsStream(name));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return properties;
    }
}
