package com.github.mangila.webshop.outbox.application.service;

import com.github.mangila.webshop.outbox.application.mapper.OutboxEventMapper;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.infrastructure.message.InternalMessageQueue;
import com.github.mangila.webshop.shared.model.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
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
        repository.findSequenceAndLockByAggregateId(new OutboxAggregateId(event.aggregateId()))
                .ifPresentOrElse(
                        currentSequence -> tryOutbox(event, OutboxSequence.incrementFrom(currentSequence)),
                        () -> tryOutbox(event, OutboxSequence.initial(event.aggregateId()))
                );
    }

    private void tryOutbox(DomainEvent event, OutboxSequence newSequence) {
        OutboxInsertCommand command = mapper.toCommand(event, newSequence);
        Outbox outbox = repository.insert(command);
        repository.updateNewSequence(newSequence);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        internalMessageQueue.add(outbox.id());
                    }
                }
        );
    }
}
