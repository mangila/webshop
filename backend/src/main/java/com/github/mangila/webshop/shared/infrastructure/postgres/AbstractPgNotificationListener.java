package com.github.mangila.webshop.shared.infrastructure.postgres;

import io.vavr.control.Try;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;

public abstract class AbstractPgNotificationListener implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(AbstractPgNotificationListener.class);

    private volatile boolean running = true;
    private final SingleConnectionDataSource dataSource;
    private final String channelName;

    public AbstractPgNotificationListener(DataSourceProperties dataSourceProperties) {
        Assert.notNull(dataSourceProperties, "DataSourceProperties must not be null");
        Assert.notNull(getProps(), "PostgresListenerProps must not be null");
        this.dataSource = new SingleConnectionDataSource(dataSourceProperties.determineUrl(), dataSourceProperties.getUsername(), dataSourceProperties.getPassword(), Boolean.TRUE);
        this.channelName = getProps().channelName();
    }

    public abstract PostgresListenerProps getProps();

    public abstract void setUp();

    public abstract void onPgNotification(PGNotification pgNotification);

    public abstract void onFailure(Throwable cause);

    public void start() {
        log.info("Starting PostgresListener for channel {}", channelName);
        Try.of(() -> {
                    running = true;
                    var template = new JdbcTemplate(dataSource);
                    // language=PostgreSQL
                    template.execute("LISTEN %s".formatted(channelName));
                    int timeoutMillis = 300;
                    template.execute((ConnectionCallback<Void>) connection -> {
                        PgConnection pgConnection = connection.unwrap(PgConnection.class);
                        while (running) {
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
                .onSuccess(__ -> {
                    if (!running) {
                        log.info("PostgresListener for channel '{}' stopped", channelName);
                        return;
                    }
                    onFailure(new IllegalStateException(String.format("PostgresListener for channel '%s' stopped for unknown reason", channelName)));
                });
    }

    public void stop() {
        running = false;
    }
}
