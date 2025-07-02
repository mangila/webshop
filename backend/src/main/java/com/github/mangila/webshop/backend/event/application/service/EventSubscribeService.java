package com.github.mangila.webshop.backend.event.application.service;

import com.github.mangila.webshop.backend.common.exception.ApiException;
import com.github.mangila.webshop.backend.event.application.gateway.EventRegistryGateway;
import com.github.mangila.webshop.backend.event.application.gateway.EventRepositoryGateway;
import com.github.mangila.webshop.backend.event.domain.command.EventSubscribeCommand;
import com.github.mangila.webshop.backend.event.domain.model.Event;
import com.github.mangila.webshop.backend.event.domain.model.EventSubscriber;
import com.github.mangila.webshop.backend.event.domain.query.EventFindByTopicAndTypeAndOffsetQuery;
import com.github.mangila.webshop.backend.event.domain.query.EventSubscriberByIdQuery;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EventSubscribeService {

    private final EventRegistryGateway eventRegistryGateway;
    private final EventRepositoryGateway eventRepositoryGateway;

    public EventSubscribeService(EventRegistryGateway eventRegistryGateway,
                                 EventRepositoryGateway eventRepositoryGateway) {
        this.eventRegistryGateway = eventRegistryGateway;
        this.eventRepositoryGateway = eventRepositoryGateway;
    }

    public EventSubscriber save(EventSubscribeCommand command) {
        var topic = command.topic();
        var type = command.type();
        if (!eventRegistryGateway.eventTopicRegistry().isRegistered(topic)) {
            throw new ApiException(String.format("Topic is not registered: '%s'", topic), Event.class, HttpStatus.CONFLICT);
        }
        if (!eventRegistryGateway.eventTypeRegistry().isRegistered(type)) {
            throw new ApiException(String.format("EventType is not registered: '%s'", type), Event.class, HttpStatus.CONFLICT);
        }
        EventSubscriber c = EventSubscriber.from(command);
        return eventRepositoryGateway.subscriber().save(c);
    }

    public Optional<EventSubscriber> findById(EventSubscriberByIdQuery query) {
        return eventRepositoryGateway.subscriber().findById(query.id());
    }

    public List<Event> findEventsByTopicAndTypeAndOffset(EventFindByTopicAndTypeAndOffsetQuery query) {
        return eventRepositoryGateway.query().consume(query);
    }

    public void acknowledge(EventSubscriber subscriber, List<Event> events) {
        long max = events.stream()
                .mapToLong(Event::getId)
                .max()
                .orElseThrow(() -> new ApiException("No events to acknowledge", Event.class, HttpStatus.CONFLICT));
        subscriber.setLatestOffset(max);
        eventRepositoryGateway.subscriber().save(subscriber);
    }
}
