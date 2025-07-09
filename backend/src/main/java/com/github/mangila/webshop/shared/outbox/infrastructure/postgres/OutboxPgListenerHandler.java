package com.github.mangila.webshop.shared.outbox.infrastructure.postgres;

import com.github.mangila.webshop.shared.infrastructure.spring.event.OutboxPgListenerFailedEvent;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
public class OutboxPgListenerHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboxPgListenerHandler.class);

    private final OutboxPgListener outboxPgListener;

    public OutboxPgListenerHandler(OutboxPgListener outboxPgListener) {
        this.outboxPgListener = outboxPgListener;
    }

    @PostConstruct
    public void init() {
        outboxPgListener.setUp();
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        outboxPgListener.start();
    }

    @Async
    @EventListener
    public void onFailed(OutboxPgListenerFailedEvent event) throws InterruptedException {
        log.error("OutboxEventPostgresListener failed, will restart", event.cause());
        outboxPgListener.stop();
        TimeUnit.SECONDS.sleep(5);
        outboxPgListener.start();
    }
}
