package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.shared.outbox.domain.cqrs.OutboxFindById;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class DefaultOutboxEntityQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityMapper mapper;
    private final OutboxEntityQueryRepository repository;

    public DefaultOutboxEntityQueryRepository(OutboxEntityMapper mapper,
                                              OutboxEntityQueryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Optional<Outbox> findById(OutboxFindById query) {
        return repository.findById(query.id())
                .map(mapper::toDomain);
    }
}
