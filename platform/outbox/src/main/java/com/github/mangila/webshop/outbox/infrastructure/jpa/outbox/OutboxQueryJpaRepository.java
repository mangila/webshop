package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;


import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdByStatusQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.query.FindAllOutboxIdsByDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class OutboxQueryJpaRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository entityRepository;

    public OutboxQueryJpaRepository(OutboxEntityQueryRepository entityRepository) {
        this.entityRepository = entityRepository;
    }

    @Override
    public List<OutboxId> findAllIdsByDomainAndStatus(FindAllOutboxIdsByDomainAndStatusQuery query) {
        return entityRepository.findAllIdsByDomainAndStatus(
                        query.domain().value(),
                        query.status(),
                        query.limit())
                .stream()
                .map(OutboxId::new)
                .toList();
    }

    @Override
    public List<OutboxId> findAllIdsByStatus(FindAllOutboxIdByStatusQuery query) {
        return entityRepository.findAllIdsByStatusAndDateBefore(
                        query.status(),
                        query.limit())
                .stream()
                .map(OutboxId::new)
                .toList();
    }
}
