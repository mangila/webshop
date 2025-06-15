package com.github.mangila.webshop.common.config;

import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;

@Configuration
public class PostgresConfig {

    private static final Logger log = LoggerFactory.getLogger(PostgresConfig.class);

    private final JdbcTemplate jdbc;

    public PostgresConfig(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    @PostConstruct
    public void init() {
        insertNewPendingEvent();
        notifyNewEventByTopic();
        acknowledgeEvent();
    }

    private void notifyNewEventByTopic() {
        log.info("Creating Postgres function notify_new_event_by_topic() with trigger on event table");
        jdbc.execute("""
                DROP FUNCTION IF EXISTS notify_new_event_by_topic();
                CREATE OR REPLACE FUNCTION notify_new_event_by_topic() RETURNS trigger AS $$
                DECLARE
                    topic TEXT;
                    payload TEXT;
                BEGIN
                    topic := NEW.topic;
                    payload := json_build_object(
                        'id', NEW.id,
                        'topic', topic
                    )::text;
                    PERFORM pg_notify(topic, payload);
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        jdbc.execute("""
                DROP TRIGGER IF EXISTS notify_new_event_by_topic_trigger ON event;
                CREATE TRIGGER notify_new_event_by_topic_trigger
                AFTER INSERT ON event
                FOR EACH ROW
                EXECUTE FUNCTION notify_new_event_by_topic();
                """);
    }

    private void insertNewPendingEvent() {
        log.info("Creating Postgres function insert_new_pending_event() with trigger on event table");
        jdbc.execute("""
                DROP FUNCTION IF EXISTS insert_new_pending_event();
                CREATE OR REPLACE FUNCTION insert_new_pending_event() RETURNS TRIGGER AS $$
                BEGIN
                INSERT INTO event_processed (event_id, event_topic, status)
                     VALUES (NEW.id, NEW.topic, 'PENDING');
                RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);
        jdbc.execute("""
                DROP TRIGGER IF EXISTS insert_new_pending_event ON event;
                CREATE TRIGGER insert_new_pending_event
                AFTER INSERT ON event
                FOR EACH ROW
                EXECUTE FUNCTION insert_new_pending_event();
                """);
    }

    private void acknowledgeEvent() {
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION acknowledge_event(
                    f_event_id BIGINT
                )
                RETURNS TABLE (
                        id         BIGINT ,
                        topic        VARCHAR,
                        type   VARCHAR,
                        aggregate_id VARCHAR,
                        data   JSONB  ,
                        created      TIMESTAMP
                ) AS
                $$
                BEGIN
                    UPDATE event_processed
                    SET status = 'ACKNOWLEDGED'
                    WHERE event_id = f_event_id;
                
                    RETURN QUERY
                    SELECT e.id,
                           e.topic,
                           e.type,
                           e.aggregate_id,
                           e.data,
                           e.created
                    FROM event e
                    WHERE e.id = f_event_id;
                END;
                $$ LANGUAGE plpgsql;
                """);
    }
}
