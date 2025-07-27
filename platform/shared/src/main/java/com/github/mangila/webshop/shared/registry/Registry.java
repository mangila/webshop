package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.exception.ApplicationException;

import java.util.List;

public interface Registry<K, V> {

    boolean isRegistered(K key);

    void ensureIsRegistered(K key);

    V get(K key);

    void register(K key, V value);

    List<V> values();

    List<K> keys();

}
