package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.application.registry.DomainRegistryService;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import io.micrometer.observation.annotation.Observed;
import io.vavr.collection.Stream;

@ObservedService
public class OutboxCommandService {

    private final OutboxMapperGateway mapper;
    private final OutboxRepositoryGateway repository;
    private final DomainRegistryService domainRegistryService;

    public OutboxCommandService(OutboxMapperGateway mapper,
                                OutboxRepositoryGateway repository,
                                DomainRegistryService domainRegistryService) {
        this.mapper = mapper;
        this.repository = repository;
        this.domainRegistryService = domainRegistryService;
    }

    public OutboxDto insert(OutboxInsertCommand command) {
        String topic = command.topic();
        String type = command.event();
        domainRegistryService.ensureHasTopicAndTypeRegistered(topic, type);
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
