package com.github.mangila.webshop.backend.event.application.registry;

public interface EventRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);
}
