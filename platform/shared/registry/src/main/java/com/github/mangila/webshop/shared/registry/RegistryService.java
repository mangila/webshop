package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private static final Logger log = LoggerFactory.getLogger(RegistryService.class);
    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public RegistryService(DomainRegistry domainRegistry,
                           EventRegistry eventRegistry) {
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
    }

    public void ensureIsRegistered(Event event) {
        if (!eventRegistry.isRegistered(event)) {
            throw new IllegalStateException(String.format("Event %s is not registered", event));
        }
    }

    public void ensureIsRegistered(Domain domain) {
        if (!domainRegistry.isRegistered(domain)) {
            throw new IllegalStateException(String.format("Domain %s is not registered", domain));
        }
    }

    public void registerDomain(Domain domain) {
        log.info("Registering domain: {}", domain.value());
        domainRegistry.register(domain, domain.value());
    }

    public void registerEvent(Event event) {
        log.info("Registered event: {}", event.value());
        eventRegistry.register(event, event.value());
    }

    public boolean isRegistered(Event value) {
        return eventRegistry.isRegistered(value);
    }

    public boolean isRegistered(Domain value) {
        return domainRegistry.isRegistered(value);
    }
}
