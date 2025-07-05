package com.github.mangila.webshop.shared.domain.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.domain.outbox.domain.OutboxCommandRepository;
import org.springframework.stereotype.Repository;

@Repository
public class DefaultOutboxEntityCommandRepository implements OutboxCommandRepository {

    private final OutboxEntityCommandRepository repository;

    public DefaultOutboxEntityCommandRepository(OutboxEntityCommandRepository repository) {
        this.repository = repository;
    }
}
