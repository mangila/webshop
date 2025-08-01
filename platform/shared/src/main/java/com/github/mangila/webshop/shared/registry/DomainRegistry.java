package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.model.CacheName;
import com.github.mangila.webshop.shared.model.Domain;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Objects;

@Component
public final class DomainRegistry implements Registry<Domain, String> {

    private static final Logger log = LoggerFactory.getLogger(DomainRegistry.class);
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
