package com.github.mangila.webshop.shared.infrastructure.postgres;

import com.github.mangila.webshop.shared.domain.exception.ApplicationException;
import com.zaxxer.hikari.HikariDataSource;
import io.vavr.control.Try;
import org.postgresql.PGNotification;
import org.postgresql.jdbc.PgConnection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;
import org.springframework.boot.jdbc.DataSourceUnwrapper;
import org.springframework.jdbc.core.ConnectionCallback;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.datasource.SingleConnectionDataSource;
import org.springframework.util.Assert;

import javax.sql.DataSource;

public abstract class AbstractPgNotificationListener implements DisposableBean {

    private static final Logger log = LoggerFactory.getLogger(AbstractPgNotificationListener.class);

    private volatile boolean running = true;
    private final SingleConnectionDataSource dataSource;
    private final String channelName;

    /**
     * javax.sql.DataSource don't have getters for url, username and password
     * since we need to create a brand-new connection so we not use one from the connection pool
     */
    public AbstractPgNotificationListener(DataSource dataSource) {
        Assert.notNull(dataSource, "DataSource must not be null");
        Assert.notNull(getProps(), "PostgresListenerProps must not be null");
        HikariDataSource hikariDataSource = DataSourceUnwrapper.unwrap(dataSource, HikariDataSource.class);
        this.dataSource = new SingleConnectionDataSource(
                hikariDataSource.getJdbcUrl(),
                hikariDataSource.getUsername(),
                hikariDataSource.getPassword(),
                Boolean.TRUE);
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
                    int timeoutMillis = getProps().pollTimeoutMillis();
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
                .onSuccess(_ -> {
                    if (!running) {
                        log.info("PostgresListener for channel '{}' stopped", channelName);
                        return;
                    }
                    onFailure(new ApplicationException(String.format("PostgresListener for channel '%s' stopped for unknown reason", channelName)));
                });
    }

    public void stop() {
        running = false;
    }
}
