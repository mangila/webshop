package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.action.command.FindOutboxForUpdateCommandAction;
import com.github.mangila.webshop.outbox.application.action.command.UpdateOutboxStatusCommandAction;
import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.cqrs.command.FindOutboxForUpdateCommand;
import com.github.mangila.webshop.outbox.domain.cqrs.command.UpdateOutboxStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.infrastructure.message.producer.SpringEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);

    private final FindOutboxForUpdateCommandAction findOutboxForUpdateCommandAction;
    private final UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction;
    private final SpringEventProducer springEventProducer;

    public MessageHandler(FindOutboxForUpdateCommandAction findOutboxForUpdateCommandAction, UpdateOutboxStatusCommandAction updateOutboxStatusCommandAction, SpringEventProducer springEventProducer) {
        this.findOutboxForUpdateCommandAction = findOutboxForUpdateCommandAction;
        this.updateOutboxStatusCommandAction = updateOutboxStatusCommandAction;
        this.springEventProducer = springEventProducer;
    }


    /**
     * Handles an outbox message by attempting to process it, publish the corresponding InboxEvent,
     * and update the status of the outbox message to PUBLISHED.
     * <p>
     * If the outbox message is locked or has already been processed, it skips the operation
     * and logs a debug message.
     * <p>
     * Publishes to Spring Event listeners, add more publishers to any other infrastructure (e.g., RabbitMQ queue/stream, Kafka Topic, Redis Stream)
     *
     * @param outboxId the identifier of the outbox message to process
     */
    @Transactional
    public void handle(OutboxId outboxId) {
        findOutboxForUpdateCommandAction.execute(new FindOutboxForUpdateCommand(outboxId))
                .filter(Outbox::notPublished)
                .ifPresentOrElse(outbox -> springEventProducer.produce()
                        .andThen(Outbox::id)
                        .andThen(UpdateOutboxStatusCommand::published)
                        .andThen(updateOutboxStatusCommandAction::execute)
                        .apply(outbox), () -> log.debug("Message: {} locked or already processed", outboxId));
    }
}
