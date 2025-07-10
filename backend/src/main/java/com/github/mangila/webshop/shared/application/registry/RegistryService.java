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

    public void ensureIsRegistered(Event key) {
        if (!eventRegistry.isRegistered(key)) {
            throw new ApplicationException(String.format("Event is not registered: '%s'", key.value()));
        }
    }

    public void register(Event event) {
        eventRegistry.register(event, event.value());
    }

    public void ensureIsRegistered(Domain domain) {
        if (!domainRegistry.isRegistered(domain)) {
            throw new ApplicationException(String.format("Domain is not registered: '%s'", domain.value()));
        }
    }

    public void register(Domain domain) {
        domainRegistry.register(domain, domain.value());
    }

    public List<Domain> domains() {
        return domainRegistry.keys();
    }

    public boolean isRegistered(Domain domain) {
        return domainRegistry.isRegistered(domain);
    }

    public boolean isRegistered(Event event) {
        return eventRegistry.isRegistered(event);
    }
}
