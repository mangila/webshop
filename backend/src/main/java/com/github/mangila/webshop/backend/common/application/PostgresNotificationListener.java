package com.github.mangila.webshop.backend.common.application;

import com.github.mangila.webshop.backend.common.domain.props.PostgresListenerProps;
import com.zaxxer.hikari.HikariConfig;
import io.vavr.control.Try;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.scheduling.annotation.Async;

public abstract class PostgresNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(PostgresNotificationListener.class);

    private final SingleConnectionDataSource dataSource;
    private final String channelName;
    private volatile boolean running = true;

    public PostgresNotificationListener(HikariConfig hikariConfig) {
        this.dataSource = new SingleConnectionDataSource(hikariConfig.getJdbcUrl(), hikariConfig.getUsername(), hikariConfig.getPassword(), true);
        this.channelName = getProps().channelName();
    }

    public abstract PostgresListenerProps getProps();

    public abstract void setUp(PostgresListenerProps props);

    public abstract void onPgNotification(PGNotification pgNotification);

    public abstract void onFailure(Throwable cause);

    @Async
    public void listen() {
        log.info("Starting listener for channel {}", channelName);
        running = true;
        var template = new JdbcTemplate(dataSource);
        Try.of(() -> {
                    // language=PostgreSQL
                    template.execute("LISTEN %s".formatted(channelName));
                    int timeoutMillis = 300;
                    template.execute((ConnectionCallback<Void>) connection -> {
                        PgConnection pgConnection = connection.unwrap(PgConnection.class);
                        while (running && !Thread.currentThread().isInterrupted()) {
                            PGNotification[] notifications = pgConnection.getNotifications(timeoutMillis);
                            for (PGNotification notification : notifications) {
                                onPgNotification(notification);
                            }
                        }
                        return null;
                    });
                    return null;
                })
                .onFailure(this::onFailure)
                .onSuccess(object -> onFailure(new IllegalStateException("Listener stopped for unknown reason")));
    }

    public void stop() {
        Thread.currentThread().interrupt();
        this.running = false;
    }
}
