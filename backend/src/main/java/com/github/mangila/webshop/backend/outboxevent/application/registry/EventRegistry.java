package com.github.mangila.webshop.backend.outboxevent.application.registry;

public interface EventRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);
}
