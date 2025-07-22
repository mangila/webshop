package com.github.mangila.webshop.outbox.infrastructure.jpa.query;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.shared.exception.CqrsException;
import com.github.mangila.webshop.shared.model.CqrsOperation;
import org.springframework.stereotype.Repository;

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
    public Outbox findByIdOrThrow(OutboxId id) throws CqrsException {
        var projection = jpaRepository.findById(id.value());
        if (projection.isEmpty()) {
            throw new CqrsException(String.format("Id not found: %s",
                    id.value()),
                    CqrsOperation.COMMAND,
                    Outbox.class);
        }
        return mapper.toDomain(projection.get());
    }
}
