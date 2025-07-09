package com.github.mangila.webshop.shared.outbox.application.cqrs;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.application.registry.DomainKey;
import com.github.mangila.webshop.shared.application.registry.EventKey;

import java.util.UUID;

public record OutboxInsertCommand(
        DomainKey domain,
        EventKey event,
        UUID aggregateId,
        ObjectNode payload
) {
    public static OutboxInsertCommand from(Class<?> domain, Enum<?> event, UUID value, JsonNode jsonNode) {
        return new OutboxInsertCommand(DomainKey.create(domain), EventKey.create(event), value, (ObjectNode) jsonNode);
    }
}
