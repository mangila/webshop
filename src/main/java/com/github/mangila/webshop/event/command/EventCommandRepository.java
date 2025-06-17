package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.model.Event;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.Arrays;

@Repository
public class EventCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(EventCommandRepository.class);

    private final JdbcTemplate jdbc;

    public EventCommandRepository(JdbcTemplate jdbc) {
        this.jdbc = jdbc;
    }

    public Event emit(Event event) {
        final String sql = """
                INSERT INTO event (type, aggregate_id, topic, data, metadata)
                VALUES (?, ?, ?, ?::jsonb, ?::jsonb)
                RETURNING id, type, aggregate_id, topic, data, created, metadata
                """;
        try {
            var params = new Object[]{
                    event.getType(),
                    event.getAggregateId(),
                    event.getTopic(),
                    event.getData(),
                    event.getMetadata()
            };
            log.debug("{} -- {}", Arrays.toString(params), sql);
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
