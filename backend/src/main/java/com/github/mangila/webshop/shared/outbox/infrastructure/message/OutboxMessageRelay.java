package com.github.mangila.webshop.shared.outbox.infrastructure.message;

import com.github.mangila.webshop.shared.outbox.application.gateway.OutboxServiceGateway;
import com.github.mangila.webshop.shared.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.outbox.infrastructure.rabbitmq.OutboxRabbitProducer;
import io.vavr.collection.Stream;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OutboxMessageRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxMessageRelay.class);

    private final OutboxServiceGateway outboxServiceGateway;
    private final OutboxRabbitProducer rabbitProducer;

    public OutboxMessageRelay(OutboxServiceGateway outboxServiceGateway, OutboxRabbitProducer rabbitProducer) {
        this.outboxServiceGateway = outboxServiceGateway;
        this.rabbitProducer = rabbitProducer;
    }

    public void publish(OutboxMessage message) {
        boolean ok = Stream.of(message)
                .map(rabbitProducer::sendToStream)
                .map(CompletableFuture::join)
                .get();
        if (ok) {
            log.info("OutboxMessage sent to RabbitMQ: {}", message);
            outboxServiceGateway.command().updateAsPublished(new OutboxId(message.id()));
        } else {
            log.error("OutboxMessage failed to send to RabbitMQ: {}", message);
        }
    }

}
