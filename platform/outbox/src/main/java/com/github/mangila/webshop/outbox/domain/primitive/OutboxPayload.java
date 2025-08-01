package com.github.mangila.webshop.outbox.domain.primitive;

import com.fasterxml.jackson.databind.node.ObjectNode;
import com.github.mangila.webshop.shared.Ensure;

public record OutboxPayload(ObjectNode value) {
    public OutboxPayload {
        Ensure.notNull(value, ObjectNode.class);
    }
}
