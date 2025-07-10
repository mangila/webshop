package com.github.mangila.webshop.shared.application.registry;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;

import java.util.List;

@ObservedService
public class RegistryService {

    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public RegistryService() {
        this.domainRegistry = new DomainRegistry();
        this.eventRegistry = new EventRegistry();
    }

    public void ensureEventIsRegistered(EventKey key) {
        if (!eventRegistry.isRegistered(key)) {
            throw new ApplicationException(String.format("Event is not registered: '%s'", key.value()));
        }
    }

    public void registerEvent(EventKey key, String value) {
        eventRegistry.register(key, value);
    }

    public void ensureDomainIsRegistered(DomainKey domainKey) {
        if (!domainRegistry.isRegistered(domainKey)) {
            throw new ApplicationException(String.format("Domain is not registered: '%s'", domainKey.value()));
        }
    }

    public void registerDomain(DomainKey domainKey, String value) {
        domainRegistry.register(domainKey, value);
    }

    public List<DomainKey> domainKeys() {
        return domainRegistry.keys();
    }
}
