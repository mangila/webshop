package com.github.mangila.webshop.backend.outboxevent.application.gateway;

import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventCommandService;
import com.github.mangila.webshop.backend.outboxevent.application.service.OutboxEventQueryService;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventServiceGateway {

    private final OutboxEventCommandService command;
    private final OutboxEventQueryService query;

    public OutboxEventServiceGateway(OutboxEventCommandService command, OutboxEventQueryService query) {
        this.command = command;
        this.query = query;
    }

    public OutboxEventCommandService command() {
        return command;
    }

    public OutboxEventQueryService query() {
        return query;
    }

}
