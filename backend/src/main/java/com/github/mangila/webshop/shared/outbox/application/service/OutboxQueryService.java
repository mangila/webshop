package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.infrastructure.spring.annotation.ObservedService;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdQuery;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import io.vavr.collection.Stream;
import jakarta.validation.Valid;
import org.springframework.validation.annotation.Validated;

@Validated
@ObservedService
public class OutboxQueryService {

    private final OutboxMapperGateway mapper;
    private final OutboxRepositoryGateway repository;

    public OutboxQueryService(OutboxMapperGateway mapper,
                              OutboxRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public OutboxDto findById(@Valid OutboxIdQuery query) {
        return Stream.of(query)
                .map(mapper.query()::toDomain)
                .map(repository.query()::findByIdOrThrow)
                .map(mapper.dto()::toDto)
                .get();
    }
}
