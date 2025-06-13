package com.github.mangila.webshop.common;

import com.github.mangila.webshop.common.model.Event;
import org.springframework.stereotype.Service;

@Service
public class EventService {

    private final EventRepository repository;

    public EventService(EventRepository repository) {
        this.repository = repository;
    }

    public void insertNewEvent(Event event) {
        repository.insertNewEvent(event);
    }

    public Event acknowledgeNewEvent(long id) {
        return repository.acknowledgeNewEvent(id);
    }
}
