package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.OutboxCommandRepository;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class MessageProcessor {

    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);
    private final RetryTemplate retryTemplate;
    private final Handler handler;

    public MessageProcessor(RetryTemplate retryTemplate, Handler handler) {
        this.retryTemplate = retryTemplate;
        this.handler = handler;
    }

    public Try<Boolean> tryProcess(OutboxId id) {
        return Try.of(() -> process(id));
    }

    private boolean process(OutboxId outboxId) {
        return retryTemplate.execute(
                context -> {
                    log.debug("Processing message: {} - Retry Attempt {}", outboxId, context.getRetryCount());
                    handler.handle(outboxId);
                    return true;
                },
                context -> false);
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
                    }, () -> log.debug("Message: {} locked or already processed", outboxId));
        }
    }

}
