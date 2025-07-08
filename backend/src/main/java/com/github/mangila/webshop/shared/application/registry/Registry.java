package com.github.mangila.webshop.shared.application.registry;

interface Registry<K, V> {

    boolean isRegistered(K key);

    V get(K key);

    void register(K key, V value);
}
