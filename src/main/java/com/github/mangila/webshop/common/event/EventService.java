package com.github.mangila.webshop.common.event;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class EventService {

    private final EventRepository repository;
    private final EventMapper eventMapper;

    public EventService(EventRepository repository,
                        EventMapper eventMapper) {
        this.repository = repository;
        this.eventMapper = eventMapper;
    }

    public Event emit(EventTopic eventTopic,
                      String aggregateId,
                      String eventType,
                      Object eventData) throws JsonProcessingException {
        var event = eventMapper.toEvent(
                eventTopic,
                aggregateId,
                eventType,
                eventData
        );
        var map = repository.emit(event);
        event.setId((Long) map.get("id"));
        event.setEventData(((PGobject) map.get("event_data")).getValue());
        event.setCreated(((Timestamp) map.get("created")).toLocalDateTime());
        return event;
    }

    public Event acknowledge(Long eventId) {
        return repository.acknowledge(eventId);
    }

    public List<Event> queryPendingEventsByTopic(EventTopic topic) {
        return repository.queryPendingEventsByTopic(topic);
    }
}
