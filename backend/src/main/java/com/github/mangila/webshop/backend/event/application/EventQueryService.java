package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import com.github.mangila.webshop.backend.event.infrastructure.EventQueryRepository;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventQueryService {

    private final EventQueryRepository queryRepository;

    public EventQueryService(EventQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Event> replay(EventReplayQuery replay) {
        return queryRepository.replay(replay);
    }
}
