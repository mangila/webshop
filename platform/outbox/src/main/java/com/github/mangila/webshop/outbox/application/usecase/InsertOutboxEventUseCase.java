package com.github.mangila.webshop.outbox.application.usecase;

import com.github.mangila.webshop.outbox.application.action.command.CreateOutboxCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.IncrementOutboxSequenceCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxSequenceCommandAction;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.OutboxSequence;
import com.github.mangila.webshop.outbox.domain.cqrs.command.CreateOutboxCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.IncrementOutboxSequenceCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxSequenceCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxAggregateId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPayload;
import com.github.mangila.webshop.shared.DistinctQueue;
import com.github.mangila.webshop.shared.SpringTransactionUtil;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.UUID;

@Service
public class InsertOutboxEventUseCase {

    private static final Logger log = LoggerFactory.getLogger(InsertOutboxEventUseCase.class);
    private final UpdateOutboxSequenceCommandAction updateOutboxSequenceCommandAction;
    private final IncrementOutboxSequenceCommandAction incrementOutboxSequenceCommandAction;
    private final CreateOutboxCommandAction createOutboxCommandAction;
    private final DistinctQueue<OutboxId> eventQueue;

    public InsertOutboxEventUseCase(UpdateOutboxSequenceCommandAction updateOutboxSequenceCommandAction,
                                    IncrementOutboxSequenceCommandAction incrementOutboxSequenceCommandAction,
                                    CreateOutboxCommandAction createOutboxCommandAction,
                                    DistinctQueue<OutboxId> eventQueue) {
        this.updateOutboxSequenceCommandAction = updateOutboxSequenceCommandAction;
        this.incrementOutboxSequenceCommandAction = incrementOutboxSequenceCommandAction;
        this.createOutboxCommandAction = createOutboxCommandAction;
        this.eventQueue = eventQueue;
    }

    @EventListener
    @Transactional(propagation = Propagation.MANDATORY)
    void execute(OutboxEvent event) {
        var aggregateId = new OutboxAggregateId(event.aggregateId());
        OutboxSequence newSequence = incrementOutboxSequenceCommandAction.execute(IncrementOutboxSequenceCommand.from((aggregateId)));
        Outbox outbox = createOutboxCommandAction.execute(toCommand(event, newSequence));
        updateOutboxSequenceCommandAction.execute(UpdateOutboxSequenceCommand.from(newSequence));
        SpringTransactionUtil.registerSynchronization(() -> new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.debug("Outbox: {} for domain: {} was successfully persisted", outbox.id(), outbox.domain());
                eventQueue.add(outbox.id());
                log.debug("OutboxId: {} was successfully added to queue", outbox.id());
            }
        });
    }

    private static CreateOutboxCommand toCommand(OutboxEvent event, OutboxSequence sequence) {
        UUID aggregateId = event.aggregateId();
        return new CreateOutboxCommand(
                event.domain(),
                event.event(),
                new OutboxAggregateId(aggregateId),
                new OutboxPayload(event.payload()),
                sequence
        );
    }

}
