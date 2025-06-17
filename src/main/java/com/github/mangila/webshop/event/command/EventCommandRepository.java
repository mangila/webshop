package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.Optional;

@Repository
public class EventCommandRepository {

    private static final Logger log = LoggerFactory.getLogger(EventCommandRepository.class);

    private final EventMapper eventMapper;
    private final JdbcTemplate jdbc;

    public EventCommandRepository(EventMapper eventMapper,
                                  JdbcTemplate jdbc) {
        this.eventMapper = eventMapper;
        this.jdbc = jdbc;
    }

    public Optional<Event> emit(Event event) {
        final String sql = """
                INSERT INTO event (type, aggregate_id, topic, data)
                VALUES (?, ?, ?, ?::jsonb)
                RETURNING id, type, aggregate_id, topic, data, created
                """;
        var params = new Object[]{
                event.getType(),
                event.getAggregateId(),
                event.getTopic(),
                event.getData()
        };
        log.debug("{} -- {}", Arrays.toString(params), sql);
        var result = jdbc.query(sql,
                eventMapper.getRowMapper(),
                params);
        if (CollectionUtils.isEmpty(result)) {
            return Optional.empty();
        }
        return Optional.of(result.getFirst());
    }
}
