package com.github.mangila.webshop.backend.event.application.service;

import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.event.application.gateway.EventRepositoryGateway;
import com.github.mangila.webshop.backend.event.domain.command.EventPublishCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;

@NullMarked
@Service
public class EventPublishService {

    private final EventRegistryGateway registryGateway;
    private final EventRepositoryGateway repositoryGateway;

    public EventPublishService(EventRegistryGateway registryGateway,
                               EventRepositoryGateway repositoryGateway) {
        this.registryGateway = registryGateway;
        this.repositoryGateway = repositoryGateway;
    }

    public Event save(EventPublishCommand command) {
        return repositoryGateway.command().save(tryGetEvent(command));
    }

    public List<Event> saveMany(List<EventPublishCommand> commands) {
        var events = commands.stream()
                .map(this::tryGetEvent)
                .toList();
        return repositoryGateway.command().saveAll(events);
    }

    private Event tryGetEvent(EventPublishCommand command) {
        var topic = command.topic();
        var eventType = command.eventType();
        if (!registryGateway.eventTopicRegistry().isRegistered(topic)) {
            throw new ApiException(String.format("Topic is not registered: '%s'", topic), Event.class, HttpStatus.CONFLICT);
        }
        if (!registryGateway.eventTypeRegistry().isRegistered(eventType)) {
            throw new ApiException(String.format("EventType is not registered: '%s'", eventType), Event.class, HttpStatus.CONFLICT);
        }
        return Event.from(command);
    }
}
