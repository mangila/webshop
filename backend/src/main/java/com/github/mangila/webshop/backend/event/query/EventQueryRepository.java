package com.github.mangila.webshop.backend.event.query;

import com.github.mangila.webshop.backend.common.exception.DatabaseException;
import com.github.mangila.webshop.backend.event.EventRepositoryUtil;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.query.model.EventReplayQuery;
import io.vavr.control.Try;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class EventQueryRepository {

    private final JdbcTemplate jdbc;
    private final EventRepositoryUtil repositoryUtil;

    public EventQueryRepository(JdbcTemplate jdbc,
                                EventRepositoryUtil repositoryUtil) {
        this.jdbc = jdbc;
        this.repositoryUtil = repositoryUtil;
    }

    public List<Event> replay(EventReplayQuery replay) {
        // language=PostgreSQL
        final String sql = """
                SELECT id, type, aggregate_id, topic, data, created
                         FROM event
                         WHERE topic = ? AND aggregate_id = ? AND id >= ?
                         ORDER BY id
                         LIMIT ?
                """;
        Object[] params = new Object[]{
                replay.topic().name(),
                replay.aggregateId(),
                replay.offset(),
                replay.limit()};
        return Try.of(() -> jdbc.query(sql, repositoryUtil.eventEntityRowMapper(), params))
                .map(repositoryUtil::findMany)
                .getOrElseThrow(cause -> new DatabaseException(
                        Event.class,
                        "Replay Events failed",
                        sql,
                        params,
                        cause
                ));
    }
}
