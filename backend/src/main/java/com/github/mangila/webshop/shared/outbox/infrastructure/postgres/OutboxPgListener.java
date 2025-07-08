package com.github.mangila.webshop.shared.outbox.infrastructure.postgres;

import com.github.mangila.webshop.shared.infrastructure.postgres.AbstractPgNotificationListener;
import com.github.mangila.webshop.shared.infrastructure.postgres.PostgresListenerProps;
import com.github.mangila.webshop.shared.infrastructure.spring.event.OutboxPgListenerFailedEvent;
import com.github.mangila.webshop.shared.infrastructure.spring.event.OutboxPgNotification;
import com.github.mangila.webshop.shared.infrastructure.spring.event.SpringEventPublisher;
import org.postgresql.PGNotification;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

@Service
public class OutboxPgListener extends AbstractPgNotificationListener {

    private final SpringEventPublisher publisher;
    private final JdbcTemplate jdbcTemplate;

    public OutboxPgListener(SpringEventPublisher publisher,
                            DataSourceProperties dataSourceProperties,
                            JdbcTemplate jdbcTemplate) {
        super(dataSourceProperties);
        this.publisher = publisher;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public void onPgNotification(PGNotification pgNotification) {
        publisher.publish(new OutboxPgNotification(pgNotification));
    }

    @Override
    public void onFailure(Throwable cause) {
        publisher.publish(new OutboxPgListenerFailedEvent(cause));
    }

    @Override
    public PostgresListenerProps getProps() {
        return new PostgresListenerProps(
                "outbox",
                "outbox_channel",
                "pg_notify_outbox_channel",
                "trg_outbox_insert_01");
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
                DECLARE
                    payload json;
                BEGIN
                    payload := json_build_object(
                        'id', NEW.id,
                        'aggregateId', NEW.aggregate_id,
                        'domain', NEW.domain,
                        'event', NEW.event
                    );
                    PERFORM pg_notify('%s', payload::text);
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
