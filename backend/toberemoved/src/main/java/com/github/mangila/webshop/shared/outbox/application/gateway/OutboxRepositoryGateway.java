package com.github.mangila.webshop.shared.outbox.application.gateway;

import com.github.mangila.webshop.shared.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.shared.outbox.domain.OutboxQueryRepository;
import org.springframework.stereotype.Service;

@Service
public class OutboxRepositoryGateway {

    private final OutboxCommandRepository command;
    private final OutboxQueryRepository query;

    public OutboxRepositoryGateway(OutboxCommandRepository command,
                                   OutboxQueryRepository query) {
        this.command = command;
        this.query = query;
    }

    public OutboxCommandRepository command() {
        return command;
    }

    public OutboxQueryRepository query() {
        return query;
    }
}
