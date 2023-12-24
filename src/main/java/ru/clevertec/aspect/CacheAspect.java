package ru.clevertec.aspect;

import com.google.gson.Gson;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.clevertec.cache.LRUCache;
import ru.clevertec.cache.impl.LRUCacheImpl;
import ru.clevertec.config.Config;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.exception.ClientDtoNotValidate;
import ru.clevertec.exception.ClientNotFoundException;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.util.valid.Validator;
import ru.clevertec.util.valid.impl.ValidatorImpl;
import ru.clevertec.util.writer.WriterInPdf;
import ru.clevertec.util.writer.impl.WriterInPdfImpl;

@Slf4j
@Aspect
public class CacheAspect {

    private static final int CAPACITY = ConnectionPoolManager.getCapacity();
    private final LRUCache<Long, Client> cache = new LRUCacheImpl<>(CAPACITY);
    private final ClientDao clientDao = new ClientDaoImpl();
    private final MapperClient mapper = new MapperClientImpl();
    private final Validator validator = new ValidatorImpl();
    private static final String RESULT_FIND_BY_ID = "ResoultFindById.pdf";
    private static final String CLEVERTEC_TEMPLATE = "Clevertec_Template.pdf";

    /**
     * Возвращает объект из кеша, если нет, то читает из базы.
     *
     * @param joinPoint Точка внедрения кода в метод ClientServiceImpl.findById()
     * @return ClientDto.
     * @throws ClientNotFoundException если не найден.
     */
    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.findById(..))")
    public ClientDto aroundFindById(ProceedingJoinPoint joinPoint) throws ClientNotFoundException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        Client client = cache.get(id);

        if (client != null) {
            getWriterInPdf(client);

            return mapper.toClientDto(client);

        } else {

            if (clientDao.findById(id)
                    .isPresent()) {
                client = clientDao.findById(id)
                        .get();
                cache.put(client.getId(), client);
                getWriterInPdf(client);

                return mapper.toClientDto(client);
            } else {
                throw new ClientNotFoundException(id);
            }
        }
    }

    /**
     * Печать объекта в pdf файл.
     *
     * @param v   Сам бъект
     * @param <T> Тип объекта.
     */
    private <T> void getWriterInPdf(T v) {
        Gson json = Config.getConfig()
                .getJson();
        WriterInPdf writerInPdf = WriterInPdfImpl.builder()
                .gson(json)
                .build();
        writerInPdf.write(v, CLEVERTEC_TEMPLATE, RESULT_FIND_BY_ID);
    }

    /**
     * Создаёт объект Client из объекта Dto. И помещает в БД, а затем в кеш.
     *
     * @param joinPoint Точка внедрения кода в метод ClientServiceImpl.create()
     * @return Новый Client.
     */
    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.create(..))")
    public Client aroundCreate(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        ClientDto clientDto = (ClientDto) args[0];

        try {
            validator.validateClientDto(clientDto);
        } catch (ClientDtoNotValidate notValidate) {
            notValidate.printStackTrace();
        }

        Client client = mapper.toClient(clientDto);
        Client newClient = clientDao.create(client);
        cache.put(newClient.getId(), newClient);

        return newClient;
    }

    /**
     * Обновляет объект в БД и кеше.
     *
     * @param joinPoint Точка внедрения кода в метод ClientServiceImpl.update()
     * @return Обновлённый объект.
     */
    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.update(..))")
    public Client aroundUpdate(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        ClientDto clientDto = (ClientDto) args[1];

        try {
            validator.validateClientDto(clientDto);
        } catch (ClientDtoNotValidate notValidate) {
            notValidate.printStackTrace();
        }

        Client client = mapper.toClient(clientDto);
        Client newClient = clientDao.update(id, client);
        cache.put(newClient.getId(), newClient);

        return newClient;
    }

    /**
     * Удаление объекта из БД, а затем из кеша.
     *
     * @param joinPoint Точка внедрения кода в метод ClientServiceImpl.delete()
     */
    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.delete(..))")
    public void aroundDelete(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        clientDao.delete(id);
        cache.remove(id);
    }
}
