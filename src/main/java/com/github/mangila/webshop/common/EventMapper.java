package com.github.mangila.webshop.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.common.model.Event;
import org.springframework.stereotype.Component;

@Component
public class EventMapper {

    private final ObjectMapper objectMapper;

    public EventMapper(ObjectMapper objectMapper) {
        this.objectMapper = objectMapper;
    }

    public Event toEvent(ChannelTopic channelTopic,
                         String aggregateId,
                         String eventType,
                         Object obj) throws JsonProcessingException {
        var pgEvent = new Event();
        pgEvent.setAggregateId(aggregateId);
        pgEvent.setTopic(channelTopic.toString());
        pgEvent.setEventType(eventType);
        pgEvent.setEventData(objectMapper.writeValueAsString(obj));
        return pgEvent;
    }

}
