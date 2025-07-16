package com.github.mangila.webshop.shared.outbox.domain.primitive;

import com.fasterxml.jackson.databind.node.ObjectNode;

public record OutboxPayload(ObjectNode value) {
}
