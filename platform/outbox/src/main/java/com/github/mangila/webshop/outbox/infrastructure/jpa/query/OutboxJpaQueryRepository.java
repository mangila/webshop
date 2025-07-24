package com.github.mangila.webshop.outbox.infrastructure.jpa.query;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;

import java.util.List;
import java.util.Optional;

@ObservedRepository
public class OutboxJpaQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaQueryRepository(OutboxEntityQueryRepository jpaRepository,
                                    OutboxEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Outbox> replay(OutboxReplayQuery query) {
        return List.of();
    }
}
