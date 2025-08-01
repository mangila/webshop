package com.github.mangila.webshop.outbox.application.graphql;

import com.github.mangila.webshop.outbox.application.graphql.input.OutboxReplayInput;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import org.springframework.stereotype.Component;

@Component
public class OutboxInputMapper {
    public OutboxReplayQuery toQuery(OutboxReplayInput request) {
        return new OutboxReplayQuery(
                OutboxSequence.from(request.aggregateId(), request.sequence()),
                request.limit()
        );
    }
}
