package ru.clevertec.reader.impl;

import org.yaml.snakeyaml.Yaml;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.reader.ReaderConfig;

import java.io.InputStream;
import java.util.Map;

public class ReaderConfigImpl implements ReaderConfig {

    @Override
    public Map<String, String> readerFileYaml(String name) {

        Yaml yaml = new Yaml();
        InputStream inputStream = ConnectionPoolManager.class.getClassLoader()
                .getResourceAsStream(name);

        return yaml.load(inputStream);
    }
}
