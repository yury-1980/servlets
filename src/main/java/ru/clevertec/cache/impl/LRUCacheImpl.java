package ru.clevertec.cache.impl;

import lombok.AllArgsConstructor;
import lombok.Data;
import ru.clevertec.cache.LRUCache;

import java.util.HashMap;
import java.util.Map;

@AllArgsConstructor
@Data
public class LRUCacheImpl<V> implements LRUCache<Long, V> {

    private Node<V> head;
    private Node<V> tail;
    private Map<Long, Node<V>> map;
    private int capacity;

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

        return item.getValue();
    }

    @Override
    public void put(Long key, V value) {
        if (map.containsKey(key)) {
            Node<V> item = map.get(key);
            item.setValue(value);

            // Перемещаем в конец
            removeNode(item);
            addToTail(item);
        } else {
            if (map.size() >= capacity) {
                // Удаляем голову
                map.remove(head.getKey());
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
        if (node.getPrev() != null) {
            node.getPrev()
                    .setNext(node.getNext());
        } else {
            head = node.getNext();
        }

        if (node.getNext() != null) {
            node.getNext()
                    .setPrev(node.getPrev());
        } else {
            tail = node.getPrev();
        }
    }

    private void addToTail(Node<V> node) {
        if (tail != null) {
            tail.setNext(node);
        }

        node.setPrev(tail);
        node.setNext(null);
        tail = node;

        if (head == null) {
            head = tail;
        }
    }
}