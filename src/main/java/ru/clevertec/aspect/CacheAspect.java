package ru.clevertec.aspect;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import ru.clevertec.cache.LRUCache;
import ru.clevertec.cache.impl.LRUCacheImpl;
import ru.clevertec.dao.ClientDao;
import ru.clevertec.dao.ConnectionPoolManager;
import ru.clevertec.dao.impl.ClientDaoImpl;
import ru.clevertec.dto.ClientDto;
import ru.clevertec.entity.Client;
import ru.clevertec.exception.ClientDtoNotValidate;
import ru.clevertec.exception.ClientNotFoundException;
import ru.clevertec.mapper.MapperClient;
import ru.clevertec.mapper.MapperClientImpl;
import ru.clevertec.valid.Validator;
import ru.clevertec.valid.impl.ValidatorImpl;

@Slf4j
@Aspect
public class CacheAspect {

    private static final int CAPACITY = ConnectionPoolManager.getCapacity();
    private final LRUCache<Long, Client> cache = new LRUCacheImpl<>(CAPACITY);
    private final ClientDao clientDao = new ClientDaoImpl();
    private final MapperClient mapper = new MapperClientImpl();
    private final Validator validator = new ValidatorImpl();

    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.findById(..))")
    public ClientDto aroundFindById(ProceedingJoinPoint joinPoint) throws ClientNotFoundException {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        Client client = cache.get(id);

        if (client != null) {
            return mapper.toClientDto(client);

        } else {
            try {
                client = clientDao.findById(id).get();
                cache.put(client.getId(), client);

                return mapper.toClientDto(client);

            } catch (RuntimeException e) {
                throw new ClientNotFoundException(id);
            }
        }
    }

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

    @Around("execution(* ru.clevertec.service.impl.ClientServiceImpl.delete(..))")
    public void aroundDelete(ProceedingJoinPoint joinPoint) {
        Object[] args = joinPoint.getArgs();
        Long id = (Long) args[0];
        clientDao.delete(id);
        cache.remove(id);
    }
}
