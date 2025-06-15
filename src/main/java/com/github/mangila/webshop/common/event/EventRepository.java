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
                INSERT INTO event (type, aggregate_id, topic, data)
                VALUES (?, ?, ?, ?::jsonb)
                """;
        log.trace("{} -- {}", event, sql);
        var keyHolder = new GeneratedKeyHolder();
        jdbc.update(connection -> {
            var ps = connection.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, event.getType());
            ps.setString(2, event.getAggregateId());
            ps.setString(3, event.getTopic());
            ps.setString(4, event.getData());
            return ps;
        }, keyHolder);
        return keyHolder.getKeyList().getFirst();
    }

    public Event acknowledge(Long eventId) {
        final String sql = """
                SELECT id, type, aggregate_id, topic, data, created
                FROM acknowledge_event(?)
                """;
        var event = jdbc.queryForObject(
                sql,
                new BeanPropertyRowMapper<>(Event.class),
                eventId);
        log.trace("Acknowledged event -- {}", event);
        return event;
    }

    public List<Long> queryEventIdsByTopicAndStatus(EventTopic topic, EventStatus status) {
        final String sql = """
                SELECT e.id
                FROM event e
                INNER JOIN event_processed ep ON e.id = ep.event_id
                WHERE e.topic = ? AND ep.status = ?
                """;
        var list = jdbc.query(
                sql,
                (rs, rowNum) -> rs.getLong("id"),
                topic.toString(),
                status.toString());
        log.trace("Found {} events for topic -- {} and status -- {}", list.size(), topic, status);
        return list;
    }

    public int incrementEventFailCounter(long id) {
        final String sql = """
                UPDATE event_processed
                SET fail_counter = fail_counter + 1
                WHERE event_id = ?
                RETURNING fail_counter
                """;
        int updatedCount = jdbc.queryForObject(sql, Integer.class, id);
        log.trace("Incremented fail counter for event with id -- {}", id);
        return updatedCount;
    }

    public void changeEventStatus(long id, EventStatus status) {
        final String sql = """
                UPDATE event_processed
                SET status = ?
                WHERE event_id = ?
                """;
        jdbc.update(sql, status, id);
        log.trace("Failed event with id -- {}", id);
    }
}