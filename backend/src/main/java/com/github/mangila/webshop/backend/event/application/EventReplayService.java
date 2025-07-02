package com.github.mangila.webshop.backend.event.application;

import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.query.EventReplayQuery;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EventReplayService {

    private final EventRepositoryGateway repositoryGateway;

    public EventReplayService(EventRepositoryGateway repositoryGateway) {
        this.repositoryGateway = repositoryGateway;
    }

    public List<Event> replay(EventReplayQuery replay) {
        return repositoryGateway.replay(replay);
    }
}
