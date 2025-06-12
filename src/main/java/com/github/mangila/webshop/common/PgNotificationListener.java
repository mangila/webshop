package com.github.mangila.webshop.common;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.common.model.Notification;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.time.Duration;
import java.util.concurrent.atomic.AtomicBoolean;

public class PgNotificationListener implements Runnable {

    private static final Logger log = LoggerFactory.getLogger(PgNotificationListener.class);

    private final ChannelTopic channel;
    private final Duration timeout;
    private final Class<? extends Notification> notificationType;
    private final ApplicationEventPublisher publisher;
    private final ObjectMapper objectMapper;
    private final JdbcTemplate jdbc;
    public final AtomicBoolean shutdown = new AtomicBoolean(false);

    public PgNotificationListener(ChannelTopic channel,
                                  Duration timeout,
                                  Class<? extends Notification> notificationType,
                                  ApplicationEventPublisher publisher,
                                  ObjectMapper objectMapper,
                                  JdbcTemplate jdbc) {
        this.channel = channel;
        this.timeout = timeout;
        this.notificationType = notificationType;
        this.publisher = publisher;
        this.objectMapper = objectMapper;
        this.jdbc = jdbc;
    }

    public void shutdown() {
        this.shutdown.set(true);
        jdbc.execute("UNLISTEN \"" + channel + "\"");
        log.info("Shutting down listener for channel {}", channel);
    }

    @Override
    public void run() {
        log.info("Starting listener for channel {}", channel);
        var timeoutMillis = (int) this.timeout.toMillis();
        jdbc.execute(String.format("LISTEN \"%s\"", channel.toString()));
        jdbc.execute((Connection c) -> {
            var pg = c.unwrap(PgConnection.class);
            while (!shutdown.get()) {
                var pgNotifications = pg.getNotifications(timeoutMillis);
                if (pgNotifications == null) {
                    continue;
                }
                for (PGNotification pgNotification : pgNotifications) {
                    var channel = pgNotification.getName();
                    var payload = pgNotification.getParameter();
                    log.debug("Received notification: {} -- {}", channel, payload);
                    var notification = deserializePayload(payload, notificationType);
                    if (notification == null) {
                        continue;
                    }
                    publisher.publishEvent(notification);
                }
            }
            return 0;
        });
    }

    private Object deserializePayload(String payload, Class<? extends Notification> notificationType) {
        try {
            return objectMapper.readValue(payload, notificationType);
        } catch (Exception e) {
            log.error("Failed to deserialize payload: {}", payload, e);
        }
        return null;
    }
}
