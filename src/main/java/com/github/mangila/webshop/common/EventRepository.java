package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {

    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    private final RowMapper<Event> eventRowMapper = (rs, rowNum) -> {
        Event event = new Event();
        event.setId(rs.getLong("id"));
        event.setEventType(rs.getString("event_type"));
        event.setAggregateId(rs.getString("aggregate_id"));
        event.setTopic(rs.getString("topic"));
        event.setEventData(rs.getString("event_data"));
        event.setCreated(rs.getTimestamp("created").toLocalDateTime());
        return event;
    };

    public void emit(Event event) {
        final String sql = """
                INSERT INTO event (event_type, aggregate_id, topic, event_data)
                VALUES (?, ?, ?, ?::jsonb)
                """;
        log.trace("{} -- {}", event, sql);
        jdbc.update(sql,
                preparedStatement -> {
                    preparedStatement.setString(1, event.getEventType());
                    preparedStatement.setString(2, event.getAggregateId());
                    preparedStatement.setString(3, event.getTopic());
                    preparedStatement.setString(4, event.getEventData());
                });
    }

    public Event acknowledgeEvent(long id, String topic) {
        final String sql = """
                SELECT id, event_type, aggregate_id, topic, event_data, created
                FROM acknowledge_event(?, ?)
                """;
        log.trace("{} -- {} -- {}", sql, id, topic);
        var event = jdbc.queryForObject(sql, eventRowMapper, id, topic);
        log.debug("Acknowledged event: {}", event);
        return event;
    }
}