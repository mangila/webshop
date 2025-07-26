package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxPublished;
import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@ConditionalOnProperty(name = "app.message-relay.enabled", havingValue = "true")
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);
    private final InternalMessageQueue internalMessageQueue;
    private final OutboxQueryRepository queryRepository;
    private final MessageProcessor processor;

    public MessageRelay(InternalMessageQueue internalMessageQueue,
                        OutboxQueryRepository queryRepository,
                        MessageProcessor processor) {
        this.internalMessageQueue = internalMessageQueue;
        this.queryRepository = queryRepository;
        this.processor = processor;
    }

    @PostConstruct
    public void init() {
        queryRepository.findAllIdsByPublished(OutboxPublished.notPublished(), 50)
                .stream()
                .peek(id -> log.info("Queue Message with ID: {}", id))
                .forEach(internalMessageQueue::add);
    }

    @Scheduled(fixedRateString = "${app.message-relay.poller-queue.fixed-rate}")
    public void pollInternalMessageQueue() {
        OutboxId outboxId = internalMessageQueue.poll();
        if (Objects.isNull(outboxId)) {
            return;
        }
        tryProcess(outboxId);
    }

    @Scheduled(fixedRateString = "${app.message-relay.poller-database.fixed-rate}")
    public void pollDatabase() {
        queryRepository.findAllIdsByPublished(OutboxPublished.notPublished(), 10)
                .forEach(this::tryProcess);
    }

    private void tryProcess(OutboxId id) {
        Try.run(() -> processor.process(id))
                .onFailure(e -> log.error("Error while relaying message with ID: {}", id, e));
    }
}
