package com.github.mangila.webshop.shared.application.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventRegistry implements Registry<EventKey, String> {

    private final Map<EventKey, String> registry = new ConcurrentHashMap<>();

    @Override
    public boolean isRegistered(EventKey key) {
        return registry.containsKey(key);
    }

    @Override
    public String get(EventKey key) {
        return registry.get(key);
    }

    @Override
    public void register(EventKey key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values().stream().toList();
    }

    @Override
    public List<EventKey> keys() {
        return registry.keySet().stream().toList();
    }
}
