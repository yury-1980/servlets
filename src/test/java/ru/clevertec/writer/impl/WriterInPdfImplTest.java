package ru.clevertec.writer.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.serializator.SerializatorXML;
import ru.clevertec.serializator.impl.SerializatorXMLImpl;
import ru.clevertec.writer.WriterInPdf;

import java.time.LocalDate;
import java.util.List;

@ExtendWith(MockitoExtension.class)
class WriterInPdfImplTest {

    private static String filePathExpected;
    private static String outputFilePathExpected;

    @Mock
    private static WriterInPdf writerInPdf;

    @Captor
    private ArgumentCaptor<String> serializeCaptor;

    @Captor
    private ArgumentCaptor<String> filePathCaptor;

    @Captor
    private ArgumentCaptor<String> outputFilePathCaptor;

    @Captor
    private ArgumentCaptor<Client> clientCaptor;

    @Captor
    private ArgumentCaptor<List<Client>> listCaptor;

    @BeforeAll
    public static void setUp() {
        filePathExpected = "Clevertec_Template.pdf";
        outputFilePathExpected = "ResoultService.pdf";
        writerInPdf = WriterInPdfImpl.builder().build();
    }

    @Test
    void shouldWriteStringInPdf() {
        // given
        ClientDto clientDtoTwo = new ClientDto("Николай", "Николаев", "Николаевич",
                LocalDate.parse("2000-01-01"));

        SerializatorXML serializatorXML = new SerializatorXMLImpl();
        String serializeExpected = serializatorXML.serialize(clientDtoTwo);

        // when
        writerInPdf.write(serializeExpected, filePathExpected, outputFilePathExpected);
        Mockito.verify(writerInPdf)
                .write(serializeCaptor.capture(), filePathCaptor.capture(), outputFilePathCaptor.capture());

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals(serializeExpected, serializeCaptor.getValue()),
                () -> Assertions.assertEquals(filePathExpected, filePathCaptor.getValue()),
                () -> Assertions.assertEquals(outputFilePathExpected, outputFilePathCaptor.getValue())
        );
    }

    @Test
    void shouldWriteObjectInPdf() {
        // given
        Client clientExpected = Client.builder()
                .clientName("Иван")
                .familyName("Иванов")
                .surName("Иванович")
                .birthDay(LocalDate.parse("2001-01-01"))
                .build();

        // when
        writerInPdf.write(clientExpected, filePathExpected, outputFilePathExpected);
        Mockito.verify(writerInPdf)
                .write(clientCaptor.capture(), filePathCaptor.capture(), outputFilePathCaptor.capture());

        // then
        Assertions.assertAll(
                () -> Assertions.assertEquals(clientExpected, clientCaptor.getValue()),
                () -> Assertions.assertEquals(filePathExpected, filePathCaptor.getValue()),
                () -> Assertions.assertEquals(outputFilePathExpected, outputFilePathCaptor.getValue())
        );
    }

    @Test
    void shouldWriteListInPdf() {
        // given
        List<Client> dtoList = List.of(Client.builder()
                        .clientName("Иван")
                        .familyName("Иванов")
                        .surName("Иванович")
                        .birthDay(LocalDate.parse("2001-01-01"))
                        .build(),
                Client.builder()
                        .clientName("Иван")
                        .familyName("Иванов")
                        .surName("Иванович")
                        .birthDay(LocalDate.parse("2001-01-01"))
                        .build(),
                Client.builder()
                        .clientName("Иван")
                        .familyName("Иванов")
                        .surName("Иванович")
                        .birthDay(LocalDate.parse("2001-01-01"))
                        .build());
        Client[] expected = dtoList.toArray(new Client[0]);

        // when
        writerInPdf.write(dtoList, filePathExpected, outputFilePathExpected);
        Mockito.verify(writerInPdf)
                .write(listCaptor.capture(), filePathCaptor.capture(), outputFilePathCaptor.capture());

        // then
        Assertions.assertAll(
                () -> Assertions.assertArrayEquals(expected, listCaptor.getValue().toArray(Client[]::new)),
                () -> Assertions.assertEquals(filePathExpected, filePathCaptor.getValue()),
                () -> Assertions.assertEquals(outputFilePathExpected, outputFilePathCaptor.getValue())
        );
    }
}