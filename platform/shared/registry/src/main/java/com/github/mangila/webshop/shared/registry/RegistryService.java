package com.github.mangila.webshop.shared.registry;

import com.github.benmanes.caffeine.cache.Cache;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import com.github.mangila.webshop.shared.util.CacheName;
import com.github.mangila.webshop.shared.util.Ensure;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private static final Logger log = LoggerFactory.getLogger(RegistryService.class);
    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    @SuppressWarnings("unchecked")
    public RegistryService(CacheManager cacheManager) {
        this.domainRegistry = new DomainRegistry(
                (Cache<Domain, String>) cacheManager.getCache(CacheName.DOMAIN_REGISTRY)
                        .getNativeCache()
        );
        this.eventRegistry = new EventRegistry(
                (Cache<Event, String>) cacheManager.getCache(CacheName.EVENT_REGISTRY)
                        .getNativeCache()
        );
    }

    public void registerDomain(Domain domain) {
        log.info("Registering domain: {}", domain.value());
        domainRegistry.register(domain, domain.value());
    }

    public void registerEvent(Event event) {
        log.info("Registering event: {}", event.value());
        eventRegistry.register(event, event.value());
    }

    public boolean isRegistered(Event event) {
        return eventRegistry.isRegistered(event);
    }

    public boolean isRegistered(Domain domain) {
        return domainRegistry.isRegistered(domain);
    }

    public void ensureIsRegistered(Event event) {
        Ensure.isTrue(eventRegistry.isRegistered(event), "Event %s is not registered".formatted(event));
    }

    public void ensureIsRegistered(Domain domain) {
        Ensure.isTrue(domainRegistry.isRegistered(domain), "Domain %s is not registered".formatted(domain));
    }
}
