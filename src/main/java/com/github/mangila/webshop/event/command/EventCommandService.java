package com.github.mangila.webshop.event.command;

import com.github.mangila.webshop.event.EventMapper;
import com.github.mangila.webshop.event.model.Event;
import com.github.mangila.webshop.event.model.EventTopic;
import org.springframework.stereotype.Service;

@Service
public class EventCommandService {

    private final EventMapper eventMapper;
    private final EventCommandRepository commandRepository;

    public EventCommandService(EventMapper eventMapper,
                               EventCommandRepository commandRepository) {
        this.eventMapper = eventMapper;
        this.commandRepository = commandRepository;
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
        return commandRepository.emit(event);
    }
}
