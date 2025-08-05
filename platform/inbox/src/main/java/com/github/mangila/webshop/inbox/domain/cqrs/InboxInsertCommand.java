package com.github.mangila.webshop.inbox.domain.cqrs;

import com.github.mangila.webshop.inbox.domain.primitive.InboxAggregateId;
import com.github.mangila.webshop.inbox.domain.primitive.InboxPayload;
import com.github.mangila.webshop.inbox.domain.primitive.InboxSequence;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.Event;
import com.github.mangila.webshop.shared.model.EventSource;

public record InboxInsertCommand(
        InboxAggregateId aggregateId,
        Domain domain,
        Event event,
        InboxPayload payload,
        InboxSequence sequence,
        EventSource source
) {
}
