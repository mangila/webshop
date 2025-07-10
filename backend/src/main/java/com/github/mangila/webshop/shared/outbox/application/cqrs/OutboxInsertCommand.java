package com.github.mangila.webshop.shared.outbox.application.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.RegistredDomain;
import com.github.mangila.webshop.shared.infrastructure.spring.validation.RegistredEvent;

import java.util.UUID;

public record OutboxInsertCommand(
        @RegistredDomain Domain domain,
        @RegistredEvent Event event,
        UUID aggregateId,
        ObjectNode payload
) {
    public static OutboxInsertCommand from(Class<?> domain, Enum<?> event, UUID value, ObjectNode payload) {
        return new OutboxInsertCommand(Domain.from(domain), Event.from(event), value, payload);
    }
}
