package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
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

    private final InternalMessageQueue internalMessageQueue;
    private final OutboxCommandRepository commandRepository;
    private final SpringEventProducer producer;

    public MessageRelay(OutboxCommandRepository commandRepository,
                        InternalMessageQueue internalMessageQueue,
                        SpringEventProducer producer) {
        this.commandRepository = commandRepository;
        this.internalMessageQueue = internalMessageQueue;
        this.producer = producer;
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-queue.fixed-rate}")
    public void pollInternalMessageQueue() {
        OutboxId outboxId = internalMessageQueue.poll();
        if (Objects.isNull(outboxId)) {
            return;
        }
        commandRepository.findMessageByIdAndPublishedForUpdate(outboxId, OutboxPublished.notPublished())
                .ifPresent(message -> {
                    log.info("Relay Message with ID: {}", message.id().value());
                    producer.produce(message);
                    commandRepository.updateAsPublished(message.id(), OutboxPublished.published());
                });
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-database.fixed-rate}")
    public void pollDatabase() {
        var messages = commandRepository.findManyMessagesByPublishedForUpdate(new OutboxPublished(false), 10);
        if (messages.isEmpty()) {
            return;
        }
        messages.forEach(message -> {
            log.info("Relay Message with ID: {}", message.id().value());
            producer.produce(message);
            commandRepository.updateAsPublished(message.id(), OutboxPublished.published());
        });
    }
}
