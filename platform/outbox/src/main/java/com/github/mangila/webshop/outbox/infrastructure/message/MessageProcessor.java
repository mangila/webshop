package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import io.vavr.control.Try;
import jakarta.validation.constraints.NotNull;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

@Service
@Validated
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

    private boolean process(@NotNull OutboxId outboxId) {
        return retryTemplate.execute(
                context -> {
                    context.setAttribute("outboxId", outboxId);
                    messageHandler.handle(outboxId);
                    return true;
                },
                context -> false);
    }
}
