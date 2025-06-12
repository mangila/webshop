package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Event;
import jakarta.annotation.PostConstruct;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.sql.Types;

@Repository
public class EventRepository {

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public void insertEvent(Event event) {
        // Use update() for INSERT operations
        var r = jdbc.update(
                "INSERT INTO event (event_type, aggregate_id, topic, event_data) VALUES (?, ?, ?, ?)",
                ps -> {
                    ps.setString(1, event.getEventType());
                    ps.setString(2, event.getAggregateId());
                    ps.setString(3, event.getTopic());
                    ps.setObject(4, event.getEventData(), Types.OTHER); // Set as JSONB
                }
        );
        System.out.println(r);
    }

    @PostConstruct
    public void createFunctionManually() {
        // Split the function creation and trigger creation for better error handling
        jdbc.update("""
                CREATE OR REPLACE FUNCTION notify_event_topic() RETURNS trigger AS $$
                DECLARE
                    channel_name TEXT;
                    payload      TEXT;
                BEGIN
                    channel_name := NEW.topic;
                    payload := json_build_object(
                        'event_id', NEW.event_id,
                        'event_type', NEW.event_type,
                        'aggregate_id', NEW.aggregate_id,
                        'created', NEW.created
                    )::text;
                    PERFORM pg_notify(channel_name, payload);
                    RETURN NEW;
                END;
                $$ LANGUAGE plpgsql;
                """);

        // Create trigger separately to handle potential "trigger already exists" errors
        try {
            jdbc.update("""
                    CREATE TRIGGER event_notify_trigger
                    AFTER INSERT ON event
                    FOR EACH ROW
                    EXECUTE FUNCTION notify_event_topic();
                    """);
        } catch (Exception e) {
            // Log warning or handle case where trigger might already exist
            // This avoids errors on application restart
        }
    }
}