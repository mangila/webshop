package com.github.mangila.webshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.model.ChannelTopic;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.PayloadApplicationEvent;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class PgNotificationListener implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PgNotificationListener.class);

    private final ChannelTopic channel;
    private final Duration timeout;
    private final Class<?> type;
    private final Constructor<?> typeConstructor;
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final SingleConnectionJdbcTemplate jdbc;
    public final AtomicBoolean shutdown = new AtomicBoolean(false);

    public PgNotificationListener(ChannelTopic channel,
                                  Duration timeout,
                                  Class<?> type,
                                  ApplicationEventPublisher publisher,
                                  ObjectMapper objectMapper,
                                  SingleConnectionJdbcTemplate jdbc) {
        this.channel = channel;
        this.timeout = timeout;
        this.type = type;
        try {
            this.typeConstructor = type.getDeclaredConstructor(String.class);
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
        this.publisher = publisher;
        this.objectMapper = objectMapper;
        this.jdbc = jdbc;
    }

    public void shutdown() {
        this.shutdown.set(true);
        jdbc.getTemplate().execute("UNLISTEN \"" + channel + "\"");
        log.info("Shutting down listener for channel {}", channel);
    }

    @Override
    public void run() {
        log.info("Starting listener for channel {}", channel);
        var timeoutMillis = (int) this.timeout.toMillis();
        var template = jdbc.getTemplate();
        template.execute(String.format("LISTEN \"%s\"", channel));
        template.execute((Connection c) -> {
            var pg = c.unwrap(PgConnection.class);
            while (!shutdown.get()) {
                var notifications = pg.getNotifications(timeoutMillis);
                if (notifications == null) {
                    continue;
                }
                for (PGNotification notification : notifications) {
                    var channel = notification.getName();
                    var payload = notification.getParameter();
                    log.debug("Received notification: {} -- {}", channel, payload);
                    var instance = tryCreateNewInstance(payload);
                    publisher.publishEvent(new PayloadApplicationEvent<>(payload, type.cast(instance)));
                }
            }
            return 0;
        });
    }

    private Object tryCreateNewInstance(String payload) {
        try {
            return typeConstructor.newInstance(payload);
        } catch (InstantiationException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
