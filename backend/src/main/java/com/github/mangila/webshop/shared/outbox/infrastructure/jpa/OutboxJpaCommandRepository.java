package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxInsert;
import io.vavr.collection.Stream;
import org.springframework.stereotype.Repository;

@Repository
public class OutboxJpaCommandRepository implements OutboxCommandRepository {

    private final OutboxEntityMapper mapper;
    private final OutboxEntityCommandRepository repository;

    public OutboxJpaCommandRepository(OutboxEntityMapper mapper,
                                      OutboxEntityCommandRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Outbox insert(OutboxInsert command) {
        return Stream.of(command)
                .map(mapper::toEntity)
                .map(repository::save)
                .map(mapper::toDomain)
                .get();
    }

    @Override
    public void updateAsPublished(OutboxId id) {
        repository.updateAsPublished(id.value());
    }
}
