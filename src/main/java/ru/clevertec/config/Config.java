package ru.clevertec.config;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.service.impl.ClientServiceImpl;
import ru.clevertec.util.gson.LocalDateAdapter;
import ru.clevertec.util.gson.LocalDateSerializer;
import ru.clevertec.util.gson.OffsetDateTimeAdapter;
import ru.clevertec.util.gson.OffsetDateTimeSerializer;
import ru.clevertec.util.valid.Validator;
import ru.clevertec.util.valid.impl.ValidatorImpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;

public class Config {

    private static final Gson JSON = new GsonBuilder()
            .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
            .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
            .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
            .create();

    private static final Validator validator = new ValidatorImpl();
    private static final MapperClient mapperClient = new MapperClientImpl();
    private static final ClientDao clientDao = new ClientDaoImpl();
    private static final ClientService clientService = new ClientServiceImpl(clientDao, mapperClient, validator);
    private static Config config;

    private Config() {
    }

    public static Config getConfig() {

        if (config == null) {
            config = new Config();
        }
        return config;

    }

    public ClientService getClientService() {
        return clientService;
    }

    public Gson getJson() {
        return JSON;
    }
}