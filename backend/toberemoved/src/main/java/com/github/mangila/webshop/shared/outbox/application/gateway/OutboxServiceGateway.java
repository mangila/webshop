package com.github.mangila.webshop.shared.outbox.application.gateway;

import com.github.mangila.webshop.shared.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.shared.outbox.application.service.OutboxQueryService;
import org.springframework.stereotype.Service;

@Service
public class OutboxServiceGateway {

    private final OutboxCommandService command;
    private final OutboxQueryService query;

    public OutboxServiceGateway(OutboxCommandService command,
                                OutboxQueryService query) {
        this.command = command;
        this.query = query;
    }

    public OutboxCommandService command() {
        return command;
    }

    public OutboxQueryService query() {
        return query;
    }

}
