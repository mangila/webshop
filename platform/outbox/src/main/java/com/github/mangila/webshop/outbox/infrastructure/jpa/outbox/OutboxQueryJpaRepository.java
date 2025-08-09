package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdsByStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
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
    public List<Outbox> findAllByDomainAndStatus(FindAllOutboxByDomainAndStatusQuery query) {
        return entityRepository.findAllByDomainAndStatus(
                        query.domain().value(),
                        query.status(),
                        query.limit())
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public List<OutboxId> findAllIdsByStatusAndDateBefore(FindAllOutboxIdsByStatusAndDateBeforeQuery query) {
        return entityRepository.findAllIdsByStatusAndDateBefore(
                        query.status(),
                        query.date(),
                        query.limit())
                .stream()
                .map(OutboxId::new)
                .toList();
    }
}
