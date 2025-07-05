package com.github.mangila.webshop.shared.outbox.infrastructure.postgres;

import com.github.mangila.webshop.shared.infrastructure.json.JsonMapper;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessage;
import com.github.mangila.webshop.shared.infrastructure.spring.event.OutboxPgListenerFailedEvent;
import com.github.mangila.webshop.shared.infrastructure.spring.event.OutboxPgNotification;
import com.github.mangila.webshop.shared.outbox.infrastructure.message.OutboxMessageRelay;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class OutboxPgListenerHandler {

    private static final Logger log = LoggerFactory.getLogger(OutboxPgListenerHandler.class);

    private final JsonMapper jsonMapper;
    private final OutboxMessageRelay outboxMessageRelay;
    private final OutboxPgListener outboxPgListener;

    public OutboxPgListenerHandler(JsonMapper jsonMapper,
                                   OutboxMessageRelay outboxMessageRelay,
                                   OutboxPgListener outboxPgListener) {
        this.jsonMapper = jsonMapper;
        this.outboxMessageRelay = outboxMessageRelay;
        this.outboxPgListener = outboxPgListener;
    }

    @Async
    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        outboxPgListener.setUp();
        outboxPgListener.start();
    }

    @Async
    @EventListener
    public void onNotification(OutboxPgNotification event) {
        String payload = event.notification().getParameter();
        var message = jsonMapper.toObject(payload.getBytes(), OutboxMessage.class);
        outboxMessageRelay.publish(message);
    }

    @Async
    @EventListener
    public void onFailed(OutboxPgListenerFailedEvent event) {
        log.error("OutboxEventPostgresListener failed, will restart", event.cause());
        outboxPgListener.stop();
        outboxPgListener.start();
    }
}
