package ru.clevertec.service.impl;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.exception.ClientDtoNotValidate;
import ru.clevertec.exception.ClientNotFoundException;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.service.ClientService;
import ru.clevertec.util.valid.Validator;

import java.util.List;

/**
 * Отправляет и получает данные из базы данных.
 */
@AllArgsConstructor
@Service
public class ClientServiceImpl implements ClientService {

    private final ClientDao clientDao;
    private final MapperClient mapperClient;
    private final Validator validator;

    private static final long DEFAULT_PAGE_SIZE = 20L;
    private static final long DEFAULT_PAGE_NUM = 1L;

    /**
     * Ищет в Б.Д. клиента по его идентификатору.
     *
     * @param id идентификатор продукта.
     * @return Найденный клиент преобразованный в ClientDto.
     */
    @Override
    public ClientDto findById(long id) {
        return clientDao.findById(id)
                .map(mapperClient::toClientDto)
                .orElseThrow(() -> new ClientNotFoundException(id));
    }

    /**
     * Возвращает из Б.Д. заданное количество клиентов.
     *
     * @param pageNum  Номер страницы.
     * @param pageSize Размер страницы.
     * @return Возвращает найденное количество клиентов и помещает их в List.
     */
    @Override
    public List<ClientDto> findByAll(Long pageNum, Long pageSize) {
        return clientDao.findByAll()
                .stream()
                .skip(getNumSkip(pageNum, pageSize))
                .limit(pageSize)
                .map(mapperClient::toClientDto)
                .toList();
    }

    /**
     * Метод возвращает количество страниц.
     *
     * @param pageNum  Номер страницы.
     * @param pageSize Размерность страницы.
     * @return Количество пропускаемых страниц.
     */
    private long getNumSkip(Long pageNum, Long pageSize) {

        if (pageSize == null || pageSize <= 0) {
            pageSize = DEFAULT_PAGE_SIZE;
        }

        if (pageNum == null || pageNum <= 0) {
            pageNum = DEFAULT_PAGE_NUM;
        }

        return (pageNum - 1L) * pageSize;
    }

    /**
     * Записывает в Б.Д. одного клиента и возвращает его если запись успешна.
     *
     * @param clientDto Записываемый в Б.Д. клиент.
     * @return Возвращает клиента если запись успешна.
     */
    @Override
    public Client create(ClientDto clientDto) {

        try {
            validator.validateClientDto(clientDto);
        } catch (ClientDtoNotValidate notValidate) {
            notValidate.printStackTrace();
        }

        Client client = mapperClient.toClient(clientDto);

        return clientDao.create(client);
    }

    /**
     * Обновляет одного заданного клиента в Б.Д.
     *
     * @param id        Идентификатор обновляемого клиента.
     * @param clientDto Клиент с обновлёнными данными.
     * @return Возврат обновлённого клиента в случае успеха.
     */
    @Override
    public Client update(long id, ClientDto clientDto) {

        try {
            validator.validateClientDto(clientDto);
        } catch (ClientDtoNotValidate notValidate) {
            notValidate.printStackTrace();
        }

        Client client = mapperClient.toClient(clientDto);

        return clientDao.update(id, client);
    }

    /**
     * Удаляет одного заданного клиента из Б.Д.
     *
     * @param id Идентификатор удаляемого клиента
     */
    @Override
    public void delete(long id) {
        clientDao.delete(id);
    }
}
