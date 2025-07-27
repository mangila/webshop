package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.model.DomainMessage;
import org.springframework.stereotype.Component;

@Component
public class MessageMapper {

    public DomainMessage toDomain(OutboxMessage outboxMessage) {
        return new DomainMessage(
                outboxMessage.id().value(),
                outboxMessage.aggregateId().value(),
                outboxMessage.domain(),
                outboxMessage.event()
        );
    }
}
