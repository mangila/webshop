package com.github.mangila.webshop.backend.event;

import com.github.mangila.webshop.backend.common.util.JsonMapper;
import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.model.EventEntity;
import org.springframework.jdbc.core.DataClassRowMapper;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Optional;

@Component
public class EventRepositoryUtil {

    private final JsonMapper jsonMapper;
    private final DataClassRowMapper<EventEntity> eventEntityRowMapper;

    public EventRepositoryUtil(JsonMapper jsonMapper,
                               DataClassRowMapper<EventEntity> eventEntityRowMapper) {
        this.jsonMapper = jsonMapper;
        this.eventEntityRowMapper = eventEntityRowMapper;
    }

    public DataClassRowMapper<EventEntity> eventEntityRowMapper() {
        return eventEntityRowMapper;
    }

    public Optional<Event> findOne(List<EventEntity> entities) {
        if (CollectionUtils.isEmpty(entities)) {
            return Optional.empty();
        }
        EventEntity entity = entities.getFirst();
        Event event = Event.from(entity, jsonMapper);
        return Optional.of(event);
    }

    public List<Event> findMany(List<EventEntity> entities) {
        return entities.stream()
                .map(entity -> Event.from(entity, jsonMapper))
                .toList();
    }
}
