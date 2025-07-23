package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.event.SpringEventPublisher;
import org.springframework.stereotype.Service;

@Service
public class SpringEventProducer {

    private final SpringEventPublisher publisher;
    private final MessageMapper mapper;

    public SpringEventProducer(SpringEventPublisher publisher,
                               MessageMapper mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public void produce(OutboxMessage outboxMessage) {
        var message = mapper.toDomain(outboxMessage);
        publisher.publishMessage(message);
    }
}
