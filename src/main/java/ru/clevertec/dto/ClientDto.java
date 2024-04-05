package ru.clevertec.dto;

import jakarta.xml.bind.annotation.XmlRootElement;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.PastOrPresent;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;
import java.time.LocalDate;

@XmlRootElement
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ClientDto {

    @NotEmpty(message = "Name should not  be empty!")
    @Size(min = 2, max = 10, message = "Name should be from 2 to 10 characters")
    @Pattern(regexp = "^[Ёёа-яА-Я\\s]+$", message = "Имя должно состоять из русских букв!")
    private String clientName;

    @NotEmpty
    @Pattern(regexp = "^[Ёёа-яА-Я\\s]{2,20}$")
    private String familyName;

    @NotEmpty
    @Pattern(regexp = "^[Ёёа-яА-Я\\s]{2,20}$")
    private String surName;

    @NotNull
    @PastOrPresent
    LocalDate birthDay;
}
