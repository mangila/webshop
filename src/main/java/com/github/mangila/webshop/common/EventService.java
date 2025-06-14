package com.github.mangila.webshop.common;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.github.mangila.webshop.common.model.ChannelTopic;
import com.github.mangila.webshop.common.model.Event;
import com.github.mangila.webshop.product.model.Product;
import com.github.mangila.webshop.product.model.ProductEventType;
import org.postgresql.util.PGobject;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
public class EventService {

    private final EventRepository repository;
    private final EventMapper eventMapper;

    public EventService(EventRepository repository,
                        EventMapper eventMapper) {
        this.repository = repository;
        this.eventMapper = eventMapper;
    }

    public Event acknowledgeEvent(long id, String topic) {
        return repository.acknowledgeEvent(id, topic);
    }

    public Event emit(ProductEventType eventType, Product product) throws JsonProcessingException {
        var event = eventMapper.toEvent(
                ChannelTopic.PRODUCTS,
                product.getId(),
                eventType.toString(),
                product
        );
        var map = repository.emit(event);
        event.setId((Long) map.get("id"));
        event.setEventData(((PGobject) map.get("event_data")).getValue());
        event.setCreated(((Timestamp) map.get("created")).toLocalDateTime());
        return event;
    }
}
