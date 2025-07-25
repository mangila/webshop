package com.github.mangila.webshop.outbox.infrastructure.jpa.command;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxSequenceEntity;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;
import io.vavr.collection.Stream;

import java.util.List;
import java.util.Optional;

@ObservedRepository
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
    public Optional<OutboxMessage> findByIdAndPublishedForUpdate(OutboxId id, OutboxPublished published) {
        return entityCommandRepository.findByIdAndPublishedForUpdate(id.value(), published.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updatePublished(OutboxId id, OutboxPublished published) {
        entityCommandRepository.updatePublished(id.value(), published.value());
    }

    @Override
    public List<OutboxMessage> findAllByPublishedForUpdate(OutboxPublished published, int limit) {
        return entityCommandRepository.findByPublishedForUpdate(published.value(), limit)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }

    @Override
    public Optional<OutboxSequence> findSequenceAndLockByAggregateId(OutboxAggregateId aggregateId) {
        return sequenceRepository.findAndLockByAggregateId(aggregateId.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updateNewSequence(OutboxSequence outboxSequence) {
        OutboxSequenceEntity entity = mapper.toEntity(outboxSequence);
        sequenceRepository.save(entity);
    }
}
