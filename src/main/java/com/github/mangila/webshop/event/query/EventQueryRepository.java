package com.github.mangila.webshop.event.query;

import com.github.mangila.webshop.event.EventRepositoryUtil;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventEntity;
import org.jetbrains.annotations.NotNull;
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

    public List<Event> replay(@NotNull String topic,
                              @NotNull String aggregateId,
                              long offset,
                              int limit) {
        var params = new Object[]{
                topic,
                aggregateId,
                offset,
                limit};
        final String sql = """
                SELECT id, type, aggregate_id, topic, data, created
                         FROM event
                         WHERE topic = ? AND aggregate_id = ? AND id >= ?
                         ORDER BY id
                         LIMIT ?
                """;
        List<EventEntity> entities = jdbc.query(sql, repositoryUtil.eventEntityRowMapper(), params);
        return repositoryUtil.extractMany(entities);
    }
}
