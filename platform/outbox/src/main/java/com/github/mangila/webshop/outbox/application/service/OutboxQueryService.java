package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import com.github.mangila.webshop.shared.Ensure;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxQueryService {

    private final OutboxQueryRepository repository;

    public OutboxQueryService(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    public List<Outbox> replay(OutboxReplayQuery query) {
        Ensure.notNull(query, OutboxReplayQuery.class);
        return repository.replay(query);
    }
}
