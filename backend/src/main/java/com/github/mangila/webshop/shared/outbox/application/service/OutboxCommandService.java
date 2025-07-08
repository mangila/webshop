package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.application.registry.DomainRegistryService;
import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdCommand;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import io.vavr.collection.Stream;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
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

    public OutboxDto insert(@Valid OutboxInsertCommand command) {
        String topic = command.topic();
        String type = command.event();
        domainRegistryService.ensureHasTopicAndTypeRegistered(topic, type);
        return Stream.of(command)
                .map(mapper.command()::toDomain)
                .map(repository.command()::insert)
                .map(mapper.dto()::toDto)
                .get();
    }

    public void updateAsPublished(@Valid OutboxIdCommand command) {
        var id = mapper.command().toDomain(command);
        repository.command().updateAsPublished(id);
    }
}
