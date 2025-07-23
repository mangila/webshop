package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import com.github.mangila.webshop.shared.exception.CqrsException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@ConditionalOnProperty(name = "app.message-relay.enabled", havingValue = "true")
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);

    private final MessageMapper mapper;
    private final InternalMessageQueue internalMessageQueue;
    private final OutboxCommandRepository commandRepository;
    private final SpringEventPublisher publisher;

    public MessageRelay(MessageMapper mapper,
                        OutboxCommandRepository commandRepository,
                        InternalMessageQueue internalMessageQueue,
                        SpringEventPublisher publisher) {
        this.mapper = mapper;
        this.commandRepository = commandRepository;
        this.internalMessageQueue = internalMessageQueue;
        this.publisher = publisher;
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-queue.fixed-rate}")
    public void pollInternalMessageQueue() {
        OutboxId outboxId = internalMessageQueue.poll();
        if (Objects.isNull(outboxId)) {
            return;
        }
        log.info("Pulled message from queue: {}", outboxId);
        Try.run(() -> {
                    OutboxMessage outboxMessage = commandRepository.findProjectionByIdAndPublishedFalseForUpdateOrThrow(outboxId);
                    relay(outboxMessage);
                })
                .onFailure(CqrsException.class, e -> log.warn("{}", e.getMessage()))
                .onFailure(throwable -> {
                    if (throwable instanceof CqrsException) {
                        log.warn("{}", throwable.getMessage());
                    } else {
                        log.error("Failed to relay message with ID: {}", outboxId, throwable);
                    }
                });
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-database.fixed-rate}")
    public void pollDatabase() {
        var messages = commandRepository.findAllByPublishedForUpdate(new OutboxPublished(false), 10);
        if (messages.isEmpty()) {
            return;
        }
        log.info("Pulled {} messages from database", messages.size());
        messages.stream()
                .peek(message -> log.info("Relay Message with ID: {}", message.id().value()))
                .forEach(message -> Try.run(() -> relay(message))
                        .onFailure(e -> log.error("Failed to relay message with ID: {}", message.id().value(), e)));
    }

    private void relay(OutboxMessage outboxMessage) {
        var message = mapper.toDomain(outboxMessage);
        publisher.publishMessage(message);
        commandRepository.updateAsPublished(outboxMessage.id(), new OutboxPublished(Boolean.TRUE));
    }
}
