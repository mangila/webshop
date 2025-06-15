package com.github.mangila.webshop.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.Statement;
import java.util.List;
import java.util.Map;

@Repository
public class EventRepository {

    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

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

    public Event acknowledge(Long eventId) {
        final String sql = """
                SELECT id, event_type, aggregate_id, topic, event_data, created
                FROM acknowledge_event(?)
                """;
        return jdbc.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Event.class),
                eventId);
    }

    public List<Event> queryPendingEventsByTopic(EventTopic topic) {
        final String sql = """
                SELECT e.id,
                       e.event_type,
                       e.aggregate_id,
                       e.topic,
                       e.event_data,
                       e.created
                FROM event e
                LEFT JOIN event_processed ep ON e.id = ep.event_id
                WHERE e.topic = ? AND ep.status = ?
                """;
        return jdbc.query(
                sql,
                new BeanPropertyRowMapper<>(Event.class),
                topic.toString(),
                EventStatus.PENDING.toString());
    }
}