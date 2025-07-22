package com.github.mangila.webshop.outbox.infrastructure.jpa;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.exception.CqrsException;

import java.util.List;

public class OutboxJpaCommandRepository implements OutboxCommandRepository {

    private final OutboxEntityCommandRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaCommandRepository(OutboxEntityCommandRepository jpaRepository,
                                      OutboxEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Outbox insert(OutboxInsertCommand command) {
        var entity = jpaRepository.save(mapper.toEntity(command));
        return mapper.toDomain(entity);
    }

    @Override
    public OutboxMessage findByIdForUpdateOrThrow(OutboxId id) throws CqrsException {
        var projection = jpaRepository.findProjectionByIdForUpdate(id.value());
        return mapper.toDomain(projection);
    }

    @Override
    public void updateAsPublished(OutboxId id, OutboxPublished published) {
        jpaRepository.updateAsPublished(id.value(), published.value());
    }

    @Override
    public List<OutboxMessage> findAllByPublishedForUpdate(OutboxPublished published, int limit) {
        var projections = jpaRepository.findAllProjectionsByPublishedForUpdate(published.value(), limit);
        return mapper.toDomain(projections);
    }
}
