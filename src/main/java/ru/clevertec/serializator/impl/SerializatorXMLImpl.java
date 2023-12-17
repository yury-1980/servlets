package ru.clevertec.serializator.impl;

import jakarta.xml.bind.JAXBContext;
import jakarta.xml.bind.JAXBException;
import jakarta.xml.bind.Marshaller;
import ru.clevertec.serializator.SerializatorXML;

import java.io.StringWriter;

public class SerializatorXMLImpl implements SerializatorXML {

    private String xml;

    @Override
    public String serialize(Object o) {
        try {
            // Создаем контекст JAXB для класса Person
            JAXBContext jaxbContext = JAXBContext.newInstance(o.getClass());

            // Создаем Marshaller для преобразования объекта в XML
            Marshaller marshaller = jaxbContext.createMarshaller();

            // Устанавливаем свойства Marshaller (опционально)
            marshaller.setProperty(Marshaller.JAXB_FORMATTED_OUTPUT, true);

            // Создаем StringWriter для записи XML
            StringWriter stringWriter = new StringWriter();

            // Преобразуем объект в XML и записываем в StringWriter
            marshaller.marshal(o, stringWriter);

            // Получаем XML в виде строки
            xml = stringWriter.toString();

        } catch (JAXBException e) {
            e.printStackTrace();
        }
        return xml;
    }
}
