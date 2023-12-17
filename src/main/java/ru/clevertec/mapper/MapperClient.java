package ru.clevertec.mapper;

import org.mapstruct.Mapper;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;

import java.time.LocalDate;

@Mapper
public interface MapperClient {

    ClientDto toClientDto(Client client);

    Client toClient(ClientDto clientDto);

    default String localDateToString(LocalDate localDate) {
        return localDate != null ? localDate.toString() : null;
    }
}
