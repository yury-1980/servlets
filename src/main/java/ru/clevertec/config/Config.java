package ru.clevertec.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.clevertec.gson.LocalDateAdapter;
import ru.clevertec.gson.LocalDateSerializer;
import ru.clevertec.gson.OffsetDateTimeAdapter;
import ru.clevertec.gson.OffsetDateTimeSerializer;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Config {

    private static Config config;

    private static final Gson JSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
            .create();

    private Config() {
    }

    public static Config getConfig() {

        if (config == null) {
            config = new Config();
        }
        return config;

    }

    public Gson getJson() {
        return JSON;
    }
}
