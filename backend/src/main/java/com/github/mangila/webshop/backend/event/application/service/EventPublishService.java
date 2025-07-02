package com.github.mangila.webshop.backend.event.application.service;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.event.application.gateway.EventRepositoryGateway;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

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

    public Event publish(
            String topic,
            String eventType,
            UUID aggregateId,
            JsonNode payload
    ) {
        if (!registryGateway.hasRegisteredTopic(topic)) {
            throw new ApiException(String.format("Topic is not registered - '%s'", topic), Event.class, HttpStatus.CONFLICT);
        }
        if (!registryGateway.hasRegisteredEvent(eventType)) {
            throw new ApiException(String.format("Event type is not registered - '%s'", eventType), Event.class, HttpStatus.CONFLICT);
        }
        return repositoryGateway.save(Event.from(topic, eventType, aggregateId, payload));
    }
}
