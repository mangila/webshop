package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.OutboxId;
import com.github.mangila.webshop.shared.outbox.domain.OutboxQueryRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OutboxJpaQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityMapper mapper;
    private final OutboxEntityQueryRepository repository;

    public OutboxJpaQueryRepository(OutboxEntityMapper mapper,
                                    OutboxEntityQueryRepository repository) {
        this.mapper = mapper;
        this.repository = repository;
    }

    @Override
    public Optional<Outbox> findById(OutboxId id) {
        return repository.findById(id.value())
                .map(mapper::toDomain);
    }
}
