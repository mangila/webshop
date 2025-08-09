package com.github.mangila.webshop.outbox.application.event;

import com.github.mangila.webshop.outbox.domain.Outbox;
import com.github.mangila.webshop.outbox.domain.primitive.OutboxId;
import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.InternalQueue;
import com.github.mangila.webshop.shared.SpringTransactionUtil;
import com.github.mangila.webshop.shared.model.Domain;
import com.github.mangila.webshop.shared.model.OutboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronization;

import java.util.Map;

@Component
public class OutboxEventListener {
    private static final Logger log = LoggerFactory.getLogger(OutboxEventListener.class);
    private final OutboxEventHandler eventHandler;
    private final Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue;

    public OutboxEventListener(OutboxEventHandler eventHandler,
                               Map<Domain, InternalQueue<OutboxId>> domainToOutboxIdQueue) {
        this.eventHandler = eventHandler;
        this.domainToOutboxIdQueue = domainToOutboxIdQueue;
    }

    @EventListener
    void listen(OutboxEvent event) {
        Ensure.activeSpringSynchronization();
        Outbox outbox = eventHandler.handle(event);
        SpringTransactionUtil.registerSynchronization(() -> new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                log.debug("Outbox: {} for domain: {} was successfully persisted", outbox.id(), outbox.domain());
                InternalQueue<OutboxId> queue = domainToOutboxIdQueue.get(outbox.domain());
                queue.add(outbox.id());
                log.debug("Message: {} was successfully added to queue", outbox.id());
            }
        });
    }
}
