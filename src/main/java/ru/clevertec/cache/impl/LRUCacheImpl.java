package ru.clevertec.cache.impl;

import lombok.Data;
import ru.clevertec.cache.LRUCache;

import java.util.HashMap;
import java.util.Map;

@Data
public class LRUCacheImpl<V> implements LRUCache<Long, V> {

    Node<V> head;
    Node<V> tail;
    Map<Long, Node<V>> map;
    int capacity;

    public LRUCacheImpl(int capacity) {
        this.capacity = capacity;
        this.map = new HashMap<>();
    }

    @Override
    public V get(Long key) {
        if (map.get(key) == null) {
            return null;
        }

        Node<V> item = map.get(key);
        // Перемещаем в конец
        removeNode(item);
        addToTail(item);

        return item.value;
    }

    @Override
    public void put(Long key, V value) {
        if (map.containsKey(key)) {
            Node<V> item = map.get(key);
            item.value = value;

            // Перемещаем в конец
            removeNode(item);
            addToTail(item);
        } else {
            if (map.size() >= capacity) {
                // Удаляем голову
                map.remove(head.key);
                removeNode(head);
            }

            // Добавляем в конец
            Node<V> node = new Node<>(key, value);
            addToTail(node);
            map.put(key, node);
        }
    }

    public void remove(Long key) {
        if (map.containsKey(key)) {
            Node<V> item = map.get(key);
            removeNode(item);
            map.remove(key);
        }
    }

    private void removeNode(Node<V> node) {
        if (node.prev != null) {
            node.prev.next = node.next;
        } else {
            head = node.next;
        }

        if (node.next != null) {
            node.next.prev = node.prev;
        } else {
            tail = node.prev;
        }
    }

    private void addToTail(Node<V> node) {
        if (tail != null) {
            tail.next = node;
        }

        node.prev = tail;
        node.next = null;
        tail = node;

        if (head == null) {
            head = tail;
        }
    }
}