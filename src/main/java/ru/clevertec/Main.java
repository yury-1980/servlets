package ru.clevertec;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import lombok.extern.slf4j.Slf4j;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.exception.ClientNotFoundException;
import ru.clevertec.gson.LocalDateAdapter;
import ru.clevertec.gson.LocalDateSerializer;
import ru.clevertec.gson.OffsetDateTimeAdapter;
import ru.clevertec.gson.OffsetDateTimeSerializer;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.serializator.SerializatorXML;
import ru.clevertec.serializator.impl.SerializatorXMLImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.service.impl.ClientServiceImpl;
import ru.clevertec.valid.Validator;
import ru.clevertec.valid.impl.ValidatorImpl;
import ru.clevertec.writer.WriterInPdf;
import ru.clevertec.writer.impl.WriterInPdfImpl;

import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.List;

@Slf4j
public class Main {
    public static void main(String[] args) {

        Validator validator = new ValidatorImpl();
        MapperClient mapperClient = new MapperClientImpl();
        ClientDao clientDao = new ClientDaoImpl(new ConnectionPoolManager());
        ClientService clientService = new ClientServiceImpl(clientDao, mapperClient, validator);
        Gson gson = new GsonBuilder()
                .registerTypeAdapter(LocalDate.class, new LocalDateAdapter())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeAdapter())
                .registerTypeAdapter(LocalDate.class, new LocalDateSerializer())
                .registerTypeAdapter(OffsetDateTime.class, new OffsetDateTimeSerializer())
                .create();
        String json;

        ClientDto clientDto = new ClientDto("Саша", "Петров", "Петрович",
                LocalDate.parse("2000-01-01"));
        ClientDto clientDtoTwo = new ClientDto("Николай", "Николаев", "Николаевич",
                LocalDate.parse("2000-01-01"));
        String outputFilePath;
        String filePath = "Clevertec_Template.pdf";
        WriterInPdf writerInPdf = WriterInPdfImpl.builder()
                .gson(gson)
                .build();

        // 1. Создание объекта.
        Client clientFirst = clientService.create(clientDto);
        json = gson.toJson(clientFirst);
        System.out.println("clientFirst = " + json);
        outputFilePath = "ResoultCreate.pdf";
        writerInPdf.write(clientFirst, filePath, outputFilePath);

        // 2. Получение объекта
        ClientDto clientDtoFirst = null;

        try {
            clientDtoFirst = clientService.findById(150);
        } catch (ClientNotFoundException e) {
            log.error(e.getMessage());
        }

        json = gson.toJson(clientDtoFirst);
        System.out.println("clientDtoFirst = " + json);
        outputFilePath = "ResoultFindById.pdf";
        writerInPdf.write(clientDtoFirst, filePath, outputFilePath);

        // 3. Изменение объекта
        Client update = clientService.update(1, clientDtoTwo);
        json = gson.toJson(update);
        System.out.println("update = " + json);
        outputFilePath = "ResoultUpdate.pdf";
        writerInPdf.write(update, filePath, outputFilePath);

        // Сериализация объекта
        SerializatorXML serializatorXML = new SerializatorXMLImpl();
        String serialize = serializatorXML.serialize(clientDtoTwo);
        System.out.println("serialize = " + serialize);
        outputFilePath = "ResoultSerializatorXML.pdf";
        writerInPdf.write(serialize, filePath, outputFilePath);

        // 4. Получение заданного кол-ва объектов
        List<ClientDto> serviceByAll = clientService.findByAll(30);
        json = gson.toJson(serviceByAll);
        System.out.println("serviceByAll = " + json);
        outputFilePath = "ResoultFindByAll.pdf";
        writerInPdf.write(serviceByAll, filePath, outputFilePath);
    }
}