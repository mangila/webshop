package com.github.mangila.pokedex.webshop.shared.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class Event {

    private Long eventId;
    private String topic;
    private String eventType;
    private String aggregateId;
    private JsonNode eventData;
    private LocalDateTime created;

    public Long getEventId() {
        return eventId;
    }

    public void setEventId(Long eventId) {
        this.eventId = eventId;
    }

    public String getTopic() {
        return topic;
    }

    public void setTopic(String topic) {
        this.topic = topic;
    }

    public String getEventType() {
        return eventType;
    }

    public void setEventType(String eventType) {
        this.eventType = eventType;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public JsonNode getEventData() {
        return eventData;
    }

    public void setEventData(JsonNode eventData) {
        this.eventData = eventData;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }
}