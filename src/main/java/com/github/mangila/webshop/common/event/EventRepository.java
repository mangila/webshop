package com.github.mangila.webshop.common.event;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EventRepository {

    private static final Logger log = LoggerFactory.getLogger(EventRepository.class);

    private final JdbcTemplate jdbc;

    public EventRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Event emit(Event event) {
        final String sql = """
                INSERT INTO event (type, aggregate_id, topic, data, metadata)
                VALUES (?, ?, ?, ?::jsonb, ?::jsonb)
                RETURNING id, type, aggregate_id, topic, data, created, metadata
                """;
        log.debug("{} -- {}", event, sql);
        try {
            var params = new Object[]{
                    event.getType(),
                    event.getAggregateId(),
                    event.getTopic(),
                    event.getData(),
                    event.getMetadata()
            };
            return jdbc.queryForObject(sql,
                    new BeanPropertyRowMapper<>(Event.class),
                    params);
        } catch (Exception e) {
            var msg = "Failed to emit event -- %s".formatted(event);
            log.error(msg, e);
            throw new RuntimeException(msg);
        }
    }
}