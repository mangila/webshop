package com.github.mangila.webshop.backend.event.application.registry;

import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
public class DefaultEventTypeRegistry implements EventRegistry {

    private final Map<String, String> registry;

    public DefaultEventTypeRegistry(@Qualifier("eventTypeRegistryMap") Map<String, String> registry) {
        this.registry = registry;
    }

    @Override
    public boolean isRegistered(String key) {
        return registry.containsKey(key);
    }

    @Override
    public void register(String key, String value) {
        registry.put(key, value);
    }
}
