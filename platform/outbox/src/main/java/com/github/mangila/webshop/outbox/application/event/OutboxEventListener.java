package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxInsertCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.outbox.infrastructure.message.OutboxQueue;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.DomainEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

import java.util.Map;
import java.util.UUID;
import java.util.function.Supplier;

@Service
public class OutboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventListener.class);

    private final Map<Domain, OutboxQueue> domainQueues;
    private final OutboxEventHandler handler;

    OutboxEventListener(Map<Domain, OutboxQueue> domainQueues,
                        OutboxEventHandler handler) {
        this.domainQueues = domainQueues;
        this.handler = handler;
    }

    @EventListener
    void listen(DomainEvent event) {
        Ensure.activeSpringTransaction();
        Ensure.isTrue(TransactionSynchronizationManager.isSynchronizationActive(), "Synchronization is not active");
        Outbox outbox = handler.handle(event);
        registerSynchronization(() -> new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.debug("Outbox: {} for domain: {} was successfully persisted", outbox.id(), outbox.domain());
                OutboxQueue queue = domainQueues.get(outbox.domain());
                queue.add(outbox.id());
                log.debug("Message: {} was successfully added to queue", outbox.id());
            }
        });
    }

    void registerSynchronization(Supplier<TransactionSynchronization> supplier) {
        TransactionSynchronizationManager.registerSynchronization(supplier.get());
    }

    @Component
    public static class OutboxEventHandler {

        private final OutboxCommandService commandService;
        private final OutboxEventMapper mapper = new OutboxEventMapper();

        OutboxEventHandler(OutboxCommandService commandService) {
            this.commandService = commandService;
        }

        @Transactional
        Outbox handle(DomainEvent event) {
            var aggregateId = new OutboxAggregateId(event.aggregateId());
            OutboxSequence newSequence = commandService.findSequenceWithLockAndIncrement(aggregateId);
            OutboxInsertCommand command = mapper.toCommand(event, newSequence);
            Outbox outbox = commandService.insert(command);
            commandService.updateSequence(newSequence);
            return outbox;
        }

    }

    private static class OutboxEventMapper {
        OutboxInsertCommand toCommand(DomainEvent event, OutboxSequence sequence) {
            UUID aggregateId = event.aggregateId();
            return new OutboxInsertCommand(
                    event.domain(),
                    event.event(),
                    new OutboxAggregateId(aggregateId),
                    new OutboxPayload(event.payload()),
                    sequence
            );
        }
    }
}
