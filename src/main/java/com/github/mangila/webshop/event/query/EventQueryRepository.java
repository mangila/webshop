package com.github.mangila.webshop.event.query;

import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventEntity;
import com.github.mangila.webshop.event.model.EventMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventQueryRepository {

    private static final Logger log = LoggerFactory.getLogger(EventQueryRepository.class);

    private final EventMapper eventMapper;
    private final JdbcTemplate jdbc;

    public EventQueryRepository(EventMapper eventMapper,
                                JdbcTemplate jdbc) {
        this.eventMapper = eventMapper;
        this.jdbc = jdbc;
    }

    public List<Event> replay(String topic,
                              String aggregateId,
                              long offset,
                              int limit) {
        var params = new Object[]{topic, aggregateId, offset, limit};
        final String sql = """
                SELECT id, type, aggregate_id, topic, data, created
                         FROM event
                         WHERE topic = ? AND aggregate_id = ? AND id >= ?
                         ORDER BY id
                         LIMIT ?
                """;
        List<EventEntity> entities = jdbc.query(sql,
                eventMapper.getRowMapper(),
                params);
        return entities.stream()
                .map(eventMapper::toEvent)
                .toList();
    }
}
