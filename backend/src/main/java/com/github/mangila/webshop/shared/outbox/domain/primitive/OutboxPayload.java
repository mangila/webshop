package com.github.mangila.webshop.shared.outbox.domain.primitive;

import com.fasterxml.jackson.databind.JsonNode;

public record OutboxPayload(JsonNode value) {
}
