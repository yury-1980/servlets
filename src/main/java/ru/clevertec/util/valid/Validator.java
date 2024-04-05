package ru.clevertec.util.valid;

import ru.clevertec.dto.ClientDto;
import ru.clevertec.exception.ClientDtoNotValidate;

public interface Validator {

    void validateClientDto(ClientDto clientDto) throws ClientDtoNotValidate;
}
