package com.github.mangila.webshop.shared.outbox.domain.message;

import com.github.mangila.webshop.shared.application.registry.Domain;
import com.github.mangila.webshop.shared.application.registry.Event;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;

public record OutboxMessage(OutboxId id,
                            OutboxAggregateId aggregateId,
                            Domain domain,
                            Event event) {
}
