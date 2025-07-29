package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.OutboxQueryRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import io.vavr.control.Try;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Objects;

@Service
@ConditionalOnProperty(name = "app.message-relay.enabled", havingValue = "true")
public class MessageRelay {

    private static final Logger log = LoggerFactory.getLogger(MessageRelay.class);
    private final InternalMessageQueue internalMessageQueue;
    private final OutboxQueryRepository queryRepository;
    private final OutboxCommandRepository commandRepository;
    private final Processor processor;

    public MessageRelay(InternalMessageQueue internalMessageQueue,
                        OutboxQueryRepository queryRepository,
                        OutboxCommandRepository commandRepository,
                        Processor processor) {
        this.internalMessageQueue = internalMessageQueue;
        this.queryRepository = queryRepository;
        this.commandRepository = commandRepository;
        this.processor = processor;
    }

    @PostConstruct
    public void init() {
        queryRepository.findAllIdsByStatus(OutboxStatusType.PENDING, 50)
                .stream()
                .peek(id -> log.info("Queue Message: {}", id))
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
        queryRepository.findAllIdsByStatus(OutboxStatusType.PENDING, 10)
                .forEach(this::tryProcess);
    }

    private void tryProcess(OutboxId id) {
        Try.of(() -> processor.process(id))
                .onSuccess(processed -> {
                    if (processed) {
                        log.info("Message: {} was successfully processed", id);
                    } else {
                        log.error("Failed to process message: {}", id);
                        commandRepository.updateStatus(id, OutboxStatusType.FAILED, OutboxUpdated.now());
                    }
                })
                .onFailure(e -> {
                    log.error("Failed to process message: {}", id, e.getCause());
                    commandRepository.updateStatus(id, OutboxStatusType.FAILED, OutboxUpdated.now());
                });
    }

    @Component
    public static class Processor {
        private final RetryTemplate retryTemplate;
        private final Handler handler;

        public Processor(RetryTemplate retryTemplate,
                         Handler handler) {
            this.retryTemplate = retryTemplate;
            this.handler = handler;
        }

        public boolean process(OutboxId outboxId) {
            return retryTemplate.execute(
                    context -> {
                        log.debug("Processing message: {} - Retry Attempt {}", outboxId, context.getRetryCount());
                        handler.handle(outboxId);
                        return true;
                    },
                    context -> false);
        }
    }

    @Component
    public static class Handler {

        private final OutboxCommandRepository commandRepository;
        private final SpringEventProducer springEventProducer;

        public Handler(OutboxCommandRepository commandRepository, SpringEventProducer springEventProducer) {
            this.commandRepository = commandRepository;
            this.springEventProducer = springEventProducer;
        }

        @Transactional
        public void handle(OutboxId outboxId) {
            commandRepository.findByIdForUpdate(outboxId)
                    .ifPresentOrElse(message -> {
                        springEventProducer.produce(message);
                        commandRepository.updateStatus(message.id(), OutboxStatusType.PUBLISHED, OutboxUpdated.now());
                    }, () -> log.debug("Message: {} was locked or already processed", outboxId));
        }
    }
}
