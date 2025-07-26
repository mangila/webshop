package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.util.ApplicationException;
import io.vavr.control.Try;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessor {
    private static final Logger log = LoggerFactory.getLogger(MessageProcessor.class);
    private final MessageHandler messageHandler;
    private final RetryTemplate retryTemplate;

    public MessageProcessor(MessageHandler messageHandler,
                            RetryTemplate retryTemplate) {
        this.messageHandler = messageHandler;
        this.retryTemplate = retryTemplate;
    }

    public void process(OutboxId outboxId) {
        retryTemplate.execute(context -> {
            log.debug("Processing message with ID: {} with Retry Attempt: {}", outboxId, context.getRetryCount());
            return Try.run(() -> messageHandler.handle(outboxId))
                    .onSuccess(v -> log.debug("Message processed with ID: {}", outboxId))
                    .getOrElseThrow(cause -> new ApplicationException("Error while processing message %s".formatted(outboxId), cause));
        });
    }
}
