package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.action.command.FindOutboxForUpdateCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.cqrs.command.FindOutboxForUpdateCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.producer.Producer;
import com.github.mangila.webshop.shared.Ensure;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.support.TransactionTemplate;

/**
 * Publishes messages from the outbox with retry and transactional mechanisms.
 * <p>
 * This class is responsible for handling the publishing of outbox messages, ensuring that the messages
 * are processed reliably and their statuses are updated accordingly. It employs retry logic for error
 * handling and transactional guarantees for message status updates.
 * <p>
 * The primary responsibility of this class is to coordinate the following steps:
 * - Retrieve an outbox message for update.
 * - Use the provided producer to publish the message.
 * - Update the outbox message's status to reflect the publication state.
 * <p>
 * If the targeted outbox message is locked or already processed (based on its status), it skips further
 * processing.
 * <p>
 * Dependencies:
 * - {@link RetryTemplate} for implementing retries.
 * - {@link FindOutboxForUpdateCommandAction} for retrieving outbox messages for processing.
 * - {@link UpdateOutboxStatusCommandAction} for updating the outbox status.
 * - {@link Producer} for publishing the message to the destination.
 * - {@link TransactionTemplate} for maintaining transactional consistency during status updates.
 * <p>
 * An instance of this class must be properly initialized with its dependencies and is expected to be
 * used in service layers requiring reliable outbox message publication.
 */
@Service
public class OutboxPublisher {

    private static final Logger log = LoggerFactory.getLogger(OutboxPublisher.class);
    private final RetryTemplate retryTemplate;
    private final FindOutboxForUpdateCommandAction findOutboxForUpdateCommandAction;
    private final UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction;
    private final Producer producer;
    private final TransactionTemplate transactionTemplate;

    public OutboxPublisher(@Qualifier("outboxRetryTemplate") RetryTemplate retryTemplate,
                           FindOutboxForUpdateCommandAction findOutboxForUpdateCommandAction,
                           UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction,
                           @Qualifier("springEventProducer") Producer producer,
                           TransactionTemplate transactionTemplate) {
        this.retryTemplate = retryTemplate;
        this.findOutboxForUpdateCommandAction = findOutboxForUpdateCommandAction;
        this.updateOutboxStatusCommandAction = updateOutboxStatusCommandAction;
        this.producer = producer;
        this.transactionTemplate = transactionTemplate;
    }

    public Try<Boolean> tryPublish(OutboxId id) {
        return Try.of(() -> publish(id));
    }

    private boolean publish(OutboxId outboxId) {
        Ensure.notNull(outboxId, OutboxId.class);
        return retryTemplate.execute(retryContext -> {
                    retryContext.setAttribute("outboxId", outboxId);
                    return findOutboxForUpdateCommandAction.execute(new FindOutboxForUpdateCommand(outboxId))
                            .map(outbox -> {
                                transactionTemplate.executeWithoutResult(tx -> {
                                    producer.produce()
                                            .andThen(Outbox::id)
                                            .andThen(UpdateOutboxStatusCommand::published)
                                            .andThen(updateOutboxStatusCommandAction::execute)
                                            .apply(outbox);
                                });
                                return true;
                            })
                            .orElseGet(() -> {
                                log.debug("Outbox: {} already published or locked by another thread", outboxId);
                                return false;
                            });
                },
                retryContext -> {
                    updateOutboxStatusCommandAction.execute(UpdateOutboxStatusCommand.failed(outboxId));
                    return false;
                });
    }
}
