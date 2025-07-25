package com.github.mangila.webshop.outbox.application.mapper;

import com.github.mangila.webshop.outbox.application.web.request.OutboxReplayRequest;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import org.springframework.stereotype.Component;

@Component
public class OutboxRequestMapper {
    public OutboxReplayQuery toQuery(OutboxReplayRequest request) {
        return new OutboxReplayQuery(
                OutboxSequence.from(request.aggregateId(), request.sequence()),
                request.limit()
        );
    }
}
