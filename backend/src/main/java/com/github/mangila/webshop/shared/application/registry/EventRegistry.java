package com.github.mangila.webshop.shared.application.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class EventRegistry implements Registry<Event, String> {

    private final Map<Event, String> registry = new ConcurrentHashMap<>();

    @Override
    public boolean isRegistered(Event key) {
        return registry.containsKey(key);
    }

    @Override
    public String get(Event key) {
        return registry.get(key);
    }

    @Override
    public void register(Event key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values().stream().toList();
    }

    @Override
    public List<Event> keys() {
        return registry.keySet().stream().toList();
    }
}
