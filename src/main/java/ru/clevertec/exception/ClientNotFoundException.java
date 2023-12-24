package ru.clevertec.exception;

public class ClientNotFoundException extends RuntimeException {

    /**
     * Сообщение должно быть именно такого формата
     *
     * @param id - идентификатор продукта
     */
    public ClientNotFoundException(long id) {
        super(String.format("Client not found, id = %d", id));
    }
}
