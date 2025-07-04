package com.github.mangila.webshop.backend.outboxevent.application.service;

import com.github.mangila.webshop.backend.common.error.exception.QueryException;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventRepositoryGateway;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.query.OutboxEventFindByIdQuery;
import com.github.mangila.webshop.backend.outboxevent.domain.query.OutboxEventReplayQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxEventQueryService {

    private final OutboxEventRepositoryGateway repositoryGateway;

    public OutboxEventQueryService(OutboxEventRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    public List<OutboxEvent> replay(OutboxEventReplayQuery query) {
        return List.of();
    }

    public OutboxEvent findById(OutboxEventFindByIdQuery query) {
        return repositoryGateway.query().findById(query.id())
                .orElseThrow(() -> new QueryException(
                        String.format("id not found: '%s'", query.id()),
                        query.getClass(),
                        OutboxEvent.class));
    }
}
