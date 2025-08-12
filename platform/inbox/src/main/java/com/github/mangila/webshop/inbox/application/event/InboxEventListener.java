package com.github.mangila.webshop.inbox.application.event;

import com.github.mangila.webshop.shared.model.InboxEvent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class InboxEventListener {

    private static final Logger log = LoggerFactory.getLogger(InboxEventListener.class);

    private final InboxEventHandler eventHandler;

    public InboxEventListener(InboxEventHandler eventHandler) {
        this.eventHandler = eventHandler;
    }

    @EventListener(id = "inbox-event-listener")
    @Transactional(propagation = Propagation.MANDATORY)
    void listen(InboxEvent event) {
        log.info("Received event: {}", event);
        eventHandler.handle(event);
    }
}
