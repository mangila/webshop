package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import com.github.mangila.webshop.backend.event.infrastructure.EventCommandRepository;
import com.github.mangila.webshop.backend.event.infrastructure.EventQueryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventRepositoryGateway {

    private final EventCommandRepository eventCommandRepository;
    private final EventQueryRepository eventQueryRepository;

    public EventRepositoryGateway(EventCommandRepository eventCommandRepository, EventQueryRepository eventQueryRepository) {
        this.eventCommandRepository = eventCommandRepository;
        this.eventQueryRepository = eventQueryRepository;
    }

    public List<Event> replay(EventReplayQuery replay) {
        return eventQueryRepository.replay(replay);
    }

    public Event save(Event event) {
        return eventCommandRepository.save(event);
    }
}
