package com.github.mangila.webshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.model.ChannelTopic;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;
import org.springframework.context.event.EventListener;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.time.Duration;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.stream.Collectors;

@Component
public class PostgresNotificationListener implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PostgresNotificationListener.class);

    private final DataSourceProperties dataSourceProperties;
    private final ExecutorService executorService;
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbcTemplate;
    private final AtomicBoolean shutdown = new AtomicBoolean(false);

    public PostgresNotificationListener(DataSourceProperties dataSourceProperties,
                                        @Qualifier("virtualThreadExecutor") ExecutorService executorService,
                                        ApplicationEventPublisher publisher,
                                        ObjectMapper objectMapper,
                                        @Qualifier("singleConnectionJdbcTemplate") JdbcTemplate jdbcTemplate) {
        this.dataSourceProperties = dataSourceProperties;
        this.executorService = executorService;
        this.publisher = publisher;
        this.objectMapper = objectMapper;
        this.jdbcTemplate = jdbcTemplate;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void onReady() {
        log.info("ApplicationReadyEvent received. Starting listener.");
        executorService.submit(this);
    }

    @Override
    public void run() {
        log.info("Listening for notifications");
        int timeout = (int) Duration.ofMillis(300).toMillis();
        jdbcTemplate.execute((Connection c) -> {
            var listenQuery = ChannelTopic.getTopicsAsStrings()
                    .stream()
                    .filter(s -> !s.equals("UNKNOWN"))
                    .map(topic -> String.format("LISTEN \"%s\"", topic))
                    .collect(Collectors.joining("; "));
            log.info("Listening for notifications: {}", listenQuery);
            try (var statement = c.createStatement()) {
                statement.execute(listenQuery);
            }
            var pg = c.unwrap(PgConnection.class);
            while (!shutdown.get()) {
                var notifications = pg.getNotifications(timeout);
                for (PGNotification notification : notifications) {
                    var channel = ChannelTopic.fromString(notification.getName());
                    var payload = notification.getParameter();
                    log.debug("Received notification: {} -- {}", channel, payload);
                    switch (channel) {
                        case ORDERS -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case PAYMENTS -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case PRODUCTS -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case CUSTOMERS -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case INVENTORY -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case DELIVERIES -> publisher.publishEvent(new PayloadApplicationEvent<>(this, payload));
                        case UNKNOWN -> log.warn("Unknown channel: {}", notification.getName());
                    }
                }
            }
            return 0;
        });
    }

    public void shutdown() {
        shutdown.set(true);
    }

    @EventListener
    public void on(PayloadApplicationEvent<?> event) {

    }

}
