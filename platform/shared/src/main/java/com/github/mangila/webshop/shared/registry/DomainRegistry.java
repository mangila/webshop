package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.model.Domain;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Map;

@Component
public final class DomainRegistry implements Registry<Domain, String> {
    private final Map<Domain, String> registry;

    public DomainRegistry(Map<Domain, String> domainToName) {
        this.registry = domainToName;
    }

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
