package com.github.mangila.webshop.event.model;

import com.fasterxml.jackson.databind.JsonNode;

import java.time.LocalDateTime;

public class Event {

    private Long id;
    private String topic;
    private String type;
    private String aggregateId;
    private String data;
    private LocalDateTime created;

    public static Event from(EventTopic eventTopic,
                             String aggregateId,
                             String eventType,
                             JsonNode data) {
        var event = new Event();
        event.setAggregateId(aggregateId);
        event.setTopic(eventTopic.toString());
        event.setType(eventType);
        event.setData(data.toString());
        return event;
    }

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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getAggregateId() {
        return aggregateId;
    }

    public void setAggregateId(String aggregateId) {
        this.aggregateId = aggregateId;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
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
                ", type='" + type + '\'' +
                ", aggregateId='" + aggregateId + '\'' +
                ", data='" + data + '\'' +
                ", created=" + created +
                '}';
    }
}