package com.github.mangila.webshop.common.model;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Notification {

    @JsonProperty("event_id")
    private final Long eventId;
    @JsonProperty("event_type")
    private final String eventType;
    @JsonProperty("aggregate_id")
    private final String aggregateId;

    public Notification(Long eventId, String eventType, String aggregateId) {
        this.eventId = eventId;
        this.eventType = eventType;
        this.aggregateId = aggregateId;
    }

    public Long getEventId() {
        return eventId;
    }

    public String getEventType() {
        return eventType;
    }

    public String getAggregateId() {
        return aggregateId;
    }
}
