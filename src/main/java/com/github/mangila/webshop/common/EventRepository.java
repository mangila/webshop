package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.Map;

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

    public Map<String, Object> emit(Event event) {
        final String sql = """
                INSERT INTO event (event_type, aggregate_id, topic, event_data)
                VALUES (?, ?, ?, ?::jsonb)
                """;
        log.trace("{} -- {}", event, sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, event.getEventType());
            ps.setString(2, event.getAggregateId());
            ps.setString(3, event.getTopic());
            ps.setString(4, event.getEventData());
            return ps;
        }, keyHolder);
        return keyHolder.getKeyList().getFirst();
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