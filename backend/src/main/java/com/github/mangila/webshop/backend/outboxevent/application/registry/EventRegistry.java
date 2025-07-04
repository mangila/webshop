package com.github.mangila.webshop.backend.outboxevent.application.registry;

import java.util.List;

public interface EventRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);

    List<String> values();
}
