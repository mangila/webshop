package com.github.mangila.webshop.backend.common;

import com.github.mangila.webshop.backend.common.props.PostgresListenerProps;
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
import org.springframework.util.Assert;

public abstract class AbstractPostgresNotificationListener {

    private static final Logger log = LoggerFactory.getLogger(AbstractPostgresNotificationListener.class);

    private final SingleConnectionDataSource dataSource;
    private final String channelName;
    private volatile boolean running = true;

    public AbstractPostgresNotificationListener(HikariConfig hikariConfig) {
        Assert.notNull(hikariConfig, "HikariConfig must not be null");
        Assert.notNull(getProps(), "PostgresListenerProps must not be null");
        this.dataSource = new SingleConnectionDataSource(hikariConfig.getJdbcUrl(), hikariConfig.getUsername(), hikariConfig.getPassword(), Boolean.TRUE);
        this.channelName = getProps().channelName();
    }

    public abstract PostgresListenerProps getProps();

    public abstract void setUp();

    public abstract void onPgNotification(PGNotification pgNotification);

    public abstract void onFailure(Throwable cause);

    @Async
    public void listen() {
        log.info("Starting listener for channel {}", channelName);
        Try.of(() -> {
                    running = true;
                    var template = new JdbcTemplate(dataSource);
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
                .onSuccess(__ -> onFailure(new IllegalStateException("Listener stopped for unknown reason")));
    }

    public void stop() {
        Thread.currentThread().interrupt();
        this.running = false;
    }
}
