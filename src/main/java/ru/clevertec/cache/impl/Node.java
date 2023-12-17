package ru.clevertec.cache.impl;

public class Node<V> {

    long key;
    V value;
    Node<V> prev;
    Node<V> next;

    public Node(long key, V value) {
        this.key = key;
        this.value = value;
    }
}
