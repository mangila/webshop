package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.cqrs.OutboxUpdateStatusCommand;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.projection.OutboxProjection;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.outbox.infrastructure.message.producer.SpringEventProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class MessageHandler {

    private static final Logger log = LoggerFactory.getLogger(MessageHandler.class);
    private final OutboxCommandService commandService;

    private final SpringEventProducer springEventProducer;

    public MessageHandler(OutboxCommandService commandService,
                          SpringEventProducer springEventProducer) {
        this.commandService = commandService;
        this.springEventProducer = springEventProducer;
    }

    /**
     * Handles an outbox message by attempting to process it, publish the corresponding InboxEvent,
     * and update the status of the outbox message to PUBLISHED.
     * <p>
     * If the outbox message is locked or has already been processed, it skips the operation
     * and logs a debug message.
     * <p>
     * Publishes to Spring Event listeners, add more publishers to any other infrastructure (e.g., RabbitMQ queue, Kafka Topic, WebHook)
     *
     * @param outboxId the identifier of the outbox message to process
     */
    @Transactional
    public void handle(OutboxId outboxId) {
        commandService.findByIdForUpdate(outboxId)
                .ifPresentOrElse(projection -> {
                    springEventProducer.produce(projection);
                    commandService.updateStatus(getOutboxUpdateStatusCommand(projection));
                }, () -> log.debug("Message: {} locked or already processed", outboxId));
    }

    private static OutboxUpdateStatusCommand getOutboxUpdateStatusCommand(OutboxProjection projection) {
        return new OutboxUpdateStatusCommand(
                projection.id(),
                OutboxStatusType.PUBLISHED,
                OutboxUpdated.now()
        );
    }
}
