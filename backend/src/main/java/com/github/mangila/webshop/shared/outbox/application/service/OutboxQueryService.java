package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdQuery;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import io.micrometer.observation.annotation.Observed;
import io.vavr.collection.Stream;

import java.util.List;

@ObservedService
public class OutboxQueryService {

    private final OutboxMapperGateway mapper;
    private final OutboxRepositoryGateway repository;

    public OutboxQueryService(OutboxMapperGateway mapper,
                              OutboxRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Observed
    public List<OutboxDto> replay(OutboxReplayQuery query) {
        return List.of();
    }

    @Observed
    public OutboxDto findById(OutboxIdQuery query) {
        return Stream.of(query)
                .map(mapper.query()::toDomain)
                .map(repository.query()::findById)
                .map(mapper.dto()::toDto)
                .get();
    }
}
