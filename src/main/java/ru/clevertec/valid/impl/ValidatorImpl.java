package ru.clevertec.valid.impl;


import ru.clevertec.dto.ClientDto;
import ru.clevertec.exception.ClientDtoNotValidate;
import ru.clevertec.valid.Validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import java.util.Set;

public class ValidatorImpl implements Validator {

    @Override
    public void validateClientDto(ClientDto clientDto) throws ClientDtoNotValidate {
        javax.validation.Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

        Set<ConstraintViolation<ClientDto>> violations = validator.validate(clientDto);

        if (!violations.isEmpty()) {

            for (ConstraintViolation<ClientDto> violation : violations) {
                System.out.println(violation.getMessage());
            }
            throw new ClientDtoNotValidate();
        }
    }
}
