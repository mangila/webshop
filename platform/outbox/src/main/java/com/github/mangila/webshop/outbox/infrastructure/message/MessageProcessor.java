package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.application.service.OutboxCommandService;
import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxUpdated;
import com.github.mangila.webshop.outbox.domain.types.OutboxStatusType;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import com.github.mangila.webshop.shared.model.DomainMessage;
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
        Ensure.notNull(outboxId, OutboxId.class);
        return retryTemplate.execute(
                context -> {
                    context.setAttribute("outboxId", outboxId);
                    handler.handle(outboxId);
                    return true;
                },
                context -> false);
    }

    @Component
    public static class Handler {

        private final OutboxCommandService commandService;
        private final SpringEventPublisher springEventPublisher;
        private final MessageMapper mapper = new MessageMapper();

        public Handler(OutboxCommandService commandService,
                       SpringEventPublisher springEventPublisher) {
            this.commandService = commandService;
            this.springEventPublisher = springEventPublisher;
        }

        @Transactional
        public void handle(OutboxId outboxId) {
            commandService.findByIdForUpdate(outboxId)
                    .ifPresentOrElse(message -> {
                        DomainMessage domainMessage = mapper.toDomain(message);
                        springEventPublisher.publishDomainMessage(domainMessage);
                        commandService.updateStatus(
                                message.id(),
                                OutboxStatusType.PUBLISHED,
                                OutboxUpdated.now());
                    }, () -> log.debug("Message: {} locked or already processed", outboxId));
        }

        private static class MessageMapper {
            public DomainMessage toDomain(OutboxMessage outboxMessage) {
                return new DomainMessage(
                        outboxMessage.id().value(),
                        outboxMessage.aggregateId().value(),
                        outboxMessage.domain(),
                        outboxMessage.event()
                );
            }
        }
    }
}
