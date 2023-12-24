package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;

@Mapper
public interface MapperClient {

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);
}
