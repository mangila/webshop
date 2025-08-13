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
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.UUID;

/**
 * The Transactional Outbox Pattern. This event should be produced from a domain module wrapped inside the same transaction
 * <a href="https://microservices.io/patterns/data/transactional-outbox.html">https://microservices.io/patterns/data/transactional-outbox.html</a>
 * <p>
 * The InsertOutboxEventUseCase is a transactional use case that is responsible for handling the
 * insertion of an outbox event into the system.
 * Use case responsible for handling the insertion of an outbox event into the system.
 * This involves incrementing the outbox sequence, creating a new outbox entry,
 * updating the sequence information, and adding the generated outbox ID to a distinct queue.
 * <p>
 * This class ensures that the processing of outbox events is handled transactionally,
 * maintaining consistency across database operations and ensuring that all the required
 * steps for handling the event are executed.
 * <p>
 * Methods:
 * - The {@code execute} method is the main entry point for processing an {@code OutboxEvent}.
 * It performs the following steps in order:
 * 1. Increments the outbox sequence for the given aggregate.
 * 2. Creates a new outbox entry.
 * 3. Updates the sequence information in the database.
 * 4. Adds the outbox ID to the queue after the transaction commits.
 */
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

    /**
     * Why not {@code @TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)}?
     * {@code @Transactional(propagation = Propagation.MANDATORY)} will ensure an active Transaction and will throw an exception,
     * instead of not processing the event. Fail fast is the preferred approach here.
     */
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
