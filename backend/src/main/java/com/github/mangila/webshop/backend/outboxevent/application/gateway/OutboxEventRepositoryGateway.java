package com.github.mangila.webshop.backend.outboxevent.application.gateway;

import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventQueryService;
import com.github.mangila.webshop.backend.outboxevent.infrastructure.repository.OutboxEventCommandJpaRepository;
import com.github.mangila.webshop.backend.outboxevent.infrastructure.repository.OutboxEventQueryJpaRepository;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventRepositoryGateway {

    private final OutboxEventCommandJpaRepository command;
    private final OutboxEventQueryJpaRepository query;

    public OutboxEventRepositoryGateway(OutboxEventCommandJpaRepository command,
                                        OutboxEventQueryJpaRepository query) {
        this.command = command;
        this.query = query;
    }

    public OutboxEventCommandJpaRepository command() {
        return command;
    }

    public OutboxEventQueryJpaRepository query() {
        return query;
    }
}
