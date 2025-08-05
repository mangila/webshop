package com.github.mangila.webshop.inbox.application.event;

import com.github.mangila.webshop.shared.Ensure;
import com.github.mangila.webshop.shared.model.EventSource;
import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;

@Service
public class InboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(InboxEventListener.class);

    private final InboxEventHandler eventHandler;

    public InboxEventListener(InboxEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @EventListener(value = InboxEvent.class)
    void listen(InboxEvent event) {
        log.info("Received event: {}", event);
        Ensure.equals(event.source(), EventSource.SPRING_EVENT);
        Ensure.activeSpringTransaction();
        Ensure.activeSpringSynchronization();
        eventHandler.handle(event);
    }

}
