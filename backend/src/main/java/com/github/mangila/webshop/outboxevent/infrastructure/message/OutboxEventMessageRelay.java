package com.github.mangila.webshop.outboxevent.infrastructure.message;

import com.github.mangila.webshop.outboxevent.application.gateway.OutboxEventServiceGateway;
import com.github.mangila.webshop.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.outboxevent.domain.query.OutboxEventFindByIdQuery;
import com.github.mangila.webshop.outboxevent.domain.springevent.OutboxEventPostgresNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OutboxEventMessageRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventMessageRelay.class);

    private final OutboxEventServiceGateway outboxEventServiceGateway;
    private final OutboxEventRabbitMqProducer outboxEventRabbitMqProducer;

    public OutboxEventMessageRelay(OutboxEventServiceGateway outboxEventServiceGateway,
                                   OutboxEventRabbitMqProducer outboxEventRabbitMqProducer) {
        this.outboxEventServiceGateway = outboxEventServiceGateway;
        this.outboxEventRabbitMqProducer = outboxEventRabbitMqProducer;
    }

    @Async
    @EventListener
    public void onSpringEvent(OutboxEventPostgresNotification event) {
        String payload = event.notification().getParameter();
        OutboxEvent outboxEvent = outboxEventServiceGateway.query()
                .findById(OutboxEventFindByIdQuery.from(payload));
        log.info("OutboxEventNotification received: {}", outboxEvent);
        CompletableFuture<Boolean> future = outboxEventRabbitMqProducer.convertAndSendToStream(outboxEvent);
        boolean ok = future.join();
        if (ok) {
            log.info("OutboxEvent sent to RabbitMQ: {}", outboxEvent);
            outboxEventServiceGateway.command()
                    .markAsPublished(outboxEvent);
        } else {
            log.error("OutboxEvent failed to send to RabbitMQ: {}", outboxEvent);
            // TODO fallback polling
        }
    }
}
