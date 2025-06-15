package com.github.mangila.webshop.common.event;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.util.JsonUtils;
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
                         Object eventData) {
        var pgEvent = new Event();
        pgEvent.setAggregateId(aggregateId);
        pgEvent.setTopic(eventTopic.toString());
        pgEvent.setType(eventType);
        pgEvent.setData(JsonUtils.ensureSerialize(eventData, objectMapper));
        return pgEvent;
    }

}
