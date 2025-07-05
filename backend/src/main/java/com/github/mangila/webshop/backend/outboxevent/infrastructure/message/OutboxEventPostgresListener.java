package com.github.mangila.webshop.backend.outboxevent.infrastructure.message;

import com.github.mangila.webshop.backend.common.AbstractPostgresNotificationListener;
import com.github.mangila.webshop.backend.common.SpringEventPublisher;
import com.github.mangila.webshop.backend.common.props.PostgresListenerProps;
import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresListenerFailedEvent;
import com.github.mangila.webshop.backend.outboxevent.domain.springevent.OutboxEventPostgresNotification;
import com.zaxxer.hikari.HikariConfig;
import org.postgresql.PGNotification;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OutboxEventPostgresListener extends AbstractPostgresNotificationListener {

    private final SpringEventPublisher publisher;
    private final JdbcTemplate jdbcTemplate;

    public OutboxEventPostgresListener(SpringEventPublisher publisher,
                                       HikariConfig hikariConfig,
                                       JdbcTemplate jdbcTemplate) {
        super(hikariConfig);
        this.publisher = publisher;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onPgNotification(PGNotification pgNotification) {
        publisher.publish(new OutboxEventPostgresNotification(pgNotification));
    }

    @Override
    public void onFailure(Throwable cause) {
        publisher.publish(new OutboxEventPostgresListenerFailedEvent(cause));
    }

    @Override
    public PostgresListenerProps getProps() {
        return new PostgresListenerProps(
                "outbox_event",
                "outbox_event_channel",
                "pg_notify_outbox_event_channel",
                "trg_outbox_event_insert_01");
    }

    @Override
    public void setUp() {
        var props = getProps();
        // language=PostgreSQL
        jdbcTemplate.execute("""
                DROP FUNCTION IF EXISTS %s();
                """.formatted(props.functionName()));
        // language=PostgreSQL
        jdbcTemplate.execute("""
                CREATE OR REPLACE FUNCTION %s()
                RETURNS trigger AS $$
                BEGIN
                PERFORM pg_notify('%s', NEW.id::text);
                RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """.formatted(props.functionName(), props.channelName()));
        // language=PostgreSQL
        jdbcTemplate.execute("""
                DROP TRIGGER IF EXISTS %s ON %s;
                """.formatted(props.triggerName(), props.tableName()));
        // language=PostgreSQL
        jdbcTemplate.execute("""
                CREATE TRIGGER %s
                AFTER INSERT ON %s
                FOR EACH ROW
                EXECUTE FUNCTION %s();
                """.formatted(props.triggerName(), props.tableName(), props.functionName()));
    }

    @Override
    public void destroy() {
        stop();
    }
}
