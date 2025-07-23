package com.github.mangila.webshop.outbox.infrastructure.jpa.query;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public class OutboxJpaQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaQueryRepository(OutboxEntityQueryRepository jpaRepository,
                                    OutboxEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public Optional<Outbox> findById(OutboxId id) {
        return jpaRepository.findById(id.value())
                .map(mapper::toDomain);
    }
}
