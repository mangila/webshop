package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.EventRepositoryUtil;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventEntity;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public class EventCommandRepository {

    private final JdbcTemplate jdbc;
    private final EventRepositoryUtil repositoryUtil;

    public EventCommandRepository(JdbcTemplate jdbc,
                                  EventRepositoryUtil repositoryUtil) {
        this.jdbc = jdbc;
        this.repositoryUtil = repositoryUtil;
    }

    public Optional<Event> emit(EventEntity entity) {
        final String sql = """
                INSERT INTO event (type, aggregate_id, topic, data)
                VALUES (?, ?, ?, ?::jsonb)
                RETURNING id, type, aggregate_id, topic, data, created
                """;
        var params = new Object[]{
                entity.type(),
                entity.aggregateId(),
                entity.topic(),
                entity.data()
        };
        List<EventEntity> result = jdbc.query(sql,
                repositoryUtil.eventEntityRowMapper(),
                params);
        return repositoryUtil.extractOneResult(result);
    }
}
