package com.github.mangila.webshop.common.event;

import org.springframework.stereotype.Service;

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
}
