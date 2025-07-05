package com.github.mangila.webshop.backend.outboxevent.infrastructure.message;

import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresListenerFailedEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventPostgresListenerHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboxEventPostgresListenerHandler.class);

    private final OutboxEventPostgresListener outboxEventPostgresListener;

    public OutboxEventPostgresListenerHandler(OutboxEventPostgresListener outboxEventPostgresListener) {
        this.outboxEventPostgresListener = outboxEventPostgresListener;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        outboxEventPostgresListener.setUp();
        outboxEventPostgresListener.start();
    }

    @Async
    @EventListener
    public void onSpringEvent(OutboxEventPostgresListenerFailedEvent event) {
        log.error("OutboxEventPostgresListener failed, will restart", event.cause());
        outboxEventPostgresListener.stop();
        outboxEventPostgresListener.start();
    }
}
