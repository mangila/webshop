package com.github.mangila.webshop.backend.event.application;

import com.fasterxml.jackson.databind.JsonNode;
import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.common.model.ApplicationUuid;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.infrastructure.EventCommandRepository;
import org.jspecify.annotations.NullMarked;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@NullMarked
@Service
public class EventPublishService {

    private final DefaultEventTypeRegistry typeRegistry;
    private final DefaultEventTopicRegistry topicRegistry;
    private final EventCommandRepository repository;

    public EventPublishService(DefaultEventTypeRegistry typeRegistry,
                               DefaultEventTopicRegistry topicRegistry,
                               EventCommandRepository repository) {
        this.typeRegistry = typeRegistry;
        this.topicRegistry = topicRegistry;
        this.repository = repository;
    }

    public Event publish(
            String topic,
            String eventType,
            ApplicationUuid aggregateId,
            JsonNode payload
    ) {
        if (!topicRegistry.isRegistered(topic)) {
            throw new ApiException(String.format("Topic is not registered - '%s'", topic), Event.class, HttpStatus.CONFLICT);
        }
        if (!typeRegistry.isRegistered(eventType)) {
            throw new ApiException(String.format("Event type is not registered - '%s'", eventType), Event.class, HttpStatus.CONFLICT);
        }
        return repository.save(new Event(topic, eventType, aggregateId, payload));
    }
}
