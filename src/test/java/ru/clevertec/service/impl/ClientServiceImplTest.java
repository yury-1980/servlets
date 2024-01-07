package ru.clevertec.service.impl;

import com.zaxxer.hikari.HikariDataSource;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.service.ClientService;
import ru.clevertec.util.valid.Validator;
import ru.clevertec.util.valid.impl.ValidatorImpl;

import java.sql.Connection;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

class ClientServiceImplTest {

    private MapperClient mapperClient;
    private ClientService service;
    private ClientDto clientDto;
    private ClientDto expected;
    private Long pageSize;
    private Client client;
    private Long pageNum;
    private final HikariDataSource DATA_SOURCE = new HikariDataSource();

    @BeforeAll
    static void setBef() throws SQLException {

    }

    @BeforeEach
    void setUp() throws SQLException {
        DATA_SOURCE.setJdbcUrl("jdbc:postgresql://localhost:5432/Cache");
        DATA_SOURCE.setUsername("postgres");
        DATA_SOURCE.setPassword("postgres");
        DATA_SOURCE.setDriverClassName("org.postgresql.Driver");
        DATA_SOURCE.setAutoCommit(false);

        Connection connection = DATA_SOURCE.getConnection();

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

        pageNum = 1L;
        pageSize = 1L;
        ClientDao clientDao = new ClientDaoImpl(connection);
        Validator validator = new ValidatorImpl();
        mapperClient = new MapperClientImpl();
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
        List<ClientDto> actualList = service.findByAll(pageNum, pageSize);
        ClientDto[] actual = actualList.toArray(new ClientDto[0]);


        List<ClientDto> dtoList = service.findByAll(pageNum, pageSize);
        ClientDto[] expected = dtoList.toArray(new ClientDto[0]);

        Assertions.assertArrayEquals(expected, actual);
    }

    @Test
    void shouldCreateClientAndReturnClientDto() {
        client = service.create(expected);
        ClientDto actual = mapperClient.toClientDto(client);

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