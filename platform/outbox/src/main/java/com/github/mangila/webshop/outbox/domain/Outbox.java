
package com.github.mangila.webshop.outbox.domain;


import com.github.mangila.webshop.outbox.domain.primitive.*;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;

public record Outbox(
        OutboxId id,
        Domain domain,
        Event event,
        OutboxAggregateId aggregateId,
        OutboxPayload payload,
        OutboxPublished published,
        OutboxPublishedAt publishedAt,
        OutboxSequence sequence,
        OutboxCreated created) {
    public Outbox {
        Ensure.notNull(id, "OutboxId must not be null");
        Ensure.notNull(domain, "Domain must not be null");
        Ensure.notNull(event, "Event must not be null");
        Ensure.notNull(aggregateId, "OutboxAggregateId must not be null");
        Ensure.notNull(payload, "OutboxPayload must not be null");
        Ensure.notNull(published, "OutboxPublished must not be null");
        Ensure.notNull(publishedAt, "OutboxPublishedAt must not be null");
        Ensure.notNull(sequence, "OutboxSequence must not be null");
        Ensure.notNull(created, "OutboxCreated must not be null");
        if (published.value()) {
            Ensure.notNull(publishedAt.value(), "OutboxPublishedAt Instant must not be null");
        }
    }
}