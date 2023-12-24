package ru.clevertec.cache.impl;

import lombok.Data;

@Data
public class Node<V> {

    private long key;
    private V value;
    private Node<V> prev;
    private Node<V> next;

    public Node(long key, V value) {
        this.key = key;
        this.value = value;
    }
}
