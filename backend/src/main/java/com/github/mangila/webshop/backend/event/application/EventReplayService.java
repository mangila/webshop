package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import com.github.mangila.webshop.backend.event.infrastructure.EventQueryRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventReplayService {

    private final EventQueryRepository queryRepository;

    public EventReplayService(EventQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Event> replay(EventReplayQuery replay) {
        return queryRepository.replay(replay);
    }
}
