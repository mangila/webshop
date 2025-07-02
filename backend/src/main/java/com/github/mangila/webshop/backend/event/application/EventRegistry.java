package com.github.mangila.webshop.backend.event.application;

public interface EventRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);
}
