package ru.clevertec.cache.impl;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.clevertec.cache.LRUCache;
import ru.clevertec.entity.Client;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class LRUCacheImplTest {

    LRUCache<Long, Client> lruCache;
    Client expected;

    @BeforeEach
    void setUp() {
        lruCache = new LRUCacheImpl<>(5);
        expected = Client.builder()
                .id(1L)
                .clientName("Иван")
                .familyName("Иванов")
                .surName("Иванович")
                .birthDay(LocalDate.parse("2001-01-01"))
                .build();
    }

    @Test
    void shouldGetObject() {
        lruCache.put(expected.getId(), expected);
        Client actual = (Client) lruCache.get(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldPutAndGet() {
        lruCache.put(expected.getId(), expected);
        Client actual = (Client) lruCache.get(1L);

        Assertions.assertEquals(expected, actual);
    }

    @Test
    void shouldRemove() {
        lruCache.put(1L, expected);
        lruCache.remove(1L);
        Client actual = lruCache.get(1L);

        assertNull(actual);
    }
}