package com.github.mangila.webshop.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonUtils;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ObjectMapper objectMapper;

    public EventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Event toEvent(EventTopic eventTopic,
                         String aggregateId,
                         String eventType,
                         Object data,
                         Object metadata) {
        var pgEvent = new Event();
        pgEvent.setAggregateId(aggregateId);
        pgEvent.setTopic(eventTopic.toString());
        pgEvent.setType(eventType);
        pgEvent.setData(JsonUtils.ensureSerialize(data, objectMapper));
        pgEvent.setMetadata(JsonUtils.serialize(metadata, objectMapper));
        return pgEvent;
    }

}
