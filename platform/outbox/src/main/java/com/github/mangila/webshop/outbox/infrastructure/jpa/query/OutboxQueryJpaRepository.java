package com.github.mangila.webshop.outbox.infrastructure.jpa.query;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxQueryJpaRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository entityRepository;
    private final OutboxEntityMapper mapper;

    public OutboxQueryJpaRepository(OutboxEntityQueryRepository entityRepository,
                                    OutboxEntityMapper mapper) {
        this.entityRepository = entityRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Outbox> replay(OutboxReplayQuery query) {
        return entityRepository.replay(
                        query.sequence().aggregateId().value(),
                        query.sequence().value(),
                        query.limit()
                ).stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<OutboxId> findAllIdsByStatus(OutboxStatusType status, int limit) {
        return entityRepository.findAllIdsByStatus(status, limit)
                .stream()
                .map(OutboxId::new)
                .toList();
    }
}
