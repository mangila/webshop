package com.github.mangila.webshop.shared.event;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

import java.util.UUID;

public record DomainEvent(
        Domain domain,
        Event event,
        UUID aggregateId,
        ObjectNode payload
) {
}
