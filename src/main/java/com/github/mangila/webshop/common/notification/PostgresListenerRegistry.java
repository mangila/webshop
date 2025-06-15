package com.github.mangila.webshop.common.notification;

import jakarta.annotation.PreDestroy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PostgresListenerRegistry {

    private static final Logger log = LoggerFactory.getLogger(PostgresListenerRegistry.class);
    private final List<PostgresListener> listeners;

    public PostgresListenerRegistry(List<PostgresListener> listeners) {
        this.listeners = listeners;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        for (var listener : listeners) {
            listener.start();
        }
    }

    @PreDestroy
    public void shutdownAll() {
        listeners.forEach(PostgresListener::shutdown);
    }
}
