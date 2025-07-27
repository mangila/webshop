package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.Domain;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public class DomainRegistry implements Registry<Domain, String> {

    private final Cache<Domain, String> registry;

    @SuppressWarnings("unchecked")
    public DomainRegistry(CacheManager cacheManager) {
        this.registry = (Cache<Domain, String>) cacheManager.getCache(CacheName.DOMAIN_REGISTRY)
                .getNativeCache();
    }

    @Override
    public boolean isRegistered(Domain key) {
        return Objects.nonNull(registry.getIfPresent(key));
    }

    @Override
    public void ensureIsRegistered(Domain key) {
        if (!isRegistered(key)) {
            throw new ApplicationException(String.format("Domain %s is not registered", key));
        }
    }

    @Override
    public String get(Domain key) {
        return registry.getIfPresent(key);
    }

    @Override
    public void register(Domain key, String value) {
        registry.put(key, value);
    }

    @Override
    public List<String> values() {
        return registry.asMap().values().stream().toList();
    }

    @Override
    public List<Domain> keys() {
        return registry.asMap().keySet().stream().toList();
    }
}
