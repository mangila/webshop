package com.github.mangila.webshop.shared.outbox.infrastructure.registry;

import java.util.List;

public interface EventRegistry {

    boolean isRegistered(String key);

    void register(String key, String value);

    List<String> values();
}
