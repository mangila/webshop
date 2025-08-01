package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.application.mapper.OutboxEventMapper;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxQueue;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.function.Consumer;

@Service
public class OutboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventListener.class);
    private final OutboxEventMapper mapper;
    private final OutboxCommandRepository repository;
    private final Map<Domain, OutboxQueue> domainQueues;

    public OutboxEventListener(Map<Domain, OutboxQueue> domainQueues,
                               OutboxEventMapper mapper,
                               OutboxCommandRepository repository) {
        this.domainQueues = domainQueues;
        this.mapper = mapper;
        this.repository = repository;
    }

    @EventListener
    public void listen(DomainEvent event) {
        var aggregateId = new OutboxAggregateId(event.aggregateId());
        repository.findCurrentSequenceAndLockByAggregateId(aggregateId).ifPresentOrElse(
                incrementCurrentSequence(event),
                createInitialSequence(event));
    }

    private Consumer<OutboxSequence> incrementCurrentSequence(DomainEvent event) {
        return currentSequence -> tryOutbox(event, OutboxSequence.incrementFrom(currentSequence));
    }

    private Runnable createInitialSequence(DomainEvent event) {
        return () -> tryOutbox(event, OutboxSequence.initial(event.aggregateId()));
    }

    private void tryOutbox(DomainEvent event, OutboxSequence newSequence) {
        OutboxInsertCommand command = mapper.toCommand(event, newSequence);
        Outbox outbox = repository.insert(command);
        repository.updateSequence(newSequence);
        TransactionSynchronizationManager.registerSynchronization(
                new TransactionSynchronization() {
                    @Override
                    public void afterCommit() {
                        OutboxQueue queue = domainQueues.get(event.domain());
                        queue.add(outbox.id());
                        log.debug("Message: {} was successfully queued", outbox.id());
                    }
                }
        );
    }
}
