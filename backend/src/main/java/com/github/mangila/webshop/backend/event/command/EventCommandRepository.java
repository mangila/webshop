package com.github.mangila.webshop.backend.event.command;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseOperationFailedException;
import com.github.mangila.webshop.backend.event.EventRepositoryUtil;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventEntity;
import io.vavr.control.Try;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

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
                entity.eventTopic().name(),
                entity.data()
        };
        return Try.of(() -> jdbc.query(sql, repositoryUtil.eventEntityRowMapper(), params))
                .map(repositoryUtil::extractOneResult)
                .getOrElseThrow(throwable -> new DatabaseOperationFailedException("emit Event", params, throwable));
    }
}
