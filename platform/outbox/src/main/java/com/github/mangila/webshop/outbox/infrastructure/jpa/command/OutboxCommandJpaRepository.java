package com.github.mangila.webshop.outbox.infrastructure.jpa.command;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxUpdateStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxSequenceEntity;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class OutboxCommandJpaRepository implements OutboxCommandRepository {

    private final OutboxEntityCommandRepository entityCommandRepository;
    private final OutboxSequenceEntityCommandRepository sequenceRepository;
    private final OutboxEntityMapper mapper;

    public OutboxCommandJpaRepository(OutboxEntityCommandRepository entityCommandRepository,
                                      OutboxSequenceEntityCommandRepository sequenceRepository,
                                      OutboxEntityMapper mapper) {
        this.entityCommandRepository = entityCommandRepository;
        this.sequenceRepository = sequenceRepository;
        this.mapper = mapper;
    }

    @Override
    public Outbox insert(OutboxInsertCommand command) {
        return Stream.of(command)
                .map(mapper::toEntity)
                .map(entityCommandRepository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public Optional<OutboxProjection> findByIdForUpdate(OutboxId id) {
        return entityCommandRepository.findByIdForUpdate(id.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updateStatus(OutboxUpdateStatusCommand command) {
        entityCommandRepository.updateStatus(
                command.id().value(),
                command.outboxStatusType().name(),
                command.outboxUpdated().value()
        );
    }

    @Override
    public Optional<OutboxSequence> findAndLockByAggregateId(OutboxAggregateId aggregateId) {
        return sequenceRepository.findAndLockByAggregateId(aggregateId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updateSequence(OutboxSequence outboxSequence) {
        OutboxSequenceEntity entity = mapper.toEntity(outboxSequence);
        sequenceRepository.save(entity);
    }

    @Override
    public void deleteByIds(List<OutboxId> ids) {
        entityCommandRepository.deleteAllById(ids
                .stream()
                .map(OutboxId::value)
                .toList());
    }
}
