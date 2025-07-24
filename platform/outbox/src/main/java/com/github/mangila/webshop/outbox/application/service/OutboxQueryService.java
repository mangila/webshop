package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.shared.annotation.ObservedService;

import java.util.List;

@ObservedService
public class OutboxQueryService {

    private final OutboxQueryRepository repository;

    public OutboxQueryService(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    public List<Outbox> replay(OutboxReplayQuery query) {
        return repository.replay(query);
    }
}
