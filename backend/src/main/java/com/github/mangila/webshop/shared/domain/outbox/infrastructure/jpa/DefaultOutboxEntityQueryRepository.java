package com.github.mangila.webshop.shared.domain.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.domain.outbox.domain.OutboxQueryRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultOutboxEntityQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository repository;

    public DefaultOutboxEntityQueryRepository(OutboxEntityQueryRepository repository) {
        this.repository = repository;
    }
}
