package ru.clevertec.valid.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.exception.ClientDtoNotValidate;
import ru.clevertec.valid.Validator;

import java.time.LocalDate;

class ValidatorImplTest {

    Validator validator;

    @BeforeEach
    void setUp() {
        validator = new ValidatorImpl();
    }

    @Test
    void shouldValidateClientDto() {
        ClientDto verification = ClientDto.builder()
                .clientName("ИванZ")
                .familyName("Иванов")
                .surName("Иванович")
                .birthDay(LocalDate.parse("2001-01-01"))
                .build();

        Assertions.assertThrows(ClientDtoNotValidate.class, () -> validator.validateClientDto(verification));
    }
}