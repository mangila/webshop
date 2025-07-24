package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.application.mapper.OutboxEventMapper;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.infrastructure.message.InternalMessageQueue;
import com.github.mangila.webshop.shared.annotation.ObservedService;
import com.github.mangila.webshop.shared.event.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@ObservedService
public class OutboxEventListener {

    private final InternalMessageQueue internalMessageQueue;
    private final OutboxEventMapper mapper;
    private final OutboxCommandRepository repository;

    public OutboxEventListener(InternalMessageQueue internalMessageQueue,
                               OutboxEventMapper mapper,
                               OutboxCommandRepository repository) {
        this.internalMessageQueue = internalMessageQueue;
        this.mapper = mapper;
        this.repository = repository;
    }

    @EventListener
    public void listen(DomainEvent event) {
        OutboxInsertCommand command = mapper.toCommand(event);
        Outbox outbox = repository.insert(command);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        internalMessageQueue.add(outbox.getId());
                    }
                }
        );
    }
}
