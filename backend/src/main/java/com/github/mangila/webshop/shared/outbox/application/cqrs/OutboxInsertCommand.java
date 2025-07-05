package com.github.mangila.webshop.shared.outbox.application.cqrs;

import com.fasterxml.jackson.databind.node.ObjectNode;

import java.util.UUID;

public record OutboxInsertCommand(
        String topic,
        String event,
        UUID aggregateId,
        ObjectNode payload
) {
}
