package com.github.mangila.webshop.shared.registry;

import com.github.mangila.webshop.shared.exception.ApplicationException;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import org.springframework.stereotype.Service;

@Service
public class RegistryService {

    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;

    public RegistryService(DomainRegistry domainRegistry,
                           EventRegistry eventRegistry) {
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
    }

    public void ensureIsRegistered(Event event) {
        if (!eventRegistry.isRegistered(event)) {
            throw new ApplicationException(String.format("Event %s is not registered", event));
        }
    }

    public void ensureIsRegistered(Domain domain) {
        if (!domainRegistry.isRegistered(domain)) {
            throw new ApplicationException(String.format("Domain %s is not registered", domain));
        }
    }

    public void registerDomain(Domain domain) {
        domainRegistry.register(domain, domain.value());
    }

    public void registerEvent(Event event) {
        eventRegistry.register(event, event.value());
    }

    public boolean isRegistered(Event value) {
        return eventRegistry.isRegistered(value);
    }

    public boolean isRegistered(Domain value) {
        return domainRegistry.isRegistered(value);
    }
}
