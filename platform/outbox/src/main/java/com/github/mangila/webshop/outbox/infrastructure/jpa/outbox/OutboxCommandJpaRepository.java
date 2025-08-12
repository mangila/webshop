package com.github.mangila.webshop.outbox.infrastructure.jpa.outbox;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.command.*;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.infrastructure.jpa.sequence.OutboxSequenceEntity;
import com.github.mangila.webshop.outbox.infrastructure.jpa.sequence.OutboxSequenceEntityCommandRepository;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

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
    public Outbox insert(CreateOutboxCommand command) {
        return Stream.of(command)
                .map(mapper::toEntity)
                .map(entityCommandRepository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public Optional<Outbox> findForUpdate(FindOutboxForUpdateCommand command) {
        return entityCommandRepository.findByIdForUpdate(command.id().value())
                .map(mapper::toDomain);
    }

    @Override
    public void updateStatus(UpdateOutboxStatusCommand command) {
        entityCommandRepository.updateStatus(
                command.id().value(),
                command.outboxStatusType(),
                command.outboxUpdated().value()
        );
    }

    @Override
    public Optional<OutboxSequence> findCurrentSequence(OutboxAggregateId aggregateId) {
        return sequenceRepository.findAndLockByAggregateId(aggregateId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updateSequence(UpdateOutboxSequenceCommand command) {
        OutboxSequenceEntity entity = mapper.toEntity(command.sequence());
        sequenceRepository.save(entity);
    }

    @Override
    public void delete(DeleteOutboxCommand command) {
        entityCommandRepository.deleteById(command.id().value());
    }
}
