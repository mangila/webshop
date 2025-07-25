package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxReplayQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OutboxQueryService {

    private final OutboxQueryRepository repository;

    public OutboxQueryService(OutboxQueryRepository repository) {
        this.repository = repository;
    }

    public List<Outbox> replay(OutboxReplayQuery query) {
        return repository.replay(query);
    }
}
