package ru.clevertec.dao;

import ru.clevertec.entity.Client;

import java.util.List;
import java.util.Optional;

public interface ClientDao {

    Optional<Client> findById(long id);

    List<Client> findByAll();

    Client create(Client client);

    Client update(long id, Client client);

    void delete(long id);
}
