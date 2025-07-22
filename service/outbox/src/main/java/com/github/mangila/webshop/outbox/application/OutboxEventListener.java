package com.github.mangila.webshop.outbox.application;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.infrastructure.message.MessageQueue;
import com.github.mangila.webshop.shared.event.DomainEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Service
public class OutboxEventListener {

    private final MessageQueue messageQueue;
    private final OutboxEventMapper mapper;
    private final OutboxCommandRepository repository;

    public OutboxEventListener(MessageQueue messageQueue,
                               OutboxEventMapper mapper,
                               OutboxCommandRepository repository) {
        this.messageQueue = messageQueue;
        this.mapper = mapper;
        this.repository = repository;
    }

    @EventListener
    public void listen(DomainEvent event) {
        Outbox outbox = repository.insert(mapper.toCommand(event));
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        messageQueue.add(outbox.getId());
                    }
                }
        );
    }
}
