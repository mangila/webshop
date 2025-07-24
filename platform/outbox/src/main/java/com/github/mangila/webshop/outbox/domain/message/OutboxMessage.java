package com.github.mangila.webshop.outbox.domain.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.registry.model.Domain;
import com.github.mangila.webshop.shared.registry.model.Event;

public record OutboxMessage(OutboxId id, OutboxAggregateId aggregateId, Domain domain, Event event) {
}
