package com.github.mangila.webshop.outbox.infrastructure.message;

import com.github.mangila.webshop.outbox.domain.message.OutboxMessage;
import com.github.mangila.webshop.shared.model.DomainMessage;
import com.github.mangila.webshop.shared.SpringEventPublisher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class SpringEventProducer {

    private static final Logger log = LoggerFactory.getLogger(SpringEventProducer.class);
    private final SpringEventPublisher publisher;
    private final MessageMapper mapper;

    public SpringEventProducer(SpringEventPublisher publisher,
                               MessageMapper mapper) {
        this.publisher = publisher;
        this.mapper = mapper;
    }

    public void produce(OutboxMessage outboxMessage) {
        DomainMessage message = mapper.toDomain(outboxMessage);
        publisher.publishDomainMessage(message);
    }
}
