package com.github.mangila.webshop.outbox.infrastructure.jpa.command;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;
import io.vavr.collection.Stream;

import java.util.List;
import java.util.Optional;

@ObservedRepository
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
    public Optional<OutboxMessage> findByIdAndPublishedForUpdate(OutboxId id, OutboxPublished published) {
        return jpaRepository.findByIdAndPublishedForUpdate(id.value(), published.value())
                .map(mapper::toDomain);
    }

    @Override
    public void updatePublished(OutboxId id, OutboxPublished published) {
        jpaRepository.updatePublished(id.value(), published.value());
    }

    @Override
    public List<OutboxMessage> findAllByPublishedForUpdate(OutboxPublished published, int limit) {
        return jpaRepository.findByPublishedForUpdate(published.value(), limit)
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
