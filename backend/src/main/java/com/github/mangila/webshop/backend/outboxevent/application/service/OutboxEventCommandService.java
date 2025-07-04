package com.github.mangila.webshop.backend.outboxevent.application.service;

import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventRegistryGateway;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventRepositoryGateway;
import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventServiceGateway;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEventInsertCommand;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventCommandService {

    private final OutboxEventRepositoryGateway outboxEventRepositoryGateway;
    private final OutboxEventRegistryGateway outboxEventRegistryGateway;

    public OutboxEventCommandService(OutboxEventRepositoryGateway outboxEventRepositoryGateway, OutboxEventRegistryGateway outboxEventRegistryGateway) {
        this.outboxEventRepositoryGateway = outboxEventRepositoryGateway;
        this.outboxEventRegistryGateway = outboxEventRegistryGateway;
    }

    public OutboxEvent insert(OutboxEventInsertCommand command) {
        return outboxEventRepositoryGateway.command().save(tryCreateEvent(command));
    }

    private OutboxEvent tryCreateEvent(OutboxEventInsertCommand command) {
        var topic = command.topic();
        var type = command.type();
        outboxEventRegistryGateway.registry().hasTopicAndTypeRegistered(topic, type);
        return OutboxEvent.from(command);
    }
}
