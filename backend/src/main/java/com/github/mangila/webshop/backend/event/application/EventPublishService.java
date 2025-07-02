package com.github.mangila.webshop.backend.event.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.UUID;

@NullMarked
@Service
public class EventPublishService {

    private final DefaultEventTypeRegistry typeRegistry;
    private final DefaultEventTopicRegistry topicRegistry;
    private final EventRepositoryGateway repositoryGateway;

    public EventPublishService(DefaultEventTypeRegistry typeRegistry,
                               DefaultEventTopicRegistry topicRegistry,
                               EventRepositoryGateway repositoryGateway) {
        this.typeRegistry = typeRegistry;
        this.topicRegistry = topicRegistry;
        this.repositoryGateway = repositoryGateway;
    }

    public Event publish(
            String topic,
            String eventType,
            UUID aggregateId,
            JsonNode payload
    ) {
        if (!topicRegistry.isRegistered(topic)) {
            throw new ApiException(String.format("Topic is not registered - '%s'", topic), Event.class, HttpStatus.CONFLICT);
        }
        if (!typeRegistry.isRegistered(eventType)) {
            throw new ApiException(String.format("Event type is not registered - '%s'", eventType), Event.class, HttpStatus.CONFLICT);
        }
        return repositoryGateway.save(Event.from(topic, eventType, aggregateId, payload));
    }
}
