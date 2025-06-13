package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Event;
import jakarta.annotation.PostConstruct;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class EventRepository {

    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertNewEvent(Event event) {
        int rows = jdbc.update(
                "INSERT INTO event (event_type, aggregate_id, topic, event_data) VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, event.getEventType());
                    ps.setString(2, event.getAggregateId());
                    ps.setString(3, event.getTopic());
                    ps.setObject(4, event.getEventData(), Types.OTHER); // Set as JSONB
                }
        );
        log.debug("Inserted {} -- {}", event, rows);
    }

    public Event acknowledgeNewEvent(long id) {
        return new Event();
    }

    @PostConstruct
    private void createNotificationTrigger() {
        log.info("Creating function pg_notify(topic,payload) with trigger");
        jdbc.execute("""
                CREATE OR REPLACE FUNCTION notify_new_event_by_topic() RETURNS trigger AS $$
                DECLARE
                    topic TEXT;
                    payload      TEXT;
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