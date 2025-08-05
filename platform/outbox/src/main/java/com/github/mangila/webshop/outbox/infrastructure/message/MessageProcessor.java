package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.Ensure;
import io.vavr.control.Try;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageProcessor {

    private final RetryTemplate retryTemplate;
    private final MessageHandler messageHandler;

    public MessageProcessor(RetryTemplate retryTemplate,
                            MessageHandler messageHandler) {
        this.retryTemplate = retryTemplate;
        this.messageHandler = messageHandler;
    }

    public Try<Boolean> tryProcess(OutboxId id) {
        return Try.of(() -> process(id));
    }

    private boolean process(OutboxId outboxId) {
        Ensure.notNull(outboxId, OutboxId.class);
        return retryTemplate.execute(
                context -> {
                    context.setAttribute("outboxId", outboxId);
                    messageHandler.handle(outboxId);
                    return true;
                },
                context -> false);
    }
}
