package com.github.mangila.webshop.shared.event;

import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public record DomainEvent(
        Domain domain,
        Event event,
        UUID aggregateId,
        ObjectNode payload
) {
}
