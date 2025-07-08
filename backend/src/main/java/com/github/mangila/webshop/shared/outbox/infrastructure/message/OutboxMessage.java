package com.github.mangila.webshop.shared.outbox.infrastructure.message;

import java.util.UUID;

public record OutboxMessage(long id, UUID aggregateId, String domain, String event) {
}
