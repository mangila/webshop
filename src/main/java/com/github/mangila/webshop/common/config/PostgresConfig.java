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
        createAcknowledgeEventFunction();
        createNotificationTrigger();
    }

    private void createAcknowledgeEventFunction() {
        log.info("Creating Postgres function acknowledge_event(f_event_id BIGINT, f_event_topic VARCHAR)");
        jdbc.execute("""
                DROP FUNCTION IF EXISTS acknowledge_event(BIGINT, VARCHAR);
                CREATE OR REPLACE FUNCTION acknowledge_event(f_event_id BIGINT, f_event_topic VARCHAR)
                 RETURNS TABLE (
                     id BIGINT,
                     event_type VARCHAR,
                     aggregate_id VARCHAR,
                     topic VARCHAR,
                     event_data JSONB,
                     created TIMESTAMP
                 ) AS $$
                 BEGIN
                     -- Update the offset first
                     INSERT INTO event_offset (event_topic, current_offset)
                     VALUES (f_event_topic, f_event_id)
                     ON CONFLICT (event_topic) DO UPDATE
                     SET current_offset = EXCLUDED.current_offset;
                
                     -- Return the event data with explicit column aliases matching the function's output columns
                     RETURN QUERY
                     SELECT
                         e.id AS id,
                         e.event_type AS event_type,
                         e.aggregate_id AS aggregate_id,
                         e.topic AS topic,
                         e.event_data AS event_data,
                         e.created AS created
                     FROM event e
                     WHERE e.id = f_event_id AND e.topic = f_event_topic;
                 END;
                 $$ LANGUAGE plpgsql;
                """);
    }

    private void createNotificationTrigger() {
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
}
