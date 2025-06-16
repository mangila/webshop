package com.github.mangila.webshop.event.query;

import com.github.mangila.webshop.event.model.Event;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventQueryService {

    private final EventQueryRepository queryRepository;

    public EventQueryService(EventQueryRepository queryRepository) {
        this.queryRepository = queryRepository;
    }

    public List<Event> replay(String topic, String aggregateId, Long offset) {
        if (offset == null) {
            offset = 1L;
        }
        return queryRepository.replay(topic, aggregateId, offset);
    }
}
