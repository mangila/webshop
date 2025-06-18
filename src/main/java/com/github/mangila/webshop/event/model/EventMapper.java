package com.github.mangila.webshop.event.model;

import com.github.mangila.webshop.common.util.JsonMapper;
import org.jetbrains.annotations.NotNull;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final JsonMapper jsonMapper;
    private final DataClassRowMapper<EventEntity> rowMapper;

    public EventMapper(JsonMapper jsonMapper) {
        this.jsonMapper = jsonMapper;
        this.rowMapper = new DataClassRowMapper<>(EventEntity.class);
    }

    public Event toEvent(@NotNull EventEntity entity) {
        return new Event(
                entity.id(),
                entity.topic(),
                entity.type(),
                entity.aggregateId(),
                jsonMapper.toJsonNode(entity.data()),
                entity.created().toInstant()
        );
    }

    public EventEntity toEntity(@NotNull EventCommand command) {
        return new EventEntity(
                null,
                command.topic(),
                command.eventType(),
                command.aggregateId(),
                command.data(),
                null
        );
    }

    public DataClassRowMapper<EventEntity> getRowMapper() {
        return rowMapper;
    }
}
