package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

@Service
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
    @Scheduled(
            fixedRate = 1,
            timeUnit = TimeUnit.SECONDS)
    public void pollInternalMessageQueue() {
        OutboxId outboxId = internalMessageQueue.poll();
        if (Objects.isNull(outboxId)) {
            return;
        }
        log.info("Pulled message from outbox: {}", outboxId);
        OutboxMessage message = commandRepository.findProjectionByIdAndPublishedFalseForUpdateOrThrow(outboxId);
        tryRelay(message);
    }

    @Transactional
    @Scheduled(
            fixedRate = 2,
            timeUnit = TimeUnit.MINUTES)
    public void pollDatabase() {
        var messages = commandRepository.findAllByPublishedForUpdate(new OutboxPublished(false), 10);
        if (messages.isEmpty()) {
            return;
        }
        log.info("Pulled {} messages from outbox", messages.size());
        messages.forEach(this::tryRelay);
    }

    private void tryRelay(OutboxMessage outboxMessage) {
        var message = mapper.toDomain(outboxMessage);
        publisher.publishMessage(message);
        commandRepository.updateAsPublished(outboxMessage.id(), new OutboxPublished(Boolean.TRUE));
    }
}
