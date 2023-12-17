package ru.clevertec.service;

import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;

import java.util.List;

public interface ClientService {

    ClientDto findById(long id);

    List<ClientDto> findByAll(long num);

    Client create(ClientDto clientDto);

    Client update(long id, ClientDto clientDto);

    void delete(long id);
}
