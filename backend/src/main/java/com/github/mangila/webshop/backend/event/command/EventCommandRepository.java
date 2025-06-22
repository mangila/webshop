package com.github.mangila.webshop.backend.event.command;

import com.github.mangila.webshop.backend.common.util.exception.DatabaseException;
import com.github.mangila.webshop.backend.event.EventRepositoryUtil;
import com.github.mangila.webshop.backend.event.command.model.EventEmitCommand;
import com.github.mangila.webshop.backend.event.model.Event;
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

    public Optional<Event> emit(EventEmitCommand command) {
        final String sql = """
                INSERT INTO event (type, aggregate_id, topic, data)
                VALUES (?, ?, ?, ?::jsonb)
                RETURNING id, type, aggregate_id, topic, data, created
                """;
        var params = new Object[]{
                command.eventType(),
                command.aggregateId(),
                command.eventTopic().name(),
                command.eventData().toString()
        };
        return Try.of(() -> jdbc.query(sql, repositoryUtil.eventEntityRowMapper(), params))
                .map(repositoryUtil::findOne)
                .getOrElseThrow(cause -> new DatabaseException(
                        Event.class,
                        "Emit Event failed",
                        sql,
                        params,
                        cause
                ));
    }
}
