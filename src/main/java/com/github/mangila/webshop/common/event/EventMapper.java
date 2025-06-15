package com.github.mangila.webshop.common.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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
                         Object eventData) throws JsonProcessingException {
        var pgEvent = new Event();
        pgEvent.setAggregateId(aggregateId);
        pgEvent.setTopic(eventTopic.toString());
        pgEvent.setEventType(eventType);
        pgEvent.setEventData(objectMapper.writeValueAsString(eventData));
        return pgEvent;
    }

}
