package com.github.mangila.webshop.common.event;

import org.springframework.stereotype.Service;

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
                      String type,
                      Object data,
                      Object metadata) {
        var event = eventMapper.toEvent(
                eventTopic,
                aggregateId,
                type,
                data,
                metadata
        );
        return repository.emit(event);
    }

    public Event acknowledge(Long eventId) {
        return repository.acknowledge(eventId);
    }

    public List<Long> queryEventIdsByTopicAndStatus(EventTopic topic, EventStatus status) {
        return repository.queryEventIdsByTopicAndStatus(topic, status);
    }

    public int incrementEventFailCounter(long id) {
        return repository.incrementEventFailCounter(id);
    }

    public void changeEventStatus(long id, EventStatus status) {
        repository.changeEventStatus(id, status);
    }
}
