package com.github.mangila.webshop.outbox.infrastructure.jpa.query;


import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.outbox.infrastructure.jpa.OutboxEntityMapper;
import com.github.mangila.webshop.shared.annotation.ObservedRepository;
import org.springframework.data.domain.Limit;
import org.springframework.data.domain.Sort;

import java.util.List;

@ObservedRepository
public class OutboxJpaQueryRepository implements OutboxQueryRepository {

    private final OutboxEntityQueryRepository jpaRepository;
    private final OutboxEntityMapper mapper;

    public OutboxJpaQueryRepository(OutboxEntityQueryRepository jpaRepository,
                                    OutboxEntityMapper mapper) {
        this.jpaRepository = jpaRepository;
        this.mapper = mapper;
    }

    @Override
    public List<Outbox> replay(OutboxReplayQuery query) {
        return List.of();
    }

    @Override
    public List<OutboxMessage> findAllByPublished(OutboxPublished published, int limit) {
        return jpaRepository.findAllByPublished(
                        published.value(),
                        Sort.by(Sort.Direction.ASC, "created"),
                        Limit.of(limit))
                .stream()
                .map(mapper::toDomain)
                .toList();
    }
}
