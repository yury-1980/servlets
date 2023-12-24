package ru.clevertec.cache;

public interface LRUCache<K, V> {

    V get(Long key);

    void put(K key, V value);

    void remove(Long key);
}
