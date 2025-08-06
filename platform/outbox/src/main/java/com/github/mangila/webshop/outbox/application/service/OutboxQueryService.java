package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxDomainAndStatusQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxStatusAndDateBeforeQuery;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import jakarta.validation.constraints.NotNull;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import java.util.List;

@Service
@Validated
public class OutboxQueryService {

    private final OutboxQueryRepository repository;

    public OutboxQueryService(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    public List<Outbox> replay(@NotNull OutboxReplayQuery query) {
        return repository.replay(query);
    }

    public List<OutboxProjection> findAllByDomainAndStatus(@NotNull OutboxDomainAndStatusQuery query) {
        return repository.findAllByDomainAndStatus(query);
    }

    public List<OutboxId> findAllIdsByStatusAndDateBefore(@NotNull OutboxStatusAndDateBeforeQuery query) {
        return repository.findAllIdsByStatusAndDateBefore(query);
    }
}
