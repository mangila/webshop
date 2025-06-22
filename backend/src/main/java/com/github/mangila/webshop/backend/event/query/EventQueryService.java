package com.github.mangila.webshop.backend.event.query;

import com.github.mangila.webshop.backend.event.model.Event;
import com.github.mangila.webshop.backend.event.query.model.EventReplayQuery;
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
