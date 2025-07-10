package com.github.mangila.webshop.shared.application.registry;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class DomainRegistry implements Registry<Domain, String> {

    private final Map<Domain, String> registry = new ConcurrentHashMap<>();

    @Override
    public boolean isRegistered(Domain key) {
        return registry.containsKey(key);
    }

    @Override
    public String get(Domain key) {
        return registry.get(key);
    }

    @Override
    public void register(Domain key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values().stream().toList();
    }

    @Override
    public List<Domain> keys() {
        return registry.keySet().stream().toList();
    }
}
