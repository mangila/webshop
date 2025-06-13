package com.github.mangila.webshop.common.model;

import java.time.LocalDateTime;

public class Event {

    private Long id;
    private String topic;
    private String eventType;
    private String aggregateId;
    private String eventData;
    private LocalDateTime created;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

    public String getEventData() {
        return eventData;
    }

    public void setEventData(String eventData) {
        this.eventData = eventData;
    }

    public LocalDateTime getCreated() {
        return created;
    }

    public void setCreated(LocalDateTime created) {
        this.created = created;
    }

    @Override
    public String toString() {
        return "Event{" +
                "id=" + id +
                ", topic='" + topic + '\'' +
                ", eventType='" + eventType + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", eventData='" + eventData + '\'' +
                ", created=" + created +
                '}';
    }
}