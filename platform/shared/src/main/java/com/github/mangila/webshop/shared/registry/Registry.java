package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.Ensure;

import java.util.List;

sealed interface Registry<K, V> permits DomainRegistry, EventRegistry {

    boolean isRegistered(K key);

    default void ensureIsRegistered(K key) {
        Ensure.isTrue(isRegistered(key), "%s is not registered".formatted(key));
    }

    V get(K key);

    void register(K key, V value);

    List<V> values();

    List<K> keys();

}
