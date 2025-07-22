package com.github.mangila.webshop.outbox.infrastructure.jpa.command;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
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
        return Stream.of(command)
                .map(mapper::toEntity)
                .map(jpaRepository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public OutboxMessage findProjectionByIdAndPublishedFalseForUpdateOrThrow(OutboxId id) throws CqrsException {
        var projection = jpaRepository.findProjectionByIdAndPublishedFalseForUpdate(id.value());
        if (projection.isEmpty()) {
            throw new CqrsException(
                    String.format("Outbox with ID: %s - is not available or already published", id.value()),
                    CqrsOperation.COMMAND,
                    Outbox.class);

        }
        return mapper.toDomain(projection.get());
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
