package com.github.mangila.webshop.common.notification;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.event.EventTopic;
import com.github.mangila.webshop.common.notification.model.Notification;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.Async;

import java.sql.Connection;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class PostgresNotificationListener implements PostgresListener {

    private static final Logger log = LoggerFactory.getLogger(PostgresNotificationListener.class);

    private final NotificationPublisher publisher;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbc;
    private final EventTopic eventTopic;
    private final Duration timeout;
    private final Class<? extends Notification> notificationType;
    public final AtomicBoolean shutdown;

    public PostgresNotificationListener(NotificationPublisher publisher,
                                        ObjectMapper objectMapper,
                                        JdbcTemplate jdbc,
                                        EventTopic eventTopic,
                                        Duration timeout,
                                        Class<? extends Notification> notificationType) {
        this.publisher = publisher;
        this.objectMapper = objectMapper;
        this.jdbc = jdbc;
        this.eventTopic = eventTopic;
        this.timeout = timeout;
        this.notificationType = notificationType;
        this.shutdown = new AtomicBoolean(false);
    }

    @Async
    @Override
    public void start() {
        log.info("Starting Postgres listener for topic -- {}", eventTopic);
        while (!shutdown.get()) {
            listen();
        }
    }

    @Override
    public void shutdown() {
        log.info("Shutting down listener for topic -- {}", eventTopic);
        shutdown.set(true);
    }

    @Override
    public void listen() {
        var timeoutMillis = (int) this.timeout.toMillis();
        jdbc.execute(String.format("LISTEN \"%s\"", eventTopic.toString()));
        jdbc.execute((Connection c) -> {
            var pg = c.unwrap(PgConnection.class);
            while (!shutdown.get()) {
                if (Thread.currentThread().isInterrupted()) {
                    log.warn("Interrupted while waiting for Postgres notification");
                    shutdown.set(true);
                    break;
                }
                var pgNotifications = pg.getNotifications(timeoutMillis);
                if (pgNotifications == null) {
                    continue;
                }
                for (PGNotification pgNotification : pgNotifications) {
                    var channel = pgNotification.getName();
                    var payload = pgNotification.getParameter();
                    log.debug("Received Postgres notification: {} -- {}", channel, payload);
                    var notification = deserializePayload(payload, notificationType);
                    if (notification == null) {
                        continue;
                    }
                    publisher.publishNotification(notification);
                }
            }
            return 0;
        });
    }

    @SuppressWarnings("unchecked")
    private <T extends Notification> T deserializePayload(String payload, Class<? extends Notification> notificationType) {
        try {
            return (T) objectMapper.readValue(payload, notificationType);
        } catch (JsonProcessingException e) {
            log.error("Failed to deserialize notification payload: {}", payload, e);
            return null;
        }
    }
}
