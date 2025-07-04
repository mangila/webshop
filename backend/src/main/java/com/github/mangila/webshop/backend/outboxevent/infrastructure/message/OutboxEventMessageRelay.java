package com.github.mangila.webshop.backend.outboxevent.infrastructure.message;

import com.github.mangila.webshop.backend.outboxevent.application.gateway.OutboxEventServiceGateway;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.OutboxEventFindByIdQuery;
import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresNotification;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.event.EventListener;
import org.springframework.rabbit.stream.producer.RabbitStreamTemplate;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
public class OutboxEventMessageRelay {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventMessageRelay.class);

    private final OutboxEventServiceGateway outboxEventServiceGateway;
    private final RabbitStreamTemplate rabbitStreamTemplate;

    public OutboxEventMessageRelay(OutboxEventServiceGateway outboxEventServiceGateway,
                                   @Qualifier("outboxEventRabbitStreamTemplate") RabbitStreamTemplate rabbitStreamTemplate) {
        this.outboxEventServiceGateway = outboxEventServiceGateway;
        this.rabbitStreamTemplate = rabbitStreamTemplate;
    }

    @EventListener
    public void onSpringEvent(OutboxEventPostgresNotification event) {
        String payload = event.notification().getParameter();
        long outboxEventId = Long.parseLong(payload);
        OutboxEvent outboxEvent = outboxEventServiceGateway.query()
                .findById(new OutboxEventFindByIdQuery(outboxEventId));
        log.info("OutboxEventNotification received: {}", outboxEvent);

        CompletableFuture<Boolean> future = rabbitStreamTemplate.convertAndSend(outboxEvent);
        boolean ok = future.join();
        if (!ok) {
            log.error("OutboxEventRabbitStreamTemplate failed to send message");
        }
    }
}
