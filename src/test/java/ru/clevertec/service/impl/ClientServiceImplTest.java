package ru.clevertec.service.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.valid.Validator;
import ru.clevertec.valid.impl.ValidatorImpl;

import java.time.LocalDate;
import java.util.List;

class ClientServiceImplTest {

    Client client;
    ClientDao clientDao;
    MapperClient mapperClient;
    Validator validator;
    ClientDto clientDto;
    ClientDto expected;
    ClientService service;

    @BeforeEach
    void setUp() {
        client = Client.builder()
                .clientName("Иван")
                .familyName("Иванов")
                .surName("Иванович")
                .birthDay(LocalDate.parse("2001-01-01"))
                .build();

        clientDto = ClientDto.builder()
                .clientName("Пётр")
                .familyName("Петров")
                .surName("Петровия")
                .birthDay(LocalDate.parse("2001-02-02"))
                .build();

        expected = ClientDto.builder()
                .clientName("Вася")
                .familyName("Васильев")
                .surName("Васильевич")
                .birthDay(LocalDate.parse("2000-01-01"))
                .build();

        clientDao = new ClientDaoImpl(new ConnectionPoolManager());
        mapperClient = new MapperClientImpl();
        validator = new ValidatorImpl();
        service = new ClientServiceImpl(clientDao, mapperClient, validator);
    }

    @Test
    void shouldFindByIdClientDto() {
        expected = ClientDto.builder()
                .clientName("Пётр")
                .familyName("Петров")
                .surName("Петровия")
                .birthDay(LocalDate.parse("2001-02-02"))
                .build();

        ClientDto actual = service.findById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldFindByAllClientDto() {
        List<ClientDto> actualList = service.findByAll(3);
        ClientDto[] actual = actualList.toArray(new ClientDto[0]);


        List<ClientDto> dtoList = service.findByAll(3);
        ClientDto[] expected = dtoList.toArray(new ClientDto[0]);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateClientAndReturnClientDto() {
        Client clientDto1 = service.create(expected);
        ClientDto actual = mapperClient.toClientDto(clientDto1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldUpdateClientAndReturnClientDto() {
        Client updateClient = service.update(1, clientDto);
        ClientDto actual = mapperClient.toClientDto(updateClient);
        expected = service.findById(1);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldDeleteLastClient() {
        Client clientDto1 = service.create(clientDto);
        long lastId = clientDto1.getId();

        service.delete(lastId);
    }
}