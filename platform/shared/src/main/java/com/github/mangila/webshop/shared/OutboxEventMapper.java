package com.github.mangila.webshop.shared;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.identity.application.IdentityService;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import com.github.mangila.webshop.shared.registry.DomainRegistry;
import com.github.mangila.webshop.shared.registry.EventRegistry;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
public class OutboxEventMapper {

    private final DomainRegistry domainRegistry;
    private final EventRegistry eventRegistry;
    private final IdentityService identityService;

    public OutboxEventMapper(DomainRegistry domainRegistry,
                             EventRegistry eventRegistry,
                             IdentityService identityService) {
        this.domainRegistry = domainRegistry;
        this.eventRegistry = eventRegistry;
        this.identityService = identityService;
    }

    public OutboxEvent toEvent(Domain domain,
                               Event event,
                               UUID aggregateId,
                               ObjectNode payload) {
        domainRegistry.ensureIsRegistered(domain);
        eventRegistry.ensureIsRegistered(event);
        identityService.ensureHasGenerated(aggregateId);
        return new OutboxEvent(
                domain,
                event,
                aggregateId,
                payload
        );
    }
}
