package com.github.mangila.webshop.outbox.infrastructure.jpa;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import io.vavr.collection.Stream;

import java.util.List;

public class OutboxJpaQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaQueryRepository(OutboxEntityQueryRepository jpaRepository,
                                    OutboxEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Outbox findByIdOrThrow(OutboxId id) throws CqrsException {
        return Stream.of(id)
                .map(OutboxId::value)
                .map(jpaRepository::findById)
                .map(find -> {
                    if (find.isEmpty()) {
                        throw new CqrsException(
                                String.format("id not found: %s", id.value()),
                                CqrsOperation.QUERY,
                                Outbox.class
                        );
                    }
                    return find.get();
                })
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public List<OutboxMessage> findAllByPublished(OutboxPublished published, int limit) {
        return List.of();
    }
}
