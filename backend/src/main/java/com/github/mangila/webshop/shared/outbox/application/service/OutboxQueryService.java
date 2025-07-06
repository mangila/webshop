package com.github.mangila.webshop.shared.outbox.application.service;

import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxIdQuery;
import com.github.mangila.webshop.shared.outbox.application.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.shared.outbox.application.dto.OutboxDto;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxMapperGateway;
import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxRepositoryGateway;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxQueryService {

    private final OutboxMapperGateway mapper;
    private final OutboxRepositoryGateway repository;

    public OutboxQueryService(OutboxMapperGateway mapper,
                              OutboxRepositoryGateway repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    public List<OutboxDto> replay(OutboxReplayQuery query) {
        return List.of();
    }

    public OutboxDto findById(OutboxIdQuery query) {
        return Stream.of(query)
                .map(mapper.query()::toDomain)
                .map(repository.query()::findById)
                .map(outbox -> {
                    if (outbox.isEmpty()) {
                        throw new CqrsException(
                                String.format("id not found: '%s'", query.id()),
                                CqrsOperation.QUERY,
                                Outbox.class);
                    }
                    return outbox.get();
                })
                .map(mapper.dto()::toDto)
                .get();
    }
}
