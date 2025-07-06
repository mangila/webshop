package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRegistryGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Service;

@Service
public class OutboxCommandService {

    private final OutboxMapperGateway mapper;
    private final OutboxRepositoryGateway repository;
    private final OutboxRegistryGateway registry;

    public OutboxCommandService(OutboxMapperGateway mapper,
                                OutboxRepositoryGateway repository,
                                OutboxRegistryGateway registry) {
        this.mapper = mapper;
        this.repository = repository;
        this.registry = registry;
    }

    public OutboxDto insert(OutboxInsertCommand command) {
        String topic = command.topic();
        String type = command.event();
        registry.registry().ensureHasTopicAndTypeRegistered(topic, type);
        return Stream.of(command)
                .map(mapper.command()::toDomain)
                .map(repository.command()::insert)
                .map(mapper.dto()::toDto)
                .get();
    }

    public void updateAsPublished(OutboxId id) {
        repository.command().updateAsPublished(id);
    }
}
