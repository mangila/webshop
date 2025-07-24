package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
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
    private final SpringEventProducer springEventProducer;

    public MessageRelay(OutboxCommandRepository commandRepository,
                        InternalMessageQueue internalMessageQueue,
                        SpringEventProducer springEventProducer) {
        this.commandRepository = commandRepository;
        this.internalMessageQueue = internalMessageQueue;
        this.springEventProducer = springEventProducer;
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-queue.fixed-rate}")
    public void pollInternalMessageQueue() {
        OutboxId outboxId = internalMessageQueue.poll();
        if (Objects.isNull(outboxId)) {
            return;
        }
        commandRepository.findMessageByIdAndPublishedForUpdate(outboxId, OutboxPublished.notPublished())
                .ifPresent(this::tryRelay);
    }

    @Transactional
    @Scheduled(fixedRateString = "${app.message-relay.poller-database.fixed-rate}")
    public void pollDatabase() {
        commandRepository.findManyMessagesByPublishedForUpdate(OutboxPublished.notPublished(), 10)
                .forEach(this::tryRelay);
    }

    private void tryRelay(OutboxMessage message) {
        log.info("Relay Message with ID: {}", message.id().value());
        springEventProducer.produce(message);
        commandRepository.updatePublished(message.id(), OutboxPublished.published());
    }
}
