package com.github.mangila.webshop.shared.outbox.infrastructure.jpa;

import com.github.mangila.webshop.shared.domain.common.CqrsOperation;
import com.github.mangila.webshop.shared.domain.exception.CqrsException;
import com.github.mangila.webshop.shared.outbox.domain.Outbox;
import com.github.mangila.webshop.shared.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import io.micrometer.tracing.Span;
import io.micrometer.tracing.Tracer;
import io.vavr.collection.Stream;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Repository;

import javax.sql.DataSource;

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
    public Outbox findById(OutboxId id) {
        return Stream.of(id)
                .map(OutboxId::value)
                .map(repository::findById)
                .map(find -> {
                    if (find.isEmpty()) {
                        throw new CqrsException(
                                String.format("id not found: %s", id.value()),
                                CqrsOperation.QUERY,
                                Outbox.class
                        );
                    }
                    return find.get();
                })
                .map(mapper::toDomain)
                .get();
    }
}
