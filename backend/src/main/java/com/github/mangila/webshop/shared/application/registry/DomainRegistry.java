package com.github.mangila.webshop.shared.application.registry;

import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Service
public class DomainRegistry implements Registry<DomainKey, String> {

    private final Map<DomainKey, String> registry = new ConcurrentHashMap<>();

    @Override
    public boolean isRegistered(DomainKey key) {
        return registry.containsKey(key);
    }

    @Override
    public String get(DomainKey key) {
        return registry.get(key);
    }

    @Override
    public void register(DomainKey key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.values().stream().toList();
    }

    @Override
    public List<DomainKey> keys() {
        return registry.keySet().stream().toList();
    }
}
